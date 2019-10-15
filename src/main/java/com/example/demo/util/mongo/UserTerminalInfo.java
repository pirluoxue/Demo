package com.example.demo.util.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
@Data
public class UserTerminalInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mac;
	private Long count;
	private Integer buildingId;
	private Integer day;
	private Integer utcCode;
	private String osType;
	private String manufactureEn;
	private String manufactureCh;
	private String macPrefix;
	private String isZeroData;
	
}
	
