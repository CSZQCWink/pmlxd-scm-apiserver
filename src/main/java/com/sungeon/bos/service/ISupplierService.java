package com.sungeon.bos.service;

import com.sungeon.bos.entity.base.SupplierEntity;

import java.util.List;

/**
 * @BelongsPackage: com.sungeon.bos.service
 * @ClassName: ISupplierService
 * @Author: 陈苏洲
 * @Description: 供应商Service接口方法
 * @CreateTime: 2023-08-30 13:23
 * @Version: 1.0
 */

public interface ISupplierService {


	// 同步帕米拉供应商
	List<SupplierEntity> syncPmilaSupplier(String startTime, String code, int page, int pageSize);
}
