package com.wevioo.fgdb.extract.batch.configuration;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface  ErrorConfigurationRepository extends JpaRepository<ErrorConfiguration, Long> {
}
