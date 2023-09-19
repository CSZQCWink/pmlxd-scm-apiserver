package com.sungeon.bos.service.impl;

import com.burgeon.framework.restapi.request.QueryFilterCombine;
import com.burgeon.framework.restapi.request.QueryFilterParam;
import com.burgeon.framework.restapi.request.QueryOrderByParam;
import com.sungeon.bos.core.exception.ParamNotMatchException;
import com.sungeon.bos.core.exception.ParamNullException;
import com.sungeon.bos.core.utils.CollectionUtils;
import com.sungeon.bos.core.utils.StringUtils;
import com.sungeon.bos.dao.IBaseDao;
import com.sungeon.bos.dao.IProductDao;
import com.sungeon.bos.dao.ISupplierDao;
import com.sungeon.bos.entity.base.*;
import com.sungeon.bos.entity.pmila.*;
import com.sungeon.bos.service.IProductService;
import com.sungeon.bos.util.BurgeonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 刘国帅
 * @author 陈苏洲
 * @date 2019-10-9
 * @date 2023-9
 **/
@Slf4j
@Service("productService")
public class ProductServiceImpl implements IProductService {

	@Autowired
	private IProductDao productDao;
	@Autowired
	private IBaseDao baseDao;
	@Autowired
	private ISupplierDao supplierDao;
	@Autowired
	private BurgeonRestClient burgeonRestClient;
	private static Long colorGroupId = null;
	// 颜色维护是否到品牌
//	private static String portal6162 = null;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Integer addProduct(List<ProductEntity> products) {
		// 首先判断xe系统中的款号档案是否存在 不存在返回0
		if (CollectionUtils.isEmpty(products)) {
			return 0;
		}
		// 存在直接遍历
		for (ProductEntity product : products) {
			if (StringUtils.isEmpty(product.getBrandName())) {
				throw new ParamNullException("品牌不能为空");
			}
			if (StringUtils.isEmpty(product.getSizeGroupName())) {
				throw new ParamNullException("尺码组不能为空");
			}
			// 根据款号查询指定的商品
			ProductEntity pdt = productDao.queryProductByCode(product.getProductCode());
			Long sizeGroupId;
			// 如果商品不为空 设置尺寸组id 为空查询出来在设置
			if (null != pdt) {
				sizeGroupId = pdt.getSizeGroupId();
			} else {
				sizeGroupId = productDao.queryAttributeId(2, product.getSizeGroupName());
				if (sizeGroupId == null) {
					log.error("尺寸组[" + product.getSizeGroupName() + "]不存在");
					throw new ParamNotMatchException("尺寸组[" + product.getSizeGroupName() + "]不存在");
				}
			}
			product.setSizeGroupId(sizeGroupId);

			// 根据品牌名查询指定的品牌id
			Long brandId = productDao.queryDimId("DIM1", product.getBrandName());
			// 判断品牌是否存在 不存在直接抛出异常
			if (brandId == null) {
				throw new ParamNotMatchException("品牌[" + product.getBrandName() + "]不存在");
			}
			product.setBrandId(brandId);

			// 年份
			if (!StringUtils.isEmpty(product.getYearName())) {
				product.setYearId(getDimId("DIM2", product.getYearCode(), product.getYearName()));
			}
			// 季节
			if (!StringUtils.isEmpty(product.getSeasonName())) {
				product.setSeasonId(getDimId("DIM3", product.getSeasonCode(), product.getSeasonName()));
			}
			// 大类
			if (!StringUtils.isEmpty(product.getBigClassName())) {
				product.setBigClassId(getDimId("DIM4", product.getBigClassCode(), product.getBigClassName()));
			}
			// 小类
			if (StringUtils.isNotEmpty(product.getSmallClassName())) {
				product.setSmallClassId(getDimId("DIM5", product.getSmallClassCode(), product.getSmallClassName()));
			}
			// 材质大类
			if (StringUtils.isNotEmpty(product.getMaterialBigClassName())) {
				product.setMaterialBigClassId(getDimId("DIM6", product.getMaterialBigClassCode(), product.getMaterialBigClassName()));
			}
			// 跟型
			if (StringUtils.isNotEmpty(product.getHeelTypeName())) {
				product.setHeelTypeId(getDimId("DIM7", product.getHeelTypeCode(), product.getHeelTypeName()));
			}
			// 产地
			if (StringUtils.isNotEmpty(product.getOriginPlaceName())) {
				product.setOriginPlaceId(getDimId("DIM8", product.getOriginPlaceCode(), product.getOriginPlaceName()));
			}
			// 质量等级
			// 产地
			if (StringUtils.isNotEmpty(product.getQualityGradeName())) {
				product.setQualityGradeId(getDimId("DIM9", product.getQualityGradeCode(), product.getQualityGradeName()));
			}
			// 执行标准
			if (!StringUtils.isEmpty(product.getStandardName())) {
				product.setStandardId(getDimId("DIM10", product.getStandardCode(), product.getStandardName()));
			}

			if (!StringUtils.isEmpty(product.getSupplierCode())) {
				Long supplierId = supplierDao.querySupplierIdByCode(product.getSupplierCode());
				product.setSupplierId(supplierId);
			}

			if (null == pdt) {
				// 新增款号
				productDao.insertProduct(product);
				productDao.callProductAc(product.getId());
			} else {
				// 更新款号
				product.setId(pdt.getId());
				productDao.updateProduct(product);
			}
//			productDao.updateProductMedia(product.getId(), product.getProductCode());
			if (null == colorGroupId) {
				colorGroupId = productDao.queryAttributeId(1, "颜色");
			}

			if (CollectionUtils.isNotEmpty(product.getSkus())) {
				product.getSkus().forEach(sku -> {
					sku.setProductId(product.getId());
					sku.setProductCode(product.getProductCode());
					sku.setBrandId(brandId);
					sku.setSizeGroupId(sizeGroupId);
					getAttributeValue(1, sku.getColorCode(), sku.getColorName(), brandId, colorGroupId);
					getAttributeValue(2, sku.getSizeCode(), sku.getSizeName(), null, sizeGroupId);
				});
				productDao.insertSku(product.getSkus());
			}
		}
		return 1;
	}

	private Long getDimId(String dimFlag, String code, String name) {
		Long dimId = productDao.queryDimId(dimFlag, name);
		if (null == dimId) {
			DimEntity dim = new DimEntity();
			dim.setCode(code);
			dim.setName(name);
			dim.setDimFlag(dimFlag);
			productDao.insertDim(dim);
			dimId = dim.getId();
		}
		return dimId;
	}


	private AttributeValueEntity getAttributeValue(int clr, String attributeValueCode, String attributeValueName, Long brandId,
	                                               Long attributeId) {
//		AttributeValueEntity attributeValue = productDao.queryAttributeValue(clr, attributeValueCode, attributeId, brandId);
		AttributeValueEntity attributeValue = productDao.queryAttributeValueByName(clr, attributeValueName, attributeId, brandId);
		if (null == attributeValue) {
			attributeValue = new AttributeValueEntity();
			attributeValue.setClr(clr);
			attributeValue.setAttributeId(attributeId);
			attributeValue.setCode(attributeValueCode);
			attributeValue.setName(attributeValueName);
			attributeValue.setBrandId(brandId);
			productDao.insertAttributeValue(attributeValue);
			if (clr == 1) {
				productDao.callColorAc(attributeValue.getId());
			} else {
				productDao.callSizeAc(attributeValue.getId());
			}
		}
		return attributeValue;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<DimEntity> syncPmilaDim(String dimFlag, String pmlDimFlag, boolean isUseCode, String startTime,
	                                    String dimName, int page, int pageSize) {
		int start = (page - 1) * pageSize;
		List<QueryFilterParam> filterParamList = new ArrayList<>();
		filterParamList.add(new QueryFilterParam("DIMFLAG", dimFlag, QueryFilterCombine.AND));
		if (StringUtils.isNotEmpty(startTime)) {
			filterParamList.add(new QueryFilterParam("", "MODIFIEDDATE > to_date('" + startTime
					+ "', 'yyyy-mm-dd hh24:mi:ss')", QueryFilterCombine.AND));
		}
		if (StringUtils.isNotEmpty(dimName)) {
			filterParamList.add(new QueryFilterParam("ATTRIBNAME", dimName, QueryFilterCombine.AND));
		}
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));

		List<PmilaDim> dims = burgeonRestClient.query(PmilaDim.class, start, pageSize, filterParamList, orderByParamList);
		List<DimEntity> dimList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(dims)) {
			log.info("获取帕米拉属性值响应：{}", dims);
			dims.forEach(d -> {
				DimEntity dim = new DimEntity();
				dim.setCode(isUseCode ? d.getCode() : d.getName());
				dim.setName(d.getName());
				dim.setDimFlag(pmlDimFlag);
				Long dimId;
				if ("DIM16".equals(pmlDimFlag)) {
					dimId = productDao.queryDimId(pmlDimFlag, dim.getCode());
				} else {
					dimId = productDao.queryDimId(pmlDimFlag, dim.getName());
				}
				if (null == dimId) {
					productDao.insertDim(dim);
				}
				dimList.add(dim);
			});
		}
		return dimList;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<AttributeValueEntity> syncPmilaColor(String startTime, String colorName, int page, int pageSize) {
		List<QueryFilterParam> filterParamList = new ArrayList<>();
		if (StringUtils.isNotEmpty(colorName)) {
			filterParamList.add(new QueryFilterParam("NAME", colorName, QueryFilterCombine.AND));
		}
		if (StringUtils.isNotEmpty(startTime)) {
			filterParamList.add(new QueryFilterParam("", "MODIFIEDDATE > to_date('" + startTime
					+ "', 'yyyy-mm-dd hh24:mi:ss')", QueryFilterCombine.AND));
		}
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));

		List<PmilaColor> colors = burgeonRestClient.query(PmilaColor.class, 1, 1000, filterParamList,
				orderByParamList);
		List<AttributeEntity> attributeEntityList = productDao.queryAttribute();
		for (AttributeEntity attributeEntity : attributeEntityList) {
			if (!attributeEntity.getName().equals("颜色") && attributeEntity.getName() == null) {
				AttributeEntity attribute = new AttributeEntity();
				attribute.setName("颜色");
				attribute.setClrSize(1);
				productDao.insertAttribute(attribute);
			}
		}
		List<AttributeValueEntity> colorList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(colors)) {
			log.info("获取帕米拉颜色响应：{}", colors);
			Long brandId = productDao.queryDimId("DIM1", "名典");
			Long attributeId = productDao.queryAttributeId(1, "颜色");
			colors.forEach(d -> {
				AttributeValueEntity attributeValue = getAttributeValue(1, d.getCode(), d.getName(), brandId, attributeId);
				colorList.add(attributeValue);
			});
		}
		return colorList;
	}

	@Override
	public List<AttributeValueEntity> syncPmilaSize(String startTime, String sizeName, int page, int pageSize) {
		int start = (page - 1) * pageSize;
		List<QueryFilterParam> filterParamList = new ArrayList<>();
		if (StringUtils.isNotEmpty(sizeName)) {
			filterParamList.add(new QueryFilterParam("NAME", sizeName, QueryFilterCombine.AND));
		}
		if (StringUtils.isNotEmpty(startTime)) {
			filterParamList.add(new QueryFilterParam("", "MODIFIEDDATE > to_date('" + startTime
					+ "', 'yyyy-mm-dd hh24:mi:ss')", QueryFilterCombine.AND));
		}
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));

		List<PmilaSize> sizes = burgeonRestClient.query(PmilaSize.class, start, pageSize, filterParamList, orderByParamList);
		List<AttributeValueEntity> sizeList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(sizes)) {
			// 创建一个Map集合用来存储尺寸组id和尺寸组的信息
			HashMap<Long, PmilaSizeGroup> map = new HashMap<>();
//			log.info("获取帕米拉尺寸响应：{}", sizes);
			for (int i = 0; i < sizes.size(); i++) {
				if (sizes.get(i).getSizeGroup() != null) {
					map.put(sizes.get(i).getSizeGroupId(), sizes.get(i).getSizeGroup());
				}

				if (sizes.get(i).getSizeGroup() == null) {
					PmilaSizeGroup group = map.get(sizes.get(i).getSizeGroupId());
					sizes.get(i).setSizeGroup(group);
				}
				Long attributeId = productDao.queryAttributeId(2, sizes.get(i).getSizeGroup().getName());
				AttributeValueEntity attributeValue = getAttributeValue(2, sizes.get(i).getCode(), sizes.get(i).getName(), null, attributeId);
				sizeList.add(attributeValue);
			}
		}
		return sizeList;
	}

	@Value("${Scm.Pmila.Api.Url}")
	private String pmilaUrl;
	@Value("${Portal.Product.Image.Path}")
	private String productImagePath;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<ProductEntity> syncPmilaProduct(String startTime, String productCode, int page, int pageSize) {
		int start = (page - 1) * pageSize;
		List<QueryFilterParam> filterParamList = new ArrayList<>();
		filterParamList.add(new QueryFilterParam("M_DIM2_ID;ATTRIBNAME", "2023", QueryFilterCombine.AND));
//		filterParamList.add(new QueryFilterParam("NAME", "MS9681", QueryFilterCombine.AND));
//		if (StringUtils.isNotEmpty(productCode)) {
//			filterParamList.add(new QueryFilterParam("NAME", productCode, QueryFilterCombine.AND));
//		}
//		if (StringUtils.isNotEmpty(startTime)) {
//			filterParamList.add(new QueryFilterParam("", "M_PRODUCT.CREATIONDATE <= to_date('" + startTime
//					+ "', 'yyyy-mm-dd hh24:mi:ss')", QueryFilterCombine.AND));
//		}
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));

		// 获取名典品牌方的款号档案并返回一个list集合
		List<PmilaProduct> pmilaProducts = burgeonRestClient.query(PmilaProduct.class, start, pageSize,
				filterParamList, orderByParamList);
		// 创建一个帕米拉XE系统的款号档案集合 用于存储抓取过来的数据
		List<ProductEntity> productList = new ArrayList<>();
		// 判断抓取的款号档案是否为空
		if (CollectionUtils.isNotEmpty(pmilaProducts)) {
//			log.info("获取帕米拉商品响应：{}", pmilaProducts);
			// 遍历款号档案获取里面的元素
			pmilaProducts.forEach(p -> {
				// 创建对象用于和元素属性对应
				ProductEntity product = new ProductEntity();
				//BeanUtils.copyProperties(p, product);
				// 设置id为空 必须使用xe本地的id
				product.setId(null);
				product.setProductCode(p.getProductCode());
				product.setProductName(p.getProductName());
				product.setPreCost(p.getPreCost());
				product.setPriceList(p.getPriceList());
				// 品牌
				product.setBrandCode("P");
				product.setBrandName("名典");
				// 年份
				product.setYearCode(p.getYearCode());
				product.setYearName(p.getYearName());
				// 季节
				product.setSeasonCode(p.getSeasonCode());
				product.setSeasonName(p.getSeasonName());
				// 大类
				product.setBigClassCode(p.getBigClassCode());
				product.setBigClassName(p.getBigClassName());
				// 小类
				product.setSmallClassCode(p.getSmallClassCode());
				product.setSmallClassName(p.getSmallClassName());
				// 材质大类
				product.setMaterialBigClassCode(p.getMaterialBigClassCode());
				product.setMaterialBigClassName(p.getMaterialBigClassName());
				// 跟型
				product.setHeelTypeCode(p.getHeelTypeCode());
				product.setHeelTypeName(p.getHeelTypeName());
				// 产地
				product.setOriginPlaceCode(p.getOriginPlaceCode());
				product.setOriginPlaceName(p.getOriginPlaceName());
				// 质量等级
				product.setQualityGradeCode(p.getQualityGradeCode());
				product.setQualityGradeName(p.getQualityGradeName());
				// 执行标准
				product.setStandardCode(p.getStandardCode());
				product.setStandardName(p.getStandardName());
				// 尺寸组名称
				product.setSizeGroupName(p.getSizeGroupName());
				product.setSupplierCode("005");
				// 条码
				List<SkuEntity> skus = new ArrayList<>();
				p.getSkus().forEach(s -> {
					SkuEntity sku = new SkuEntity();
					BeanUtils.copyProperties(s, sku);
					sku.setId(null);
					sku.setProductId(null);
					skus.add(sku);
				});
				product.setSkus(skus);
				// 将每一个同步好的款号添加到xe系统的款号档案中
				productList.add(product);
			});
			addProduct(productList);
		}
		return productList;
	}

	@Override
	public List<AttributeEntity> syncPmilaAttribute(String startTime, String attributeName, int page, int pageSize) {
		int start = (page - 1) * pageSize;
		List<QueryFilterParam> filterParamList = new ArrayList<>();
		if (StringUtils.isNotEmpty(attributeName)) {
			filterParamList.add(new QueryFilterParam("NAME", attributeName, QueryFilterCombine.AND));
		}
		if (StringUtils.isNotEmpty(startTime)) {
			filterParamList.add(new QueryFilterParam("", "MODIFIEDDATE > to_date('" + startTime
					+ "', 'yyyy-mm-dd hh24:mi:ss')", QueryFilterCombine.AND));
		}
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));
		List<PmilaAttribute> pmilaAttributeList = burgeonRestClient.query(PmilaAttribute.class, start, pageSize, filterParamList, orderByParamList);
		ArrayList<AttributeEntity> attributeEntities = new ArrayList<>();
		for (PmilaAttribute pmilaAttribute : pmilaAttributeList) {
			AttributeEntity attributeEntity = new AttributeEntity();
			attributeEntity.setName(pmilaAttribute.getName());
			attributeEntity.setClrSize(pmilaAttribute.getClrSize());
			attributeEntities.add(attributeEntity);

			productDao.insertAttribute(attributeEntity);
			if (attributeEntity.getClrSize() == 2) {
				productDao.callSizeGroupAC(attributeEntity.getId());
			}
		}
		return attributeEntities;
	}
}
