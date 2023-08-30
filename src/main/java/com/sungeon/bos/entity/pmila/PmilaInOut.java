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
@RestTable(tableName = "M_OTHER_INOUT", description = "物理调整单", defaultQueryFilter = "M_OTHER_INOUT.ISACTIVE = 'Y'", isNeedSubmit = true)
public class PmilaInOut extends BaseRestBean {

	@RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
	private Long id;
	@RestColumn(name = "DOCNO")
	private String docNo;
	@RestColumn(name = "BILLDATE", isRestSave = true)
	private Integer billDate;
	@RestColumn(name = "C_STORE_ID__NAME", isRestSave = true)
	private String storeName;
	@RestColumn(name = "C_OTHER_INOUTTYPE_ID__NAME", isRestSave = true, isRestQuery = false)
	private String transferType = "正常调整";
	@RestColumn(name = "SOURCENO", isRestSave = true)
	private String sourceNo;
	@RestColumn(name = "DESCRIPTION", isRestSave = true)
	private String description;
	@RestOneToMany(fkParentColumnName = "id", fkChildColumnName = "otherInOutId", childBeanClass = PmilaInOutItem.class)
	private List<PmilaInOutItem> items;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
