package com.wevioo.fgdb.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
//@EnableDiscoveryClient
@SpringBootApplication
public class FgdbConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(FgdbConfigApplication.class, args);
	}

}
