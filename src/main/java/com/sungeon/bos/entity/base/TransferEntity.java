package com.sungeon.bos.entity.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Data
@Alias("Transfer")
public class TransferEntity {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String docNo;
	private String sourceNo;
	private Integer billDate;
	private String customerCode;
	private String origCode;
	private String origName;
	private String origKind;
	private String destCode;
	private String destName;
	private String destKind;
	private Boolean isAutoSubmit = true;
	private Integer status;
	private Boolean isAutoOut = true;
	private Integer outDate;
	private Integer outStatus;
	private Boolean isAutoIn = true;
	private Integer inDate;
	private Integer inStatus;
	private String description;
	private List<ItemEntity> items;

	private Long origId;
	private Long destId;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
