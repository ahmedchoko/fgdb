package com.wevioo.fgdb.referenciel.service;


import com.wevioo.fgdb.referenciel.domain.Country;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ReferentialService {

    /**
     * Load all countries
     *
     * @return list of CountryDto
     */
    List<?> getAllCountries();
    /**
     * Retrieves a list of countries by their IDs.
     *
     * @param ids The list of country IDs to retrieve
     * @return List of Country objects corresponding to the provided IDs
     */
    List<Country> findByIdIn(List<String> ids);

}
