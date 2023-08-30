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
@RestTable(tableName = "B_PDT_MEDIA", refTableName = "B_PDT_MEDIA", description = "商品媒体资料",
		defaultQueryFilter = "B_PDT_MEDIA.ISACTIVE = 'Y'")
public class PmilaProductMediaItem extends BaseRestBean {

	@RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
	private Long id;
	@RestColumn(name = "M_PRODUCT_ID", valuePraser = LongParser.class)
	private Long productId;
	@RestColumn(name = "IMGURL1")
	private String imgUrl1;
	@RestColumn(name = "IMGURL2")
	private String imgUrl2;
	@RestColumn(name = "IMGURL3")
	private String imgUrl3;
	@RestColumn(name = "IMGURL4")
	private String imgUrl4;
	@RestColumn(name = "IMGURL5")
	private String imgUrl5;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}


}
