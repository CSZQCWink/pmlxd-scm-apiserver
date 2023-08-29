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
@RestTable(tableName = "M_PRODUCT_ALIAS", refTableName = "M_PRODUCT_ALIAS", description = "条码",
        defaultQueryFilter = "M_PRODUCT_ALIAS.ISACTIVE = 'Y'")
public class PmilaProductAliasItem extends BaseRestBean {

    @RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
    private Long id;
    @RestColumn(name = "M_PRODUCT_ID", valuePraser = LongParser.class)
    private Long productId;
    @RestColumn(name = "NO")
    private String sku;
    @RestColumn(name = "M_ATTRIBUTESETINSTANCE_ID;VALUE1_CODE")
    private String colorCode;
    @RestColumn(name = "M_ATTRIBUTESETINSTANCE_ID;VALUE1")
    private String colorName;
    @RestColumn(name = "M_ATTRIBUTESETINSTANCE_ID;VALUE2_CODE")
    private String sizeCode;
    @RestColumn(name = "M_ATTRIBUTESETINSTANCE_ID;VALUE2")
    private String sizeName;
    @RestColumn(name = "INTSCODE")
    private String intsCode;
    @RestColumn(name = "FORCODE")
    private String forCode;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }


}
