package com.wevioo.fgdb.extract.batch.writer;

import com.wevioo.fgdb.extract.batch.processor.XmlFileItemProcessor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class SecondResultWriter implements ItemWriter<XmlFileItemProcessor.ValidationResult> {


    @Override
    public void write(Chunk<? extends XmlFileItemProcessor.ValidationResult> chunk) throws Exception {

       System.out.println("second writer");
    }

}
