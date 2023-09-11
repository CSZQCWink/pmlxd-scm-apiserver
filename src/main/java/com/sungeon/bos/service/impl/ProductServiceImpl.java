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
import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
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
	private static String portal6162 = null;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Integer addProduct(List<ProductEntity> products) {
		if (org.springframework.util.CollectionUtils.isEmpty(products)) {
			return 0;
		}
		for (ProductEntity product : products) {
			if (StringUtils.isEmpty(product.getBrandName())) {
				throw new ParamNullException("品牌不能为空");
			}
			if (StringUtils.isEmpty(product.getSizeGroupName())) {
				throw new ParamNullException("尺码组不能为空");
			}
			ProductEntity pdt = productDao.queryProductByCode(product.getProductCode());
			Long brandId = productDao.queryDimId("DIM1", product.getBrandName());
			if (brandId == null) {
				throw new ParamNotMatchException("品牌[" + product.getBrandName() + "]不存在");
			}
			product.setBrandId(brandId);
			Long sizeGroupId;
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
			if (!StringUtils.isEmpty(product.getYearName())) {
				product.setYearId(getDimId("DIM5", product.getYearCode(), product.getYearName()));
			}
			if (!StringUtils.isEmpty(product.getSeasonName())) {
				product.setSeasonId(getDimId("DIM6", product.getSeasonCode(), product.getSeasonName()));
			}
			if (!StringUtils.isEmpty(product.getBigClassName())) {
				product.setBigClassId(getDimId("DIM17", product.getBigClassCode(), product.getBigClassName()));
			}
			if (StringUtils.isNotEmpty(product.getMidClassName())) {
				product.setMidClassId(getDimId("DIM18", product.getMidClassCode(), product.getMidClassName()));
			}
			if (StringUtils.isNotEmpty(product.getSmallClassName())) {
				product.setSmallClassId(getDimId("DIM19", product.getSmallClassCode(), product.getSmallClassName()));
			}
			if (!StringUtils.isEmpty(product.getGenderName())) {
				product.setGenderId(getDimId("DIM20", product.getGenderCode(), product.getGenderName()));
			}
			if (!StringUtils.isEmpty(product.getClassName())) {
				product.setClassId(getDimId("DIM2", product.getClassCode(), product.getClassName()));
			}
			if (!StringUtils.isEmpty(product.getBandName())) {
				product.setBandId(getDimId("DIM9", product.getBandCode(), product.getBandName()));
			}
			if (!StringUtils.isEmpty(product.getStandardName())) {
				product.setStandardId(getDimId("DIM15", product.getStandardCode(), product.getStandardName()));
			}
			if (!StringUtils.isEmpty(product.getSecurityCategoryName())) {
				product.setSecurityCategoryId(getDimId("DIM16", product.getSecurityCategoryCode(), product.getSecurityCategoryName()));
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
			productDao.updateProductMedia(product.getId(), product.getProductCode());
			if (null == colorGroupId) {
				colorGroupId = productDao.queryAttributeId(1, "颜色");
			}
			if (null == portal6162) {
				portal6162 = baseDao.getParamValue("portal.6162");
			}

			// 新增或修改条码
			if (CollectionUtils.isNotEmpty(product.getSkus())) {
				for (int i = 0; i < product.getSkus().size(); i++) {
					product.getSkus().get(i).setProductId(product.getId());
					product.getSkus().get(i).setProductCode(product.getProductCode());
					product.getSkus().get(i).setBrandId(brandId);
					product.getSkus().get(i).setSizeGroupId(sizeGroupId);
					// 获取ASI的条件是 尺寸组的id + 尺寸的id + 颜色的id
					Long asi = productDao.queryASI(product.getSizeGroupId(),
							product.getSkus().get(i).getSizeCode(),
							product.getSkus().get(i).getColorCode());
					product.getSkus().get(i).setAsiId(asi);

//					getAttributeValue(1, product.getSkus().get(i).getColorCode(), product.getSkus().get(i).getColorName(), brandId, colorGroupId);
//					getAttributeValue(2, product.getSkus().get(i).getSizeCode(), product.getSkus().get(i).getSizeName(), null, sizeGroupId);
				}
				productDao.insertSku(product.getSkus());
			}
		}
		portal6162 = null;
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
		AttributeValueEntity attributeValue = productDao.queryAttributeValue(clr, attributeValueCode, attributeId, brandId);
		if (null == attributeValue) {
			attributeValue = new AttributeValueEntity();
			attributeValue.setClr(clr);
			if(clr == 1){
				Long attributeId1 = productDao.queryAttributeId(clr, "颜色");
				attributeValue.setAttributeId(attributeId1);
			}else{
				attributeValue.setAttributeId(attributeId);
			}
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
		List<AttributeValueEntity> colorList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(colors)) {
			log.info("获取帕米拉颜色响应：{}", colors);
			Long brandId = productDao.queryDimId("DIM1", "名典");
			colors.forEach(d -> {
				AttributeValueEntity attributeValue = getAttributeValue(1, d.getCode(), d.getName(), brandId, 1498L);
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
			log.info("获取帕米拉尺寸响应：{}", sizes);

			for (int i = 0; i < sizes.size(); i++) {
				if (sizes.get(i).getSizeGroup() != null) {
					Long sizeGroupId;
					if ("均码".equals(sizes.get(i).getSizeGroup().getName()) || "00".equals(sizes.get(i).getSizeGroup().getName())) {
						sizeGroupId = productDao.queryAttributeId(2, "均码");
					} else {
						sizeGroupId = productDao.queryAttributeId(2, sizes.get(i).getSizeGroup().getName());
					}
					AttributeValueEntity attributeValue = getAttributeValue(2, sizes.get(i).getCode(), sizes.get(i).getName(), null, sizeGroupId);
					sizeList.add(attributeValue);
				}
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
		if (StringUtils.isNotEmpty(productCode)) {
			filterParamList.add(new QueryFilterParam("NAME", productCode, QueryFilterCombine.AND));
		}
		if (StringUtils.isNotEmpty(startTime)) {
			filterParamList.add(new QueryFilterParam("", "M_PRODUCT.CREATIONDATE >= to_date('" + startTime
					+ "', 'yyyy-mm-dd hh24:mi:ss')", QueryFilterCombine.AND));
		}
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));

		List<PmilaProduct> pmilaProducts = burgeonRestClient.query(PmilaProduct.class, start, pageSize,
				filterParamList, orderByParamList);
		List<ProductEntity> productList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(pmilaProducts)) {
			log.info("获取帕米拉商品响应：{}", pmilaProducts);
			pmilaProducts.forEach(p -> {
				ProductEntity product = new ProductEntity();
				//BeanUtils.copyProperties(p, product);
				product.setId(null);
				product.setProductCode(p.getProductCode());
				product.setProductName(p.getProductName());
				product.setPriceList(p.getPriceList());
				product.setBrandCode("P");
				product.setBrandName("名典");
				product.setYearCode(p.getYearCode());
				product.setSeasonCode(p.getSeasonCode());
				product.setSeasonName(p.getSeasonName());
				product.setBigClassCode(p.getBigClassCode());
				product.setBigClassName(p.getBigClassName());
				product.setClassCode(p.getClassCode());
				product.setClassName(p.getClassName());
				product.setMidClassCode(p.getMidClassCode());
				product.setMidClassName(p.getMidClassName());
				product.setSmallClassCode(p.getSmallClassCode());
				product.setSmallClassName(p.getSmallClassName());
				product.setGenderCode(p.getGenderCode());
				product.setGenderName(p.getGenderName());
				product.setBandCode(p.getBandCode());
				product.setBandName(p.getBandName());
				product.setStandardCode(p.getStandardCode());
				product.setStandardName(p.getStandardName());
				product.setSecurityCategoryCode(p.getSecurityCategoryCode());
				product.setSecurityCategoryName(p.getSecurityCategoryName());
				if ("均码".equals(p.getSizeGroupName()) || "00".equals(p.getSizeGroupName())) {
					product.setSizeGroupName("均码");
				} else {
					product.setSizeGroupName(p.getSizeGroupName());
				}
				product.setSupplierCode("005");

				List<SkuEntity> skus = new ArrayList<>();
				p.getSkus().forEach(s -> {
					SkuEntity sku = new SkuEntity();
					BeanUtils.copyProperties(s, sku);
					sku.setId(null);
					sku.setProductId(null);
					skus.add(sku);
				});
				product.setSkus(skus);
				productList.add(product);
			/*
			下载图片
				File img;
				if (StringUtils.isNotEmpty(p.getImageUrl())) {
					img = FileUtils.getFile(productImagePath + p.getProductCode() + ".jpg");
					if (!img.exists()) {
						HttpUtils.doDownload(pmilaUrl + p.getImageUrl(),
								productImagePath + p.getProductCode() + ".jpg");
					}
				}
				if (StringUtils.isNotEmpty(p.getMedia().getImgUrl1())) {
					img = FileUtils.getFile(productImagePath + p.getProductCode() + "_1.jpg");
					if (!img.exists()) {
						HttpUtils.doDownload(pmilaUrl + p.getMedia().getImgUrl1(),
								productImagePath + p.getProductCode() + "_1.jpg");
					}
				}
				if (StringUtils.isNotEmpty(p.getMedia().getImgUrl2())) {
					img = FileUtils.getFile(productImagePath + p.getProductCode() + "_2.jpg");
					if (!img.exists()) {
						HttpUtils.doDownload(pmilaUrl + p.getMedia().getImgUrl2(),
								productImagePath + p.getProductCode() + "_2.jpg");
					}
				}
				if (StringUtils.isNotEmpty(p.getMedia().getImgUrl3())) {
					img = FileUtils.getFile(productImagePath + p.getProductCode() + "_3.jpg");
					if (!img.exists()) {
						HttpUtils.doDownload(pmilaUrl + p.getMedia().getImgUrl3(),
								productImagePath + p.getProductCode() + "_3.jpg");
					}
				}
				if (StringUtils.isNotEmpty(p.getMedia().getImgUrl4())) {
					img = FileUtils.getFile(productImagePath + p.getProductCode() + "_4.jpg");
					if (!img.exists()) {
						HttpUtils.doDownload(pmilaUrl + p.getMedia().getImgUrl4(),
								productImagePath + p.getProductCode() + "_4.jpg");
					}
				}
				if (StringUtils.isNotEmpty(p.getMedia().getImgUrl5())) {
					img = FileUtils.getFile(productImagePath + p.getProductCode() + "_5.jpg");
					if (!img.exists()) {
						HttpUtils.doDownload(pmilaUrl + p.getMedia().getImgUrl5(),
								productImagePath + p.getProductCode() + "_5.jpg");
					}
				}
				*/
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
			attributeEntity.setId(pmilaAttribute.getId());
			attributeEntity.setName(pmilaAttribute.getName());
			attributeEntity.setClrSize(pmilaAttribute.getClrSize());
			attributeEntities.add(attributeEntity);

			productDao.insertAttribute(attributeEntity);
		}
		return attributeEntities;
	}
}
