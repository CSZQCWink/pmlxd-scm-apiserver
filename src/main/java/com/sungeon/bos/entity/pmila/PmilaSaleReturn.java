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
@RestTable(tableName = "M_RET_SALE", description = "销售退货单", defaultQueryFilter = "M_RET_SALE.ISACTIVE = 'Y'",
        isNeedSubmit = false)
public class PmilaSaleReturn extends BaseRestBean {

    @RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
    private Long id;
    @RestColumn(name = "DOCNO")
    private String docNo;
    @RestColumn(name = "BILLDATE", isRestSave = true)
    private Integer billDate;
    @RestColumn(name = "DATEOUT", isRestSave = true)
    private Integer outDate;
    @RestColumn(name = "DATEIN", isRestSave = true)
    private Integer inDate;
    @RestColumn(name = "RETSALETYPE1", isRestSave = true)
    private String retSaleType;
    @RestColumn(name = "C_ORIG_ID;CODE")
    private String origCode;
    @RestColumn(name = "C_ORIG_ID;NAME")
    private String origName;
    @RestColumn(name = "C_ORIG_ID__NAME", isRestSave = true, isRestQuery = false)
    private String origAddName;
    @RestColumn(name = "C_STORE_ID;CODE")
    private String destCode;
    @RestColumn(name = "C_STORE_ID;NAME")
    private String destName;
    @RestColumn(name = "C_STORE_ID__NAME", isRestSave = true, isRestQuery = false)
    private String destAddName;
    @RestColumn(name = "C_SALEDISTYPE_ID__NAME", isRestSave = true, isRestQuery = false)
    private String saleDistTypeAdd = "现货非买断退货";
    @RestColumn(name = "C_SALEDISTYPE_ID;NAME")
    private String saleDistType;
    @RestColumn(name = "SOURCENO", isRestSave = true)
    private String sourceNo;
    @RestColumn(name = "DESCRIPTION", isRestSave = true)
    private String description;
    @RestColumn(name = "STATUS")
    private String status;
    @RestColumn(name = "OUT_STATUS")
    private String outStatus;
    @RestColumn(name = "IN_STATUS")
    private String inStatus;
    @RestOneToMany(fkParentColumnName = "id", fkChildColumnName = "retSaleId", childBeanClass = PmilaSaleReturnItem.class)
    private List<PmilaSaleReturnItem> items;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }


}
