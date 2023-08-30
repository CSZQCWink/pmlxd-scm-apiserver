package com.sungeon.bos.service.impl;

import com.burgeon.framework.restapi.request.QueryFilterCombine;
import com.burgeon.framework.restapi.request.QueryFilterParam;
import com.burgeon.framework.restapi.request.QueryOrderByParam;
import com.sungeon.bos.core.exception.ParamNotMatchException;
import com.sungeon.bos.core.exception.ParamNullException;
import com.sungeon.bos.core.utils.CollectionUtils;
import com.sungeon.bos.core.utils.FileUtils;
import com.sungeon.bos.core.utils.HttpUtils;
import com.sungeon.bos.core.utils.StringUtils;
import com.sungeon.bos.dao.IBaseDao;
import com.sungeon.bos.dao.IProductDao;
import com.sungeon.bos.dao.ISupplierDao;
import com.sungeon.bos.entity.base.AttributeValueEntity;
import com.sungeon.bos.entity.base.DimEntity;
import com.sungeon.bos.entity.base.ProductEntity;
import com.sungeon.bos.entity.base.SkuEntity;
import com.sungeon.bos.entity.pmila.PmilaColor;
import com.sungeon.bos.entity.pmila.PmilaDim;
import com.sungeon.bos.entity.pmila.PmilaProduct;
import com.sungeon.bos.entity.pmila.PmilaSize;
import com.sungeon.bos.service.IProductService;
import com.sungeon.bos.util.BurgeonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
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
				product.getSkus().forEach(sku -> {
					sku.setProductId(product.getId());
					sku.setProductCode(product.getProductCode());
					sku.setBrandId(brandId);
					sku.setSizeGroupId(sizeGroupId);
					getAttributeValue(1, sku.getColorCode(), sku.getColorName(), portal6162.equals("true")
							? brandId : null, colorGroupId);
					getAttributeValue(2, sku.getSizeCode(), sku.getSizeName(), null, sizeGroupId);
				});
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
	public List<DimEntity> syncBsijaDim(String dimFlag, String zjhDimFlag, boolean isUseCode, String startTime,
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
			log.info("获取毕厶迦属性值响应：{}", dims);
			dims.forEach(d -> {
				DimEntity dim = new DimEntity();
				dim.setCode(isUseCode ? d.getCode() : d.getName());
				dim.setName(d.getName());
				dim.setDimFlag(zjhDimFlag);
				Long dimId;
				if ("DIM16".equals(zjhDimFlag)) {
					dimId = productDao.queryDimId(zjhDimFlag, dim.getCode());
				} else {
					dimId = productDao.queryDimId(zjhDimFlag, dim.getName());
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
	public List<AttributeValueEntity> syncBsijaColor(String startTime, String colorName, int page, int pageSize) {
		int start = (page - 1) * pageSize;
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

		List<PmilaColor> colors = burgeonRestClient.query(PmilaColor.class, start, pageSize, filterParamList,
				orderByParamList);
		List<AttributeValueEntity> colorList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(colors)) {
			log.info("获取毕厶迦颜色响应：{}", colors);
			Long brandId = productDao.queryDimId("DIM1", "B");
			colors.forEach(d -> {
				AttributeValueEntity attributeValue = getAttributeValue(1, d.getCode(), d.getName(), brandId, 1498L);
				colorList.add(attributeValue);
			});
		}
		return colorList;
	}

	@Override
	public List<AttributeValueEntity> syncBsijaSize(String startTime, String sizeName, int page, int pageSize) {
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

		List<PmilaSize> sizes = burgeonRestClient.query(PmilaSize.class, start, pageSize, filterParamList,
				orderByParamList);
		List<AttributeValueEntity> sizeList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(sizes)) {
			log.info("获取毕厶迦尺寸响应：{}", sizes);
			sizes.forEach(d -> {
				Long sizeGroupId;
				if ("均码".equals(d.getSizeGroup().getName()) || "00".equals(d.getSizeGroup().getName())) {
					sizeGroupId = productDao.queryAttributeId(2, "均码");
				} else {
					sizeGroupId = productDao.queryAttributeId(2, "BSIJA-" + d.getSizeGroup().getName());
				}
				AttributeValueEntity attributeValue = getAttributeValue(2, d.getCode(), d.getName(), null,
						sizeGroupId);
				sizeList.add(attributeValue);
			});
		}
		return sizeList;
	}

	@Value("${Scm.Bsija.Api.Url}")
	private String bsijaUrl;
	@Value("${Portal.Product.Image.Path}")
	private String productImagePath;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<ProductEntity> syncBsijaProduct(String startTime, String productCode, int page, int pageSize) {
		int start = (page - 1) * pageSize;
		List<QueryFilterParam> filterParamList = new ArrayList<>();
		if (StringUtils.isNotEmpty(productCode)) {
			filterParamList.add(new QueryFilterParam("NAME", productCode, QueryFilterCombine.AND));
		}
		if (StringUtils.isNotEmpty(startTime)) {
			filterParamList.add(new QueryFilterParam("", "M_PRODUCT.MODIFIEDDATE > to_date('" + startTime
					+ "', 'yyyy-mm-dd hh24:mi:ss')", QueryFilterCombine.AND));
		}
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));

		List<PmilaProduct> bsijaProducts = burgeonRestClient.query(PmilaProduct.class, start, pageSize,
				filterParamList, orderByParamList);
		List<ProductEntity> productList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(bsijaProducts)) {
			log.info("获取毕厶迦商品响应：{}", bsijaProducts);
			bsijaProducts.forEach(p -> {
				ProductEntity product = new ProductEntity();
				BeanUtils.copyProperties(p, product);
				product.setId(null);
				product.setBrandCode("B");
				product.setBrandName("毕厶迦");
				product.setYearCode(p.getYearName());
				product.setGenderCode(p.getGenderName());
				product.setSmallClassCode(p.getClassCode());
				product.setSmallClassName(p.getClassName());
				if ("均码".equals(p.getSizeGroupName()) || "00".equals(p.getSizeGroupName())) {
					product.setSizeGroupName("均码");
				} else {
					product.setSizeGroupName("BSIJA-" + p.getSizeGroupName());
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

				// 下载图片
				File img;
				if (StringUtils.isNotEmpty(p.getImageUrl())) {
					img = FileUtils.getFile(productImagePath + p.getProductCode() + ".jpg");
					if (!img.exists()) {
						HttpUtils.doDownload(bsijaUrl + p.getImageUrl(),
								productImagePath + p.getProductCode() + ".jpg");
					}
				}
				if (StringUtils.isNotEmpty(p.getMedia().getImgUrl1())) {
					img = FileUtils.getFile(productImagePath + p.getProductCode() + "_1.jpg");
					if (!img.exists()) {
						HttpUtils.doDownload(bsijaUrl + p.getMedia().getImgUrl1(),
								productImagePath + p.getProductCode() + "_1.jpg");
					}
				}
				if (StringUtils.isNotEmpty(p.getMedia().getImgUrl2())) {
					img = FileUtils.getFile(productImagePath + p.getProductCode() + "_2.jpg");
					if (!img.exists()) {
						HttpUtils.doDownload(bsijaUrl + p.getMedia().getImgUrl2(),
								productImagePath + p.getProductCode() + "_2.jpg");
					}
				}
				if (StringUtils.isNotEmpty(p.getMedia().getImgUrl3())) {
					img = FileUtils.getFile(productImagePath + p.getProductCode() + "_3.jpg");
					if (!img.exists()) {
						HttpUtils.doDownload(bsijaUrl + p.getMedia().getImgUrl3(),
								productImagePath + p.getProductCode() + "_3.jpg");
					}
				}
				if (StringUtils.isNotEmpty(p.getMedia().getImgUrl4())) {
					img = FileUtils.getFile(productImagePath + p.getProductCode() + "_4.jpg");
					if (!img.exists()) {
						HttpUtils.doDownload(bsijaUrl + p.getMedia().getImgUrl4(),
								productImagePath + p.getProductCode() + "_4.jpg");
					}
				}
				if (StringUtils.isNotEmpty(p.getMedia().getImgUrl5())) {
					img = FileUtils.getFile(productImagePath + p.getProductCode() + "_5.jpg");
					if (!img.exists()) {
						HttpUtils.doDownload(bsijaUrl + p.getMedia().getImgUrl5(),
								productImagePath + p.getProductCode() + "_5.jpg");
					}
				}
			});
			addProduct(productList);
		}
		return productList;
	}

}
