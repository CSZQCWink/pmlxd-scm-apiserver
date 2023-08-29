package com.sungeon.bos.entity.pmila;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.annotation.RestColumn;
import com.burgeon.framework.restapi.annotation.RestTable;
import com.burgeon.framework.restapi.model.BaseRestBean;
import com.burgeon.framework.restapi.parser.value.LongParser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 陈苏洲
 * @date 2023-8-29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@RestTable(tableName = "M_OTHER_INOUTITEM", refTableName = "M_OTHER_INOUTITEM", description = "物理调整单明细",
        defaultQueryFilter = "M_OTHER_INOUTITEM.ISACTIVE = 'Y'")
public class PmilaInOutItem extends BaseRestBean {

    @RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
    private Long id;
    @RestColumn(name = "M_OTHER_INOUT_ID", valuePraser = LongParser.class)
    private Long otherInOutId;
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

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }


}
