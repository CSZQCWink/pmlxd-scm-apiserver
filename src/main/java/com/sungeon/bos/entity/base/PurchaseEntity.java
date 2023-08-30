package com.sungeon.bos.entity.base;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Alias("Purchase")
@Data
public class PurchaseEntity {

	private Long id;
	private String docNo;
	private Integer billDate;
	private String poDocno;
	private String customerCode;
	private String sourceNo;
	private String docType;
	private String supplierCode;
	private String storeCode;
	private Double tax = 0.17;
	private Boolean isAutoIn = false;
	private Integer inDate;
	private String inTime;
	private String description;
	private List<ItemEntity> items;

	private Long poId;
	private Long supplierId;
	private Long storeId;

}
