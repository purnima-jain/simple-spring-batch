package com.purnima.jain.springbatch.job.reader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.purnima.jain.springbatch.job.config.ExportDataJobConfig;
import com.purnima.jain.springbatch.mysql.entity.SourceMySqlEntity;
import com.purnima.jain.springbatch.mysql.repo.MySqlSourceRepository;

public class SourceMySqlTableReader {
	
	private static final Logger logger = LoggerFactory.getLogger(ExportDataJobConfig.class);
	
	private MySqlSourceRepository mySqlSourceRepository;
	
	private String idListStr;
	
	public SourceMySqlTableReader(MySqlSourceRepository mySqlSourceRepository, String idListStr) {
		this.mySqlSourceRepository = mySqlSourceRepository;
		this.idListStr = idListStr;		
	}
	
	public RepositoryItemReader<SourceMySqlEntity> getSourceMySqlTableReader() {
		logger.info("Entering SourceMySqlTableReader.getSourceMySqlTableReader()....................................................................................");

//		idListStrWithoutBrackets = idListStr.substring(1, idListStr.length()-1);
//      String sqlQuery = "SELECT * FROM customer_mysql WHERE customer_id IN (" + idListStrWithoutBrackets + ")";
//		logger.info("sqlQuery in getSourceMySqlTableReader() :: " + sqlQuery);

//		JdbcCursorItemReader<SourceMySqlEntity> reader = new JdbcCursorItemReader<>();
//
//		reader.setSql(sqlQuery);
//		reader.setDataSource(this.postgresDataSource);
//		reader.setRowMapper(new MySqlEntityToPostgresEntityRowMapper());
		
		RepositoryItemReader<SourceMySqlEntity> reader = new RepositoryItemReader<>();		
		final Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("customer_id", Direction.ASC);
        reader.setSort(sorts);
        reader.setRepository(mySqlSourceRepository);
		
		String idListStrWithoutBrackets = idListStr.substring(1, idListStr.length()-1);
		
		if(idListStrWithoutBrackets != null && idListStrWithoutBrackets.length() > 0) {
			List<Integer> idList = Stream.of(idListStrWithoutBrackets.split(", ")).map(Integer::parseInt).collect(Collectors.toList());			
			reader.setMethodName("findAllByCustomerId");
			reader.setArguments(List.of(idList));
		}
		else {
			reader.setMethodName("findAllCustomers");
		}		

		logger.info("Leaving SourceMySqlTableReader.getSourceMySqlTableReader()....................................................................................");
		return reader;
	}

}
