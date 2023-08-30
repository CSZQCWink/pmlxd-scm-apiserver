package com.sungeon.bos.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
public class BosResult implements Serializable {


	private static final long serialVersionUID = 1L;

	private int code;
	private String message = "OK";

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
