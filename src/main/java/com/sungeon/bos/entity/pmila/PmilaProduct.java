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
	@RestColumn(name = "M_SIZEGROUP_ID;NAME")
	private String sizeGroupName;
	@RestColumn(name = "preCost", valuePraser = DoubleParser.class)
	private Double preCost;
	@RestColumn(name = "PRICELIST", valuePraser = DoubleParser.class)
	private Double priceList;

	// 品牌
	@RestColumn(name = "M_DIM1_ID;ATTRIBCODE")
	private String bandCode;
	@RestColumn(name = "M_DIM1_ID;ATTRIBNAME")
	private String bandName;
	// 年份
	@RestColumn(name = "M_DIM2_ID;ATTRIBCODE")
	private String yearCode;
	@RestColumn(name = "M_DIM2_ID;ATTRIBNAME")
	private String yearName;
	// 季节
	@RestColumn(name = "M_DIM3_ID;ATTRIBCODE")
	private String seasonCode;
	@RestColumn(name = "M_DIM3_ID;ATTRIBNAME")
	private String seasonName;
	// 大类
	@RestColumn(name = "M_DIM4_ID;ATTRIBCODE")
	private String bigClassCode;
	@RestColumn(name = "M_DIM4_ID;ATTRIBNAME")
	private String bigClassName;
	// 小类
	@RestColumn(name = "M_DIM5_ID;ATTRIBCODE")
	private String smallClassCode;
	@RestColumn(name = "M_DIM5_ID;ATTRIBNAME")
	private String smallClassName;
	// 材质大类
	@RestColumn(name = "M_DIM7_ID;ATTRIBCODE")
	private String materialBigClassCode;
	@RestColumn(name = "M_DIM7_ID;ATTRIBNAME")
	private String materialBigClassName;
	// 跟型
	@RestColumn(name = "M_DIM8_ID;ATTRIBCODE")
	private String HeelTypeCode;
	@RestColumn(name = "M_DIM8_ID;ATTRIBNAME")
	private String HeelTypeName;
	// 产地
	@RestColumn(name = "M_DIM9_ID;ATTRIBCODE")
	private String originPlaceCode;
	@RestColumn(name = "M_DIM9_ID;ATTRIBNAME")
	private String originPlaceName;
	// 质量等级
	@RestColumn(name = "M_DIM14_ID;ATTRIBCODE")
	private String qualityGradeCode;
	@RestColumn(name = "M_DIM14_ID;ATTRIBNAME")
	private String qualityGradeName;
	// 执行标准
	@RestColumn(name = "M_DIM15_ID;ATTRIBCODE")
	private String standardCode;
	@RestColumn(name = "M_DIM15_ID;ATTRIBNAME")
	private String standardName;

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
