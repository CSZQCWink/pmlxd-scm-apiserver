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
@RestTable(tableName = "M_OUT", description = "出库单", defaultQueryFilter = "M_OUT.ISACTIVE = 'Y'")
public class PmilaOut extends BaseRestBean {

    @RestColumn(name = "ID", valuePraser = LongParser.class)
    private Long id;
    @RestColumn(name = "DOCNO", isAkField = true)
    private String docNo;
    @RestColumn(name = "BILLTYPE")
    private String billType;
    @RestColumn(name = "STATUS")
    private String status;
    @RestOneToMany(fkParentColumnName = "id", fkChildColumnName = "outId", childBeanClass = PmilaOutItem.class)
    private List<PmilaOutItem> items;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
