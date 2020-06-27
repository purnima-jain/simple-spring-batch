package com.purnima.jain.springbatch.mysql.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.purnima.jain.springbatch.mysql.entity.SourceMySqlEntity;

@Repository
public interface MySqlSourceRepository extends JpaRepository<SourceMySqlEntity, Integer> {

	@Query(value = "SELECT * FROM customer_mysql WHERE customer_id IN (:idList) ", nativeQuery = true)
	public Page<List<SourceMySqlEntity>> findAllByCustomerId(@Param("idList") List<Integer> idList, Pageable pageable);
	
	@Query(value = "SELECT * FROM customer_mysql", nativeQuery = true)
	public Page<List<SourceMySqlEntity>> findAllCustomers(Pageable pageable);
	
}
