
package com.sungeon.bos.entity.base;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.alibaba.fastjson.JSONObject;


@Alias("Order")
public class OrderEntity implements Serializable {

	private static final long serialVersionUID = 6937393310805654706L;

	private String orderid = null; // 电商单号ID
	private String ordersn = null; // 电商单号（平台单号）
	private String distrtype = null; // 配送方式
	private String shipcode = null; // 快递公司编号
	private String shipname = null; // 快递公司名称
	private String logisticcode = null; // 快递单号

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}

	public String getDistrtype() {
		return distrtype;
	}

	public void setDistrtype(String distrtype) {
		this.distrtype = distrtype;
	}

	public String getShipcode() {
		return shipcode;
	}

	public void setShipcode(String shipcode) {
		this.shipcode = shipcode;
	}

	public String getShipname() {
		return shipname;
	}

	public void setShipname(String shipname) {
		this.shipname = shipname;
	}

	public String getLogisticcode() {
		return logisticcode;
	}

	public void setLogisticcode(String logisticcode) {
		this.logisticcode = logisticcode;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return JSONObject.toJSONString(this);
	}

}
