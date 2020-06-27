package com.purnima.jain.springbatch.job.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.purnima.jain.springbatch.job.processor.DataProcessor;
import com.purnima.jain.springbatch.job.reader.SourceMySqlTableReader;
import com.purnima.jain.springbatch.job.tasklet.CleanupDestinationTableTasklet;
import com.purnima.jain.springbatch.job.writer.DestinationPostgresTableWriter;
import com.purnima.jain.springbatch.mysql.entity.SourceMySqlEntity;
import com.purnima.jain.springbatch.mysql.repo.MySqlSourceRepository;
import com.purnima.jain.springbatch.postgres.entity.DestinationPostgresEntity;
import com.purnima.jain.springbatch.postgres.repo.PostgresDestinationRepository;

@Configuration
@EnableBatchProcessing
public class ExportDataJobConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ExportDataJobConfig.class);
	
	@Value("${chunk_size: 5}")
	private Integer chunkSize;
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private MySqlSourceRepository mySqlSourceRepository;
	
	@Autowired
	private PostgresDestinationRepository postgresDestinationRepository;
	
	@Autowired
	private DataProcessor dataProcessor;
	
	@Autowired
	private DestinationPostgresTableWriter destinationPostgresTableWriter;
	
//	@Bean(destroyMethod = "")
//	@StepScope
//	public JdbcCursorItemReader<SourceMySqlEntity> sourceMySqlTableReader(@Value("#{jobParameters['idListStr']}") String idListStr) {
//		logger.info("Entering ExportDataJobConfig.sourceMySqlTableReader() with idListStr: {}", idListStr);
//		SourceMySqlTableReader sourceMySqlTableReader = new SourceMySqlTableReader(mySqlSourceRepository, idListStr);
//		return sourceMySqlTableReader.getSourceMySqlTableReader();
//	}
	
	@Bean(destroyMethod = "")
	@StepScope
	public RepositoryItemReader<SourceMySqlEntity> sourceMySqlTableReader(@Value("#{jobParameters['idListStr']}") String idListStr) {
		logger.info("Entering ExportDataJobConfig.sourceMySqlTableReader() with idListStr: {}", idListStr);
		SourceMySqlTableReader sourceMySqlTableReader = new SourceMySqlTableReader(mySqlSourceRepository, idListStr);
		logger.info("Leaving ExportDataJobConfig.sourceMySqlTableReader()........................................................................");
		return sourceMySqlTableReader.getSourceMySqlTableReader();
	}
	
	@Bean
	public Step cleanupDestinationTable() {
		logger.info("Entering ExportDataJobConfig.cleanupDestinationTable()......................................................................");
		return stepBuilderFactory.get("cleanupDestinationTableTasklet")
				.tasklet(new CleanupDestinationTableTasklet(postgresDestinationRepository))				
				.build();
	}
	
	@Bean
	public Step exportData() throws Exception {
		logger.info("Entering ExportDataJobConfig.exportData()....................................................................................");
		return stepBuilderFactory.get("exportDataStep")
				.<SourceMySqlEntity, DestinationPostgresEntity> chunk(chunkSize)
				.reader(sourceMySqlTableReader(null))
				.processor(dataProcessor)
				.writer(destinationPostgresTableWriter)
				.build();
	}
	
	@Bean(name = "exportDataJob")
	public Job exportDataJob() throws Exception {
		logger.info("Entering ExportDataJobConfig.exportDataJob()................................................................................");
		return jobBuilderFactory.get("exportDataJobChild")
				.start(cleanupDestinationTable())
				.next(exportData())				
				.build();
	}

}
