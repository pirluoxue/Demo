package com.example.demo.util.mongo.analysis.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HourFlowLogByBuilding implements Serializable {

	private static final long serialVersionUID = 1L;

	private String intfType;
	private Integer buildingId;
	private Long rxBytes;
	private Long rxPkts;
	private Long txBytes;
	private Long txPkts;
	private Long aggregatedTime;
	private Long rxTxBytes;
	private String productType;
	private Date updateTime;
	private int utcCode;
	private String sn;

}

