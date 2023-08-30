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
 * @author 陈苏洲
 * @date 2023-8-29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@RestTable(tableName = "M_SALE", description = "销售单", defaultQueryFilter = "M_SALE.ISACTIVE = 'Y'")
public class PmilaSale extends BaseRestBean {

	@RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
	private Long id;
	@RestColumn(name = "DOCNO")
	private String docNo;
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
	@RestColumn(name = "CONFIRM_STATUS")
	private String confirmStatus;
	@RestColumn(name = "OUT_STATUS")
	private String outStatus;
	@RestColumn(name = "IN_STATUS")
	private String inStatus;
	@RestOneToMany(fkParentColumnName = "id", fkChildColumnName = "saleId", childBeanClass = PmilaSaleItem.class)
	private List<PmilaSaleItem> items;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}


}
