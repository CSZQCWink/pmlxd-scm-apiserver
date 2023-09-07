package com.sungeon.bos.entity.pmila;

import com.burgeon.framework.restapi.annotation.RestColumn;
import com.burgeon.framework.restapi.annotation.RestTable;
import com.burgeon.framework.restapi.model.BaseRestBean;
import com.burgeon.framework.restapi.parser.value.LongParser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @BelongsPackage: com.sungeon.bos.entity.pmila
 * @ClassName: PmilaSupplier
 * @Author: 陈苏洲
 * @Description: 帕米拉供应商
 * @CreateTime: 2023-08-30 13:57
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@RestTable(tableName = "C_SUPPLIER", description = "供应商", defaultQueryFilter = "C_SUPPLIER.ISACTIVE = 'Y'")
public class PmilaSupplier extends BaseRestBean {

	@RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
	private Long id;

	@RestColumn(name = "SUPTYPE")
	private String supplierType;

	@RestColumn(name = "DESCRIPTION")
	private String description;

	@RestColumn(name = "C_SUPPLIERTYPE_ID")
	private String supplierTypeId;

	@RestColumn(name = "CODE")
	private String supplierCode;

	@RestColumn(name = "NAME")
	private String supplierName;

	@RestColumn(name = "FEEREMAIN")
	private Double supplierFeerPrice;

	@RestColumn(name = "ACCOUNT")
	private String supplierAccount;

	@RestColumn(name = "EMAIL")
	private String supplierEmail;

	@RestColumn(name = "MOBIL")
	private String supplierPhone;
}
