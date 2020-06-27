package com.purnima.jain.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringBatchApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
		logger.info("SpringBatchApplication Started........");
	}

}
