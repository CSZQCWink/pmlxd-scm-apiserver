package com.sungeon.bos.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Alias("ThirdTime")
public class ThirdTime implements Serializable {


	private static final long serialVersionUID = 1L;

	private Integer id;
	private String type;
	private String time;
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
