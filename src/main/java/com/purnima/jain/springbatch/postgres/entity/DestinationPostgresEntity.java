package com.purnima.jain.springbatch.postgres.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CUSTOMER_POSTGRES")
@Data
public class DestinationPostgresEntity {
	
	@Id
	@Column(name = "CUSTOMER_ID")
	private Integer customerId;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "LAST_NAME")
	private String lastName;

}
