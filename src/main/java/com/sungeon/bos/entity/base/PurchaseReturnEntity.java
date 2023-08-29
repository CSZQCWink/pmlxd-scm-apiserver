package com.sungeon.bos.entity.base;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Alias("PurchaseReturn")
@Data
public class PurchaseReturnEntity {

    private Long id;
    private String docNo;
    private Integer billDate;
    private String sourceNo;
    private String docType;
    private String storeCode;
    private String storeName;
    private String customerCode;
    private String supplierCode;
    private String supplierName;
    private Double tax = 0.17;
    private Boolean isAutoOut;
    private Integer status;
    private Integer outStatus;
    private Integer outDate;
    private String outTime;
    private String purchaseReturnOrderNo;
    private String description;
    private List<ItemEntity> items;

    private Long supplierId;
    private Long storeId;
    private Long purchaseReturnOrderId;

}
