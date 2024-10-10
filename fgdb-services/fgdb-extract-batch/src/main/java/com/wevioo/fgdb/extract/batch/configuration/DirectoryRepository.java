package com.wevioo.fgdb.extract.batch.configuration;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long> {

    List<Directory> findByStatus(DirectoryStatus status);
}

