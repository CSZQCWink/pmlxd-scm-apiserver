package com.sungeon.bos.entity.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.ibatis.type.Alias;


@Data
@Alias("Dim")
public class DimEntity {

	private Long id;
	private String code;
	private String name;
	private String dimFlag;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
