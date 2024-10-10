package com.wevioo.fgdb.extract.batch.configuration;


import com.wevioo.fgdb.extract.batch.listener.JobCompletionListener;
import com.wevioo.fgdb.extract.batch.processor.XmlFileItemProcessor;
import com.wevioo.fgdb.extract.batch.processor.XmlFileItemSecondProcessor;
import com.wevioo.fgdb.extract.batch.reader.XmlFileItemReader;
import com.wevioo.fgdb.extract.batch.reader.XmlFileItemSecondReader;
import com.wevioo.fgdb.extract.batch.writer.SecondResultWriter;
import com.wevioo.fgdb.extract.batch.writer.ValidationResultWriter;
import generated.Vuc;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
@Configuration
//@EnableBatchProcessing
public
class BatchConfiguration {

    private final AtomicBoolean stopCondition = new AtomicBoolean(false);
    AtomicInteger counter = new AtomicInteger(0);  // Initialize counter with 0


    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final Resource[] inputFiles;
//    @Autowired
//    @Qualifier("dataSource")
//    private DataSource dataSource;


    @Autowired
    private JobCompletionListener jobCompletionListener;

    @Autowired
    private ChunkFilePathRepository chunkFilePathRepository;


    @Autowired
    private DirectoryService directoryService;


    public BatchConfiguration(JobRepository jobRepository,
                       PlatformTransactionManager transactionManager,
                       @Value("classpath*:input/*.xml") Resource[] inputFiles) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.inputFiles = inputFiles;

    }

    @Bean
    public
    Job parallelJob() throws Exception {
        return new JobBuilder("parallelJob", jobRepository)
                .start(masterStep(null , null))
              //  .next(masterSecondStep())
                .listener(jobCompletionListener)
                .build();
    }


    @Bean
    @JobScope
    public
    Step masterStep(@Value("#{jobParameters['pathRepository']}") String pathRepository , @Value("#{jobParameters['idRepository']}") Long idRepository) throws Exception {
        // Split the XML file into chunks
        List<Directory> stopRepertories = directoryService.getListDirectory();

        // Check if there are any directories with status "STOP"
        if (stopRepertories.isEmpty()) {
            throw new IllegalStateException("No directories with status STOP found.");
        }

        List<String> filePaths = directoryService.getFilePathsFromDirectory(pathRepository);

        if (filePaths.isEmpty()) {
            throw new IllegalStateException("No files found in the selected directory.");
        }

        String inputFilePath = filePaths.get(0);
        System.out.println("BANK TO BE TREATED "+inputFilePath)      ;
        List<XmlFileSplitter.Chunk> chunks = XmlFileSplitter.splitFile(new FileInputStream(inputFilePath), 600);
       // List<XmlFileSplitter.Chunk> chunks = XmlFileSplitter.splitFile(inputFiles[0].getInputStream(), 600);
        directoryService.updateRepertoryStatus(idRepository ,DirectoryStatus.EN_COURS);
        return new StepBuilder("masterStep", jobRepository)
                .partitioner("slaveStep", new XMLPartitioner( chunkFilePathRepository ,chunks))
                .partitionHandler(partitionHandler())
                .listener(new JobCompletionListener())
                .build();
    }


    @Bean
    @JobScope
    public Step masterSecondStep() throws Exception {
        // Retrieve chunk file paths
        Map< Integer, String> chunkFilePaths = new HashMap<>();
        List <ChunkFilePath>  chunksList = chunkFilePathRepository.findAll();
        for(ChunkFilePath chunkFilePath : chunksList){
            chunkFilePaths.put(chunkFilePath.getChunkId(), chunkFilePath.getFilePath());
        }
        return new StepBuilder("masterSecondStep", jobRepository)
                .partitioner("slaveSecondStep", new XMLSecondPartitioner(chunkFilePaths))
                .partitionHandler(partitionMasterHandler())
                .build();
    }





    //    @Bean
//    @StepScope
//    public ItemReader<Vuc> itemReader(@Value("#{stepExecutionContext['chunkData']}") List<String> chunkData) throws JAXBException {
//        return new XmlFileItemReader(chunkData);
//    }
    @Bean
    @StepScope
    public
    ItemReader<Vuc> itemReader(@Value("#{stepExecutionContext['chunkFilePath']}") String chunkFilePath) throws JAXBException {
        return new XmlFileItemReader(chunkFilePath , stopCondition);
    }
    @Bean
    @StepScope
    public
    ItemReader<Vuc> itemSecondReader(@Value("#{stepExecutionContext['chunkFilePath']}") String chunkFilePath) throws JAXBException {
        return new XmlFileItemSecondReader(chunkFilePath);
    }
    @Bean
    public
    PartitionHandler partitionHandler() throws Exception {
        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();
        partitionHandler.setTaskExecutor(taskExecutor());
        partitionHandler.setGridSize(1667);
        partitionHandler.setStep(slaveStep());
        partitionHandler.afterPropertiesSet();
        return partitionHandler;
    }

    @Bean
    public
    PartitionHandler partitionMasterHandler() throws Exception {
        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();
        partitionHandler.setTaskExecutor(taskExecutor());
        partitionHandler.setGridSize(1667);
        partitionHandler.setStep(slaveSecondStep());
        partitionHandler.afterPropertiesSet();
        return partitionHandler;
    }


    @Bean
    public
    Step slaveStep() throws Exception {
        return new StepBuilder("slaveStep", jobRepository)
                .<Vuc, XmlFileItemProcessor.ValidationResult>chunk(1, transactionManager)
                .reader(itemReader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }

    /**
     * @return
     * @throws Exception
     */
    @Bean
    public
    Step slaveSecondStep() throws Exception {
        return new StepBuilder("slaveStep", jobRepository)
                .<Vuc, XmlFileItemProcessor.ValidationResult>chunk(1, transactionManager)
                .reader(itemSecondReader(null))
                .processor(processorSecond())
                .writer(writerSecond())
                .build();
    }

    @Bean
    public
    ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(21);
        executor.setThreadNamePrefix("Batch-");
        return executor;
    }

    @Bean
    public
    XmlFileItemProcessor processor() {
        return new XmlFileItemProcessor(stopCondition, counter);
    }

    @Bean
    public
    XmlFileItemSecondProcessor processorSecond() {
        return new XmlFileItemSecondProcessor();
    }


    @Bean
    public
    ValidationResultWriter writer() {
        return new ValidationResultWriter(stopCondition);
    }


    @Bean
    public
    SecondResultWriter writerSecond() {
        return new SecondResultWriter();
    }


}
