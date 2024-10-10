package com.wevioo.fgdb.extract.batch.writer;


import com.wevioo.fgdb.common.constants.ApplicationConstants;
import com.wevioo.fgdb.common.exception.ConflictException;
import com.wevioo.fgdb.extract.batch.processor.XmlFileItemProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ValidationResultWriter implements ItemWriter<XmlFileItemProcessor.ValidationResult> {

    private static final String FILE_PATH = "/app/fgbd/output.txt";
    private final AtomicBoolean stopCondition;


    public ValidationResultWriter(AtomicBoolean stopCondition) {
        this.stopCondition = stopCondition;
        log.info("init Validation Result Writer ");
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new ConflictException(ApplicationConstants.ERROR_CREATE_FILE_FOR_LOG_RESULT , ApplicationConstants.ERROR_CREATE_FILE_FOR_LOG_RESULT );
        }

    }

    @Override
    public void write(Chunk<? extends XmlFileItemProcessor.ValidationResult> chunk) throws Exception {
        if (stopCondition.get()) {
            throw new JobInterruptedException("Job stopped due to the stop condition being met. write");
        }
        log.info(" write Thread: {}", Thread.currentThread().getName());
        StringBuilder stringBuilder = new StringBuilder();
        for (XmlFileItemProcessor.ValidationResult item : chunk.getItems()) {
            if (item.getValidationErrors() != null) {
                for (XmlFileItemProcessor.ValidationError error : item.getValidationErrors()) {
                    stringBuilder.append(error.getPropertyPath())
                            .append(error.getInvalidValue())
                            .append(error.getMessage())
                            .append(error.getErrorCode())
                            .append(System.lineSeparator());
                    stringBuilder.append("propertyPath = ")
                            .append(error.getPropertyPath())
                            .append(", invalidValue = ")
                            .append(error.getInvalidValue())
                            .append(", message = ")
                            .append(error.getMessage())
                            .append(", code = ")
                            .append(error.getErrorCode())
                            .append(System.lineSeparator())
                            .append("*************************")
                            .append(System.lineSeparator());
                }
                writeDepositorLogResult(stringBuilder, item.getValidationErrorsDepositors());
            }


        }
        stringBuilder.append("*************************************************");


        Files.write(Paths.get(FILE_PATH), stringBuilder.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

    }


    private   void writeDepositorLogResult(StringBuilder stringBuilder ,  HashMap<String ,  List<XmlFileItemProcessor.ValidationError> >  validationErrorsDepositors ) {

        if(validationErrorsDepositors != null && !validationErrorsDepositors.isEmpty()) {
            for (Map.Entry<String, List<XmlFileItemProcessor.ValidationError>> entry : validationErrorsDepositors.entrySet()) {
                String  depositorId = entry.getKey();
                List<XmlFileItemProcessor.ValidationError> errors = entry.getValue();
                if(errors.isEmpty()){
                    stringBuilder.append("DepositorId: ").append(depositorId).append(" : Valid Data ").append(System.lineSeparator());
                }
                else{
                    stringBuilder.append("DepositorId: ").append(depositorId).append(System.lineSeparator());
                }
                // Loop through the list of ValidationError objects
                for (XmlFileItemProcessor.ValidationError error : errors) {
                    stringBuilder.append(" - Property path: ").append(error.getPropertyPath())
                            .append("  -,Invalid Value: ").append(error.getInvalidValue())
                            .append("  -, Error Message: ").append(error.getMessage())
                            .append("  -, Error Code: ").append(error.getErrorCode())
                            .append(System.lineSeparator());

                }
            }
        }

    }
}
