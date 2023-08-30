package com.sungeon.bos.entity.pmila;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.annotation.RestColumn;
import com.burgeon.framework.restapi.annotation.RestTable;
import com.burgeon.framework.restapi.model.BaseRestBean;
import com.burgeon.framework.restapi.parser.value.DoubleParser;
import com.burgeon.framework.restapi.parser.value.LongParser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 陈苏洲
 * @date 2023-8-29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@RestTable(tableName = "M_RETAILITEM", refTableName = "M_RETAILITEM", description = "零售单明细",
		defaultQueryFilter = "M_RETAILITEM.ISACTIVE = 'Y'")
public class PmilaRetailItem extends BaseRestBean {

	@RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
	private Long id;
	@RestColumn(name = "M_RETAIL_ID", valuePraser = LongParser.class, isRestSave = true)
	private Long retailId;
	@RestColumn(name = "M_PRODUCTALIAS_ID;NO")
	private String sku;
	@RestColumn(name = "M_PRODUCT_ID;NAME")
	private String product;
	@RestColumn(name = "M_PRODUCT_ID__NAME", isRestSave = true, isRestQuery = false)
	private String productAdd;
	@RestColumn(name = "M_ATTRIBUTESETINSTANCE_ID", valuePraser = LongParser.class, isRestSave = true)
	private Long asiId;
	@RestColumn(name = "QTY", isRestSave = true)
	private Integer qty;
	@RestColumn(name = "PRICEACTUAL", valuePraser = DoubleParser.class, isRestSave = true)
	private Double priceActual;
	@RestColumn(name = "TOT_AMT_ACTUAL", valuePraser = DoubleParser.class)
	private Double amtActual;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}


}
