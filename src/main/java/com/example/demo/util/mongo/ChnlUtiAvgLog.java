package com.example.demo.util.mongo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChnlUtiAvgLog implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String sn;
	private Integer buildingId;
	private Integer radioIndex;
	private Integer avgUtilization;
	private Integer tenantId;
	
}
