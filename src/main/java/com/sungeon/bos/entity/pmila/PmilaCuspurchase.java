package com.sungeon.bos.entity.pmila;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.annotation.RestColumn;
import com.burgeon.framework.restapi.annotation.RestOneToMany;
import com.burgeon.framework.restapi.annotation.RestTable;
import com.burgeon.framework.restapi.model.BaseRestBean;
import com.burgeon.framework.restapi.parser.value.LongParser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @BelongsPackage: com.sungeon.bos.entity.pmila
 * @ClassName: PmilaCuspurchase
 * @Author: 陈苏洲
 * @Description: 经销商采购
 * @CreateTime: 2023-08-31 10:33
 * @Version: 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@RestTable(tableName = "M_V_CUSPURCHASE", description = "经销商采购单",
		   defaultQueryFilter = "M_V_CUSPURCHASE.ISACTIVE = 'Y'")

public class PmilaCuspurchase extends BaseRestBean {
	@RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
	private Long id;
	@RestColumn(name = "DOCNO")
	private String docNo;
	@RestColumn(name = "DOCTYPE")
	private String docType;
	@RestColumn(name = "C_STORE_ID")
	private String storeId;
	@RestColumn(name = "BILLDATE")
	private Integer billDate;
	@RestColumn(name = "DATEOUT")
	private Integer outDate;
	@RestColumn(name = "DATEIN")
	private Integer inDate;
	@RestColumn(name = "C_STORE_ID;CODE")
	private String origCode;
	@RestColumn(name = "C_STORE_ID;NAME")
	private String origName;
	@RestColumn(name = "C_DEST_ID;CODE")
	private String destCode;
	@RestColumn(name = "C_DEST_ID;NAME")
	private String destName;
	@RestColumn(name = "DESCRIPTION")
	private String description;
	@RestColumn(name = "STATUS")
	private String status;
	@RestColumn(name = "OUT_STATUS")
	private String outStatus;
	@RestColumn(name = "IN_STATUS")
	private String inStatus;
	@RestOneToMany(fkParentColumnName = "id", fkChildColumnName = "saleId", childBeanClass = PmilaCuspurchaseItem.class)
	private List<PmilaCuspurchaseItem> items;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
