package com.sungeon.bos.entity.base;

import java.io.Serializable;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import com.alibaba.fastjson.JSONObject;


@Data
@Alias("Ticket")
public class TicketEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String ticketNo; // 券号
	private String checkNo; // 密码、校验码
	private String name; // 名称
	private Double parValue; // 面值
	private Integer dateBeg; // 有效期起
	private Integer dateEnd; // 有效期止
	private String note; // 使用条件、使用备注
	private Double amount; // 满足金额
	private String givenTime; // 发放时间
	private String isVerify; // 是否核销
	private String verifyTime; // 核销时间
	private String vipCardNo; // 所属会员卡号
	private String vipId; // 所属会员ID

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
