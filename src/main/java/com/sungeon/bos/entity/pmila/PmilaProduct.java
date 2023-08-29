package com.sungeon.bos.entity.pmila;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.annotation.RestColumn;
import com.burgeon.framework.restapi.annotation.RestOneToMany;
import com.burgeon.framework.restapi.annotation.RestOneToOne;
import com.burgeon.framework.restapi.annotation.RestTable;
import com.burgeon.framework.restapi.model.BaseRestBean;
import com.burgeon.framework.restapi.parser.value.DoubleParser;
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
@RestTable(tableName = "M_PRODUCT", description = "款号", defaultQueryFilter = "M_PRODUCT.ISACTIVE = 'Y'")
public class PmilaProduct extends BaseRestBean {

    @RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
    private Long id;
    @RestColumn(name = "NAME")
    private String productCode;
    @RestColumn(name = "VALUE")
    private String productName;
    @RestColumn(name = "PRICELIST", valuePraser = DoubleParser.class)
    private Double priceList;
    @RestColumn(name = "M_SIZEGROUP_ID;NAME")
    private String sizeGroupName;
    @RestColumn(name = "M_DIM2_ID;ATTRIBCODE")
    private String yearCode;
    @RestColumn(name = "M_DIM2_ID;ATTRIBNAME")
    private String yearName;
    @RestColumn(name = "M_DIM3_ID;ATTRIBCODE")
    private String seasonCode;
    @RestColumn(name = "M_DIM3_ID;ATTRIBNAME")
    private String seasonName;
    @RestColumn(name = "M_DIM10_ID;ATTRIBCODE")
    private String bigClassCode;
    @RestColumn(name = "M_DIM10_ID;ATTRIBNAME")
    private String bigClassName;
    @RestColumn(name = "M_DIM6_ID;ATTRIBCODE")
    private String midClassCode;
    @RestColumn(name = "M_DIM6_ID;ATTRIBNAME")
    private String midClassName;
    @RestColumn(name = "M_DIM11_ID;ATTRIBCODE")
    private String smallClassCode;
    @RestColumn(name = "M_DIM11_ID;ATTRIBNAME")
    private String smallClassName;
    @RestColumn(name = "M_DIM4_ID;ATTRIBCODE")
    private String genderCode;
    @RestColumn(name = "M_DIM4_ID;ATTRIBNAME")
    private String genderName;
    @RestColumn(name = "M_DIM8_ID;ATTRIBCODE", isRestQuery = false)
    private String classCode;
    @RestColumn(name = "M_DIM8_ID;ATTRIBNAME", isRestQuery = false)
    private String className;
    @RestColumn(name = "M_DIM7_ID;ATTRIBCODE")
    private String bandCode;
    @RestColumn(name = "M_DIM7_ID;ATTRIBNAME")
    private String bandName;
    @RestColumn(name = "M_DIM15_ID;ATTRIBCODE")
    private String standardCode;
    @RestColumn(name = "M_DIM15_ID;ATTRIBNAME")
    private String standardName;
    @RestColumn(name = "M_DIM16_ID;ATTRIBCODE")
    private String securityCategoryCode;
    @RestColumn(name = "M_DIM16_ID;ATTRIBNAME")
    private String securityCategoryName;
    @RestColumn(name = "IMAGEURL")
    private String imageUrl;
    @RestOneToMany(fkParentColumnName = "id", fkChildColumnName = "productId", childBeanClass = PmilaProductAliasItem.class)
    private List<PmilaProductAliasItem> skus;
    @RestOneToOne(fkParentColumnName = "id", fkChildColumnName = "productId", childBeanClass = PmilaProductMediaItem.class)
    private PmilaProductMediaItem media;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }


}
