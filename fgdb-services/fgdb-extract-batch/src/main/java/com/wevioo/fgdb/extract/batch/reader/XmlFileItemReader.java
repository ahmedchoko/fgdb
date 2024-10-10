package com.wevioo.fgdb.extract.batch.reader;
import generated.Vuc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.item.ItemReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class XmlFileItemReader implements ItemReader<Vuc> {

    private final String chunkFilePath;
    private final Unmarshaller unmarshaller;
    private static final JAXBContext context;
    private static final Logger logger = LoggerFactory.getLogger(XmlFileItemReader.class);
    private boolean processed = false;
    private final AtomicBoolean stopCondition;
    static {
        try {
            context = JAXBContext.newInstance(Vuc.class);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public XmlFileItemReader(String chunkFilePath ,AtomicBoolean stopCondition) throws JAXBException {
        this.chunkFilePath = chunkFilePath;
        this.unmarshaller = context.createUnmarshaller();
        this.stopCondition = stopCondition;
        logger.info("XmlFileItemReader initialized with chunk file: {}", chunkFilePath);
    }

    @Override
    public Vuc read() throws JAXBException, JobInterruptedException {
        if (stopCondition.get()) {
            throw new JobInterruptedException("Job stopped due to the stop condition being met in read ");
        }
        if (!processed) {
            File chunkFile = new File(chunkFilePath);
            if (chunkFile.exists() && chunkFile.isFile()) {
                try (InputStream inputStream = new FileInputStream(chunkFile)) {
                    logger.info("Thread: {} is processing chunk file: {}", Thread.currentThread().getName(), chunkFilePath);
                    Vuc vuc = (Vuc) unmarshaller.unmarshal(inputStream);

                    /////////////// Example logic for checking phone number
                    boolean exists = vuc.getDepositors().getDepositor().stream()
                            .anyMatch(depositor -> depositor.getContactDetails().getPhoneNumber1().equals("55"));
                    if (exists) {
                        logger.info("Phone number '55' exists in the Vuc.");
                    }

                    /////////// Mark as processed after reading the file
                    processed = true;

                    //////////// Delete the file after processing
                    if (chunkFile.delete()) {
                        logger.info("Chunk file {} deleted successfully.", chunkFilePath);
                    } else {
                        logger.warn("Failed to delete chunk file: {}", chunkFilePath);
                    }

                    return vuc;
                } catch (Exception e) {
                    logger.error("Error processing chunk file: {}", chunkFilePath, e);
                    throw new RuntimeException(e);
                }
            } else {
                logger.warn("Chunk file does not exist: {}", chunkFilePath);
                return null;
            }
        } else {
            return null;  //////////////// Return null when no more data to process
        }
    }
}
