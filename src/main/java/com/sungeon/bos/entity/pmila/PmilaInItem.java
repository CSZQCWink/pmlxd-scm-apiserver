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
@RestTable(tableName = "M_INITEM", refTableName = "M_INITEM", description = "入库单明细",
        defaultQueryFilter = "M_INITEM.ISACTIVE = 'Y'")
public class PmilaInItem extends BaseRestBean {

    @RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
    private Long id;
    @RestColumn(name = "M_IN_ID", valuePraser = LongParser.class)
    private Long inId;
    @RestColumn(name = "M_PRODUCTALIAS_ID;NO")
    private String sku;
    @RestColumn(name = "M_PRODUCT_ID;NAME")
    private String product;
    @RestColumn(name = "M_PRODUCT_ID__NAME", isRestSave = true, isRestQuery = false)
    private String productAdd;
    @RestColumn(name = "M_ATTRIBUTESETINSTANCE_ID", valuePraser = LongParser.class)
    private Long asiId;
    @RestColumn(name = "QTYIN", isRestSave = true)
    private Integer qtyIn;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }


}
