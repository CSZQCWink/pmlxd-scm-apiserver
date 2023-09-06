package com.sungeon.bos.service.impl;

import com.burgeon.framework.restapi.request.QueryFilterCombine;
import com.burgeon.framework.restapi.request.QueryFilterParam;
import com.burgeon.framework.restapi.request.QueryOrderByParam;
import com.sungeon.bos.core.exception.ParamNullException;
import com.sungeon.bos.core.utils.CollectionUtils;
import com.sungeon.bos.core.utils.StringUtils;
import com.sungeon.bos.dao.ISupplierDao;
import com.sungeon.bos.entity.base.SupplierEntity;
import com.sungeon.bos.entity.pmila.PmilaSupplier;
import com.sungeon.bos.service.ISupplierService;
import com.sungeon.bos.util.BurgeonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsPackage: com.sungeon.bos.service.impl
 * @ClassName: SupplierServiceImpl
 * @Author: 陈苏洲
 * @Description: 实现SupplierService接口方法
 * @CreateTime: 2023-08-30 13:31
 * @Version: 1.0
 */
@Slf4j
@Service
public class SupplierServiceImpl implements ISupplierService {

	@Autowired
	private ISupplierDao supplierDao;

	@Autowired
	private BurgeonRestClient burgeonRestClient;



	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<SupplierEntity> syncPmilaSupplier(String startTime, String code, int page, int pageSize) {
		int start = (page - 1) * pageSize;
		List<QueryFilterParam> filterParamList = new ArrayList<>();
		if (StringUtils.isNotEmpty(code)) {
			filterParamList.add(new QueryFilterParam("NAME", code, QueryFilterCombine.AND));
		}
		if (StringUtils.isNotEmpty(startTime)) {
			filterParamList.add(new QueryFilterParam("", "C_SUPPLIER.MODIFIEDDATE > to_date('" + startTime
					+ "', 'yyyy-mm-dd hh24:mi:ss')", QueryFilterCombine.AND));
		}
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));

		List<PmilaSupplier> pmilaSupplierList = burgeonRestClient.query(PmilaSupplier.class, start,
																		pageSize, filterParamList, orderByParamList);
		ArrayList<SupplierEntity> supplierEntityList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(pmilaSupplierList)){
			log.info("获取帕米拉供应商响应：{}", pmilaSupplierList);
			pmilaSupplierList.forEach(s -> {
				SupplierEntity supplier = new SupplierEntity();
				BeanUtils.copyProperties(s, supplier);
				supplier.setId(null);
				supplier.setSupplierCode(s.getSupplierCode());
				supplier.setSupplierName(s.getSupplierName());
				supplier.setSupplierFeerPrice(s.getSupplierFeerPrice());
				supplier.setSupplierAccount(s.getSupplierAccount());
				supplier.setSupplierEmail(s.getSupplierEmail());
				supplier.setSupplierPhone(s.getSupplierPhone());
				supplierEntityList.add(supplier);
				supplierDao.addSupplier(supplier);
			});
		}
		return supplierEntityList;
	}
}
