package com.wevioo.fgdb.extract.batch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ValidationRuleService {
    @Autowired
    private ValidationRuleRepository validationRuleRepository;


    @Cacheable("rules")
    public List<ValidationRule> getValidationRules(String entityName) {
        log.info("Fetching rules from the database...");
        return validationRuleRepository.findByEntityName(entityName);
    }
}
