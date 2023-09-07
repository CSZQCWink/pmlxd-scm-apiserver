package com.sungeon.bos.task;

import com.sungeon.bos.core.utils.CollectionUtils;
import com.sungeon.bos.entity.base.AttributeEntity;
import com.sungeon.bos.entity.base.AttributeValueEntity;
import com.sungeon.bos.entity.base.DimEntity;
import com.sungeon.bos.entity.base.ProductEntity;
import com.sungeon.bos.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : 刘国帅
 * @date : 2016-12-6
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class ProductTask extends BaseTask {

	@Autowired
	private IProductService productService;

	/**
	 * @title: syncPmilaDim
	 * @author: 陈苏洲
	 * @description: 同步属性
	 * @param: []
	 * @return: void
	 * @date: 2023/8/30 11:31
	 **/
	public void syncPmilaDim() {
		try {
			List<DimEntity> dims;
			String startTime = baseService.getThirdTime("BSIJA_DIM_SYNC_TIME");
			Date now = new Date();
			int page = 1;
			int pageSize = 200;
			do {
				// 年份
				dims = productService.syncPmilaDim("DIM2", "DIM5", false, startTime,
						null, page++, pageSize);
			} while (dims.size() == pageSize);
			page = 1;
			do {
				// 季节
				dims = productService.syncPmilaDim("DIM3", "DIM6", true, startTime,
						null, page++, pageSize);
			} while (dims.size() == pageSize);
			page = 1;
			do {
				// 性别
				dims = productService.syncPmilaDim("DIM4", "DIM20", false, startTime,
						null, page++, pageSize);
			} while (dims.size() == pageSize);
			page = 1;
			do {
				// 大类
				dims = productService.syncPmilaDim("DIM10", "DIM17", true, startTime,
						null, page++, pageSize);
			} while (dims.size() == pageSize);
			page = 1;
			do {
				// 中类
				dims = productService.syncPmilaDim("DIM6", "DIM18", true, startTime,
						null, page++, pageSize);
			} while (dims.size() == pageSize);
			page = 1;
			do {
				// 品类——帕米拉-小类
				dims = productService.syncPmilaDim("DIM8", "DIM19", true, startTime,
						null, page, pageSize);
			} while (dims.size() == pageSize);
			page = 1;
			do {
				// 波段
				dims = productService.syncPmilaDim("DIM7", "DIM16", true, startTime,
						null, page++, pageSize);
			} while (dims.size() == pageSize);
			page = 1;
			do {
				// 执行标准
				dims = productService.syncPmilaDim("DIM15", "DIM13", true, startTime,
						null, page++, pageSize);
			} while (dims.size() == pageSize);
			page = 1;
			do {
				// 安全类别
				dims = productService.syncPmilaDim("DIM16", "DIM14", true, startTime,
						null, page++, pageSize);
			} while (dims.size() == pageSize);
			baseService.updateThirdTime("BSIJA_DIM_SYNC_TIME", now);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * @title: syncPmilaColor
	 * @author: 陈苏洲
	 * @description: 同步颜色信息
	 * @param: []
	 * @return: void
	 * @date: 2023/8/30 11:29
	 **/
	public void syncPmilaColor() {
		try {
			List<AttributeValueEntity> attributes;
			String startTime = baseService.getThirdTime("BSIJA_COLOR_SYNC_TIME");
			Date now = new Date();
			int page = 1;
			int pageSize = 100;
			do {
				attributes = productService.syncPmilaColor(startTime, null, ++page, pageSize);
			} while (attributes.size() == pageSize);
			baseService.updateThirdTime("BSIJA_COLOR_SYNC_TIME", now);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * @title: syncPmilaSize
	 * @author: 陈苏洲
	 * @description: 同步尺寸
	 * @param: []
	 * @return: void
	 * @date: 2023/8/30 11:30
	 **/
	public void syncPmilaSize() {
		try {
			List<AttributeValueEntity> attributes;
			String startTime = baseService.getThirdTime("BSIJA_SIZE_SYNC_TIME");
			Date now = new Date();
			int page = 1;
			int pageSize = 100;
			do {
				attributes = productService.syncPmilaSize(startTime, null, page++, pageSize);
			} while (attributes.size() == pageSize);
			baseService.updateThirdTime("BSIJA_SIZE_SYNC_TIME", now);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * @title: syncPmilaProduct
	 * @author: 陈苏洲
	 * @description: 同步款号档案
	 * @param: []
	 * @return: void
	 * @date: 2023/8/30 11:30
	 **/
	public void syncPmilaProduct() {
		Date now = new Date();
		try {
			List<ProductEntity> products;
			String startTime = baseService.getThirdTime("BSIJA_PRODUCT_SYNC_TIME");
			int page = 1;
			int pageSize = 100;
			List<String> productCodes = new ArrayList<>();
			boolean be = false;
			do {
				products = productService.syncPmilaProduct(startTime, null, page++, pageSize);
				if (CollectionUtils.isNotEmpty(products)) {
					for (ProductEntity product : products) {
						boolean exists = productCodes.stream().anyMatch(code -> product.getProductCode().equals(code));
						if (exists) {
							be = true;
						} else {
							productCodes.add(product.getProductCode());
						}
					}
					if (be) {
						break;
					}
				}
			} while (products.size() == pageSize);
			baseService.updateThirdTime("BSIJA_PRODUCT_SYNC_TIME", now);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	/**
	 * @title: syncPmilaAttribute
	 * @author: 陈苏洲
	 * @description: 同步商品特征包含尺寸组和颜色组
	 * @param: []
	 * @return: void
	 * @date: 2023/9/7 9:51
	 **/
	public void syncPmilaAttribute() {
		try {
			List<AttributeEntity> attributes;
			String startTime = baseService.getThirdTime("BSIJA_COLOR_SYNC_TIME");
			Date now = new Date();
			int page = 1;
			int pageSize = 100;
			do {
				attributes = productService.syncPmilaAttribute(startTime, null, page++, pageSize);
			} while (attributes.size() == pageSize);
			baseService.updateThirdTime("BSIJA_COLOR_SYNC_TIME", now);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
