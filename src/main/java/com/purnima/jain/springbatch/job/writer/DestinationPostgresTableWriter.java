package com.purnima.jain.springbatch.job.writer;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.purnima.jain.springbatch.postgres.entity.DestinationPostgresEntity;
import com.purnima.jain.springbatch.postgres.repo.PostgresDestinationRepository;

@Component
public class DestinationPostgresTableWriter implements ItemWriter<DestinationPostgresEntity> {

	private static final Logger logger = LogManager.getLogger(DestinationPostgresTableWriter.class);

	@Autowired
	private PostgresDestinationRepository postgresDestinationRepository;

	@Override
	public void write(List<? extends DestinationPostgresEntity> destinationPostgresEntityList) throws Exception {
		logger.info("Entering DestinationPostgresTableWriter.write() with destinationPostgresEntityList.size() = {}", destinationPostgresEntityList.size());
		for (DestinationPostgresEntity destinationPostgresEntity : destinationPostgresEntityList) {
			postgresDestinationRepository.save(destinationPostgresEntity);
		}
		logger.info("Leaving DestinationPostgresTableWriter.write()............................");
	}
}