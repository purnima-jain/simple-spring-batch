package com.purnima.jain.springbatch.job.tasklet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.purnima.jain.springbatch.postgres.repo.PostgresDestinationRepository;

public class CleanupDestinationTableTasklet implements Tasklet {

	private static final Logger logger = LogManager.getLogger(CleanupDestinationTableTasklet.class);

	private PostgresDestinationRepository postgresDestinationRepository;

	public CleanupDestinationTableTasklet(PostgresDestinationRepository postgresDestinationRepository) {
		this.postgresDestinationRepository = postgresDestinationRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		logger.info("Entering CleanupDestinationTableTasklet.execute()....................................................................................");
		postgresDestinationRepository.deleteAll();
		logger.info("Leaving CleanupDestinationTableTasklet.execute()....................................................................................");
		return RepeatStatus.FINISHED;
	}

}
