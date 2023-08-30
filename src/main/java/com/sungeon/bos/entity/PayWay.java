package com.sungeon.bos.entity;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Alias("PayWay")
public class PayWay implements Serializable {


	private static final long serialVersionUID = 1L;

	// @Excel(name = "id")
	private Integer id;
	// @Excel(name = "付款方式")
	private String code;
	// @Excel(name = "付款方式名称")
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
