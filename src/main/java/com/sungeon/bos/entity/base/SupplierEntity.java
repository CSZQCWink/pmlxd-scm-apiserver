package com.sungeon.bos.entity.base;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.annotation.RestColumn;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * @BelongsPackage: com.sungeon.bos.entity.base
 * @ClassName: SupplierEntity
 * @Author: 陈苏洲
 * @Description: 供应商类
 * @CreateTime: 2023-08-30 13:24
 * @Version: 1.0
 */

@Data
@Alias("Supplier")
public class SupplierEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	// id
	private Long id;

	// 供应商类型
	private String supplierType;

	// 描述
	private String description;

	// 外购区域
	private String supplierTypeId;

	// 供应商编号
	private String supplierCode;

	// 供应商名称
	private String supplierName;

	// 供应商资金余额
	private Double supplierFeerPrice;

	// 供应商银行账号
	private String supplierAccount;

	// 供应商电子邮件
	private String supplierEmail;

	// 供应商电话
	private String supplierPhone;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
