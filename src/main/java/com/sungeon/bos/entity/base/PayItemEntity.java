package com.sungeon.bos.entity.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;


@Data
@Alias("PayItem")
public class PayItemEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String payWayCode;
	private String payWayName;
	private Double payAmount;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
