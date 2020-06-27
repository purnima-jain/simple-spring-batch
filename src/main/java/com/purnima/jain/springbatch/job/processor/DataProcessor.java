package com.purnima.jain.springbatch.job.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.purnima.jain.springbatch.mysql.entity.SourceMySqlEntity;
import com.purnima.jain.springbatch.postgres.entity.DestinationPostgresEntity;

@Component
public class DataProcessor implements ItemProcessor<SourceMySqlEntity, DestinationPostgresEntity> {

	private static final Logger logger = LogManager.getLogger(DataProcessor.class);

	@Override
	public DestinationPostgresEntity process(SourceMySqlEntity sourceMySqlEntity) throws Exception {
		logger.info("Entering DataProcessor.process() with sourceMySqlEntity = {}", sourceMySqlEntity);
		
		DestinationPostgresEntity destinationPostgresEntity = new DestinationPostgresEntity();
		destinationPostgresEntity.setCustomerId(sourceMySqlEntity.getCustomerId());
		destinationPostgresEntity.setFirstName(sourceMySqlEntity.getFirstName());
		destinationPostgresEntity.setLastName(sourceMySqlEntity.getLastName());
		
		logger.info("Leaving DataProcessor.process() with destinationPostgresEntity = {}", destinationPostgresEntity);		
		return destinationPostgresEntity;
	}

}