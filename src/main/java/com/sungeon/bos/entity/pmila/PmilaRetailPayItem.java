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
@RestTable(tableName = "M_RETAILPAYITEM", refTableName = "M_RETAILPAYITEM", description = "零售单付款明细",
        defaultQueryFilter = "M_RETAILPAYITEM.ISACTIVE = 'Y'")
public class PmilaRetailPayItem extends BaseRestBean {

    @RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
    private Long id;
    @RestColumn(name = "M_RETAIL_ID", valuePraser = LongParser.class)
    private Long retailId;
    @RestColumn(name = "C_PAYWAY_ID__NAME", isRestSave = true, isRestQuery = false)
    private String payWayName = "现金";
    @RestColumn(name = "PAYAMOUNT", valuePraser = DoubleParser.class, isRestSave = true)
    private Double payAmount;
    @RestColumn(name = "BASE_PAYAMOUNT", valuePraser = DoubleParser.class, isRestSave = true)
    private Double basePayAmount;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }


}
