package com.purnima.jain.springbatch.json;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ExportDataRequestDto {
	
	List<Integer> ids = new ArrayList<>();
	

}
