package com.sungeon.bos.entity;

import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.core.utils.DateTimeUtils;
import com.sungeon.bos.util.SystemProperties;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * @author 刘国帅
 * @date 2020-10-26
 **/
@Alias("InterfaceLog")
@Data
public class InterfaceLog {

	private Long id;
	/**
	 * 接口应用，可根据任务组
	 */
	private String application;
	/**
	 * 接口类型：SEND:推送，RECEIVE:接受
	 */
	private String type;
	/**
	 * 接口名称
	 */
	private String name;
	/**
	 * 接口地址
	 */
	private String url;
	/**
	 * 接口方法、API
	 */
	private String method;
	/**
	 * 处理日期
	 */
	private Integer dealDate;
	/**
	 * 处理时间
	 */
	private Date dealTime;
	/**
	 * 源数据，比如单据编号
	 */
	private String source;
	/**
	 * 源数据ID
	 */
	private Long sourceId;
	/**
	 * 请求索引
	 */
	private String requestId;
	/**
	 * 发送/接受的数据
	 */
	private String data;
	/**
	 * 发送/接受处理结果
	 */
	private String result;
	/**
	 * 发送/接受处理结果描述
	 */
	private String resultMessage;
	/**
	 * 错误日志
	 */
	private String errorMessage;
	/**
	 * 数量
	 */
	private Integer count;

	public InterfaceLog(String type) {
		this.application = SystemProperties.ScheduleGroup;
		this.type = type;
		this.dealDate = DateTimeUtils.todayNumber();
		this.dealTime = new Date();
	}

	public InterfaceLog(String type, String url, String method) {
		this.application = SystemProperties.ScheduleGroup;
		this.type = type;
		this.url = url;
		this.method = method;
		this.dealDate = DateTimeUtils.todayNumber();
		this.dealTime = new Date();
	}

	public InterfaceLog(String type, String name, String method, String source, Long sourceId, String requestId) {
		this.application = SystemProperties.ScheduleGroup;
		this.type = type;
		this.name = name;
		this.method = method;
		this.source = source;
		this.sourceId = sourceId;
		this.requestId = requestId;
		this.dealDate = DateTimeUtils.todayNumber();
		this.dealTime = new Date();
	}

	public String toJSONString() {
		return JSONObject.toJSONString(this);
	}

}
