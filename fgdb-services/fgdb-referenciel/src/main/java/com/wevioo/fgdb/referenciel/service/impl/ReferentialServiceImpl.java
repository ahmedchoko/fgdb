package com.wevioo.fgdb.referenciel.service.impl;

import com.wevioo.fgdb.referenciel.domain.Country;
import com.wevioo.fgdb.referenciel.repository.CountryRepository;
import com.wevioo.fgdb.referenciel.service.ReferentialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ReferentialServiceImpl implements ReferentialService {


    /**
     * Injected bean {@link CountryRepository}
     */
    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<?> getAllCountries() {
      //  log.info("get All Countries");
        List<?> countryProjection;
        // return countries in case language is provided : FR
              countryProjection = countryRepository.findByOrderByLabelAscFR();
              return countryProjection;
        }


    @Override
    public List<Country> findByIdIn(List<String> ids) {
        return countryRepository.findByIdIn(ids);
    }

}
