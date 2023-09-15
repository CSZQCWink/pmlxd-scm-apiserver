package com.sungeon.bos.entity.base;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Alias("Purchase")
@Data
public class PurchaseEntity  {

	private Long id;
	private String docNo;
	private String docType;
	private Integer billDate;
	private Long poId;
	// 采购店仓
	private Long storeId;
	private String storeCode;
	private String storeName;
	private Long customerId;
	private String customerCode;
	private String customerName;
	private Long supplierId;
	private String supplierCode;
	private String supplierName;
	private Double tax = 0.17;
	private Boolean isAutoIn = false;
	private Integer outDate;
	private Integer inDate;
	private String description;
	private String inStatus;
	private String status;
	private List<ItemEntity> items;
	private List<SkuEntity> skuEntities;

//	private String saleType;
//	private String poDocno;
//	private String sourceNo;
//	private String outStatus;
//	private String inTime;

}
