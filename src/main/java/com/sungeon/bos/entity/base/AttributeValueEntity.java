package com.sungeon.bos.entity.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;


@Data
@Alias("AttributeValue")
public class AttributeValueEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String code;
	private String name;
	private Integer clr;
	private Long attributeId;
	private Long brandId;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
