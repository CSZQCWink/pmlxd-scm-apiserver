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
 * @BelongsPackage: com.sungeon.bos.entity.pmila
 * @ClassName: PmilaCuspurchaseReturnItem
 * @Author: 陈苏洲
 * @Description: 经销商采购单明细
 * @CreateTime: 2023-09-01 10:06
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@RestTable(tableName = "M_V_RET_CUSPURITEM", description = "经销商采购退货单明细",
		defaultQueryFilter = "M_V_RET_CUSPURITEM.ISACTIVE = 'Y'")
public class PmilaCuspurchaseReturnItem extends BaseRestBean {
	@RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
	private Long id;
	@RestColumn(name = "M_RET_SALE_ID", valuePraser = LongParser.class)
	private Long retSaleId;
	@RestColumn(name = "M_PRODUCTALIAS_ID;NO")
	private String sku;
	@RestColumn(name = "M_PRODUCT_ID;NAME")
	private String product;
	@RestColumn(name = "M_PRODUCT_ID__NAME", isRestSave = true, isRestQuery = false)
	private String productAdd;
	@RestColumn(name = "M_ATTRIBUTESETINSTANCE_ID", valuePraser = LongParser.class)
	private Long asiId;
	@RestColumn(name = "QTY", isRestSave = true)
	private Integer qty;
	@RestColumn(name = "QTYOUT")
	private Integer qtyOut;
	@RestColumn(name = "QTYIN")
	private Integer qtyIn;
	@RestColumn(name = "PRICEACTUAL", valuePraser = DoubleParser.class, isRestSave = true)
	private Double priceActual;
	@RestColumn(name = "DISCOUNT", valuePraser = DoubleParser.class)
	private Double discount;
	@RestColumn(name = "TOT_AMT_ACTUAL", valuePraser = DoubleParser.class)
	private Double totAmtActual;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
