package com.wevioo.fgdb.extract.batch.processor;

import com.wevioo.fgdb.extract.batch.configuration.DynamicValidationService;
import generated.Vuc;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class XmlFileItemSecondProcessor implements ItemProcessor<Vuc, XmlFileItemProcessor.ValidationResult> {


    @Autowired
    private DynamicValidationService dynamicValidationService;

    @Override
    public
    XmlFileItemProcessor.ValidationResult process(Vuc vuc) throws Exception {
        String res = dynamicValidationService.validate(vuc.getDepositors().getDepositor());
        System.out.println(res);
        return  null;
    }
}
