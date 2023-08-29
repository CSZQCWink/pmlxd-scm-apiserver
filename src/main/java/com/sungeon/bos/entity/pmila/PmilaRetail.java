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
@RestTable(tableName = "M_RETAIL", description = "零售单", defaultQueryFilter = "M_RETAIL.ISACTIVE = 'Y'", isNeedSubmit = true)
public class PmilaRetail extends BaseRestBean {

    @RestColumn(name = "ID", valuePraser = LongParser.class, isRestSave = true)
    private Long id;
    @RestColumn(name = "DOCNO", isAkField = true)
    private String docNo;
    @RestColumn(name = "BILLDATE", isRestSave = true)
    private Integer billDate;
    @RestColumn(name = "C_STORE_ID__NAME", isRestSave = true, isRestQuery = false)
    private String storeName;
    @RestColumn(name = "REFNO", isRestSave = true)
    private String sourceNo;
    @RestColumn(name = "DESCRIPTION", isRestSave = true)
    private String description;
    @RestOneToMany(fkParentColumnName = "id", fkChildColumnName = "retailId", childBeanClass = PmilaRetailItem.class)
    private List<PmilaRetailItem> items;
    @RestOneToMany(fkParentColumnName = "id", fkChildColumnName = "retailId", childBeanClass = PmilaRetailPayItem.class)
    private List<PmilaRetailPayItem> payItems;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
