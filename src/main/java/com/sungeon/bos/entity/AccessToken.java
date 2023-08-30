package com.sungeon.bos.entity;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.Alias;
import org.quartz.Trigger.TriggerState;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Alias("AccessToken")
public class AccessToken implements Serializable {


	private static final long serialVersionUID = 1L;

	private Long id;
	private String appId;
	private String appSecret;
	private String accessToken;
	private Integer expireIn;
	private Date dateBegin;
	private Date dateEnd;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getExpireIn() {
		return expireIn;
	}

	public void setExpireIn(Integer expireIn) {
		this.expireIn = expireIn;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
