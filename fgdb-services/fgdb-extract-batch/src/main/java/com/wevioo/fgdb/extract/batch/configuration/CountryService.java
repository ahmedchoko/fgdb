package com.wevioo.fgdb.extract.batch.configuration;


import com.wevioo.fgdb.extract.batch.feign.CountryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CountryService {

    @Autowired
    private ErrorConfigurationRepository errorConfigurationRepository;

    @Cacheable("errors")
    public
    Map<String, String> getErrorsAsMap() {
        List<ErrorConfiguration> errors = errorConfigurationRepository.findAll();
        return errors.stream().collect(Collectors.toMap(ErrorConfiguration::getMessage, ErrorConfiguration::getErrorCode));
    }

}
