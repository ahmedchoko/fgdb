package com.wevioo.fgdb.extract.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
public  class FgdbExtractBatchApplication {

    public static
    void main(String[] args) {
        SpringApplication.run(FgdbExtractBatchApplication.class, args);
    }

}
