package com.sungeon.bos.entity.base;

import java.io.Serializable;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import com.alibaba.fastjson.JSONObject;

@Data
@Alias("VIP")
public class VipEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer eVipId;
	private String cardNo;
	private String vipName;
	private String password;
	private String mobile;
	private String store;
	private String customer;
	private String gender;
	private Integer birthday;
	private String email;
	private String vipType;
	private Integer openDate;
	private Integer integral;
	private String description;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
