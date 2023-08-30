package com.sungeon.bos.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * @author 刘国帅
 * @date 2020-10-26
 **/
@Alias("InterfaceResendLog")
@Data
public class InterfaceResendLog {

	private String name;
	private BigDecimal sourceId;
	private BigDecimal count;

	public String toJSONString() {
		return JSONObject.toJSONString(this);
	}

}
