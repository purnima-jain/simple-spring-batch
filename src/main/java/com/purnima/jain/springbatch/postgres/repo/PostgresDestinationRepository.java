package com.purnima.jain.springbatch.postgres.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.purnima.jain.springbatch.postgres.entity.DestinationPostgresEntity;

@Repository
public interface PostgresDestinationRepository extends JpaRepository<DestinationPostgresEntity, Integer> {
}
