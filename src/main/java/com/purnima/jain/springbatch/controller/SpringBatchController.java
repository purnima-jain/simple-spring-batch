package com.purnima.jain.springbatch.controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.purnima.jain.springbatch.json.ExportDataRequestDto;
import com.purnima.jain.springbatch.json.ExportDataResponseDto;

@RestController
public class SpringBatchController {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringBatchController.class);
	
	private static AtomicBoolean isJobInProgress = new AtomicBoolean(Boolean.FALSE);
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("exportDataJob")
	private Job exportDataJob;
	
	@PostMapping(value = "batchjob/exportData")
	public ExportDataResponseDto exportData(@RequestBody ExportDataRequestDto exportDataRequestDto) throws Exception {
		logger.info("Entering SpringBatchController.exportData() with exportDataRequestDto :: {}", exportDataRequestDto.getIds());
		
		ExportDataResponseDto exportDataResponseDto = new ExportDataResponseDto();
		
		if (Boolean.TRUE.equals(isJobInProgress.get()))
			exportDataResponseDto.setMessage("Another Batch Job In-Progress. Please try later.");
		else {
			JobExecution jobExecution = executeDataExportJob(exportDataRequestDto.getIds());		
			logger.info("Status of the JobExecution :: {}", jobExecution.getStatus());
			if(BatchStatus.COMPLETED.equals(jobExecution.getStatus()))
				exportDataResponseDto.setMessage(String.format("Batch job has finished migrating given customerIds: %s", exportDataRequestDto.getIds().toString()));
			else
				exportDataResponseDto.setMessage(String.format("Batch job could not finish migrating given customerIds: %s", exportDataRequestDto.getIds().toString()));
		}
		
		logger.info("Leaving SpringBatchController.exportData() with exportDataResponseDto:: {}", exportDataResponseDto);		
		return exportDataResponseDto;		
	}
	
	private JobExecution executeDataExportJob(List<Integer> idList) 
			throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("idListStr", idList.toString())
				.addString("time", ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
				.toJobParameters();
		JobExecution jobExecution = executeJob(jobParameters);
		return jobExecution;
	}
	
	private synchronized JobExecution executeJob(final JobParameters jobParameters)
			throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		isJobInProgress.compareAndSet(Boolean.FALSE, Boolean.TRUE);
		JobExecution jobExecution = jobLauncher.run(exportDataJob, jobParameters);
		isJobInProgress.compareAndSet(Boolean.TRUE, Boolean.FALSE);
		return jobExecution;
	}
}
