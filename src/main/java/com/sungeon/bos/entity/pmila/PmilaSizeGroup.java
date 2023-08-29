package com.sungeon.bos.entity.pmila;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.annotation.RestColumn;
import com.burgeon.framework.restapi.annotation.RestTable;
import com.burgeon.framework.restapi.model.BaseRestBean;
import com.burgeon.framework.restapi.parser.value.BooleanParser;
import com.burgeon.framework.restapi.parser.value.LongParser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 陈苏洲
 * @date 2023-8-29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@RestTable(tableName = "M_SIZEGROUP", description = "尺寸组", defaultQueryFilter = "ISACTIVE = 'Y'")
public class PmilaSizeGroup extends BaseRestBean {

    @RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
    private Long id;
    @RestColumn(name = "NAME")
    private String name;
    @RestColumn(name = "CLRSIZE")
    private Integer clr;
    @RestColumn(name = "MODIFIEDDATE")
    private String modifiedDate;
    @RestColumn(name = "ISACTIVE", valuePraser = BooleanParser.class)
    private Boolean isActive;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
