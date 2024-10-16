package com.wevioo.fgdb.referenciel.repository;


import com.wevioo.fgdb.referenciel.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * A repository interface for managing Country entities using JpaRepository operations.
 */

public interface CountryRepository extends JpaRepository<Country, String > {

    /**
     * Find countries by their IDs.
     *
     * @param ids A list of country IDs to search for.
     * @return A list of countries matching the provided IDs.
     */
    List<Country> findByIdIn(List<String> ids);
    /**
     * Check if a country with a specific ID exists.
     *
     * @param id The ID of the country to check for existence.
     * @return true if a country with the specified ID exists; otherwise, false.
     */
    boolean existsById(String id);
    /**
     * Find countries and order them by label in ascending order after trimming leading and trailing spaces.
     *
     * @return A list of countries ordered by label in ascending order with leading and trailing spaces trimmed.
     */
    @Query("SELECT o.id as id , o.code as code , o.code2 as code2 , o.label as label , o.nationality as nationality  FROM Country o ORDER BY LTRIM(RTRIM(o.label)) asc")
    List<CountryProjection> findByOrderByLabelAscFR();

    /**
     * Find countries and order them by label in ascending order after trimming leading and trailing spaces.
     *
     * @return A list of countries ordered by label in ascending order with leading and trailing spaces trimmed.
     */
    @Query("SELECT o.id as id , o.code as code , o.code2 as code2 , o.labelEn as label , o.nationalityEn as nationality FROM Country o ORDER BY LTRIM(RTRIM(o.label)) asc")
    List<CountryProjection> findByOrderByLabelAscEN();

    /**
     * Find countries and order them by label in ascending order after trimming leading and trailing spaces.
     *
     * @return A list of countries ordered by label in ascending order with leading and trailing spaces trimmed.
     */
    @Query("SELECT o FROM Country o ORDER BY LTRIM(RTRIM(o.label)) asc")
    List<Country> findByOrderByLabelAsc();

    Country findByCode2(String code2);
}
