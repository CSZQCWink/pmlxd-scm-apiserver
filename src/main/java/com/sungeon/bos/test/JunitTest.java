package com.sungeon.bos.test;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.model.ObjectOperateType;
import com.burgeon.framework.restapi.request.QueryFilterCombine;
import com.burgeon.framework.restapi.request.QueryFilterParam;
import com.burgeon.framework.restapi.request.QueryOrderByParam;
import com.burgeon.framework.restapi.response.ProcessOrderResponse;
import com.sungeon.bos.core.utils.CollectionUtils;
import com.sungeon.bos.dao.IProductDao;
import com.sungeon.bos.dao.IPurchaseDao;
import com.sungeon.bos.entity.base.AttributeEntity;
import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import com.sungeon.bos.entity.pmila.*;
import com.sungeon.bos.service.IProductService;
import com.sungeon.bos.service.IPurchaseService;
import com.sungeon.bos.util.BurgeonRestClient;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 刘国帅
 * @date 2023-9-4
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class JunitTest {

	@Autowired
	private BurgeonRestClient burgeonRestClient;
	@Autowired
	private IPurchaseDao purchaseDao;
	@Autowired
	private IPurchaseService purchaseService;
	@Autowired
	private IProductDao productDao;

	@Autowired
	private IProductService productService;

	/**
	 * @title: test
	 * @author: 刘国帅
	 * @description: 测试回写功能
	 * @param: []
	 * @return: void
	 * @date: 2023/9/7 9:36
	 **/
	@Test
	public void test() {
		PmilaIn in = new PmilaIn();
		in.setId(25874L);
		in.setDocNo("TF2308290000001");
		in.setDateIn(20230903);
		List<PmilaInItem> items = new ArrayList<>();
		PmilaInItem item = new PmilaInItem();
		// item.setId(290274L);
		// item.setInId(25874L);
		item.setProductAdd("11111004"); // 条码
		item.setQtyIn(1);
		items.add(item);
		in.setItems(items);

		System.out.println(JSONObject.toJSONString(in));
		ProcessOrderResponse resp = burgeonRestClient.processOrder(in, ObjectOperateType.MODIFY);
		System.out.println(JSONObject.toJSONString(resp));
	}


	/**
	 * @title: testPurchase
	 * @author: 陈苏洲
	 * @description: 查询经销商采购单
	 * @param: []
	 * @return: void
	 * @date: 2023/9/6 11:32
	 **/
	@Test
	public void testPurchase() {
		List<QueryFilterParam> filterParamList = new ArrayList<>();
//		filterParamList.add(new QueryFilterParam("OUT_STATUS", "2", QueryFilterCombine.AND));
		filterParamList.add(new QueryFilterParam("IN_STATUS", "2", QueryFilterCombine.AND));
		filterParamList.add(new QueryFilterParam("C_DEST_ID", "445", QueryFilterCombine.AND));
//		filterParamList.add(new QueryFilterParam("DOCNO", "SA2308020000008", QueryFilterCombine.AND));
//		if (StringUtils.isNotEmpty(docNo)) {
//			filterParamList.add(new QueryFilterParam("DOCNO", docNo, QueryFilterCombine.AND));
//		}

		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));
		// 获取品牌方的经销商采购单
		List<PmilaCuspurchase> cuspurchaseList = burgeonRestClient.query(PmilaCuspurchase.class, 1, 100, filterParamList, orderByParamList);
		System.out.println(cuspurchaseList.size());
	}

	/**
	 * @title: testSize
	 * @author: 陈苏洲
	 * @description: 查询尺寸
	 * @param: []
	 * @return: void
	 * @date: 2023/9/6 11:31
	 **/
	@Test
	public void testSize() {
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));
		List<PmilaSize> sizes = burgeonRestClient.query(PmilaSize.class, 1, 1000, null, orderByParamList);
		if (CollectionUtils.isNotEmpty(sizes)) {
			// 创建一个Map集合用来存储尺寸组id和尺寸组的信息
			HashMap<Long, PmilaSizeGroup> map = new HashMap<>();
//			log.info("获取帕米拉尺寸响应：{}", sizes);
			for (int i = 0; i < sizes.size(); i++) {
				if (sizes.get(i).getSizeGroup() != null) {
					map.put(sizes.get(i).getSizeGroupId(),sizes.get(i).getSizeGroup());
				}
//				Set<Long> sizeGroupIdList = map.keySet();
//				for (Long sizeGroupId : sizeGroupIdList) {
//					PmilaSizeGroup pmilaSizeGroup = map.get(sizeGroupId);
//					Long attributeId = productDao.queryAttributeId(2, pmilaSizeGroup.getName());
//					AttributeValueEntity attributeValue = getAttributeValue(2, sizes.get(i).getCode(), sizes.get(i).getName(), null, attributeId);
//					sizeList.add(attributeValue);
//				}
			}
			System.out.println(map.keySet().size());
		}
	}

	/**
	 * @title: testProduct
	 * @author: 陈苏洲
	 * @description: 查询款号档案
	 * @param: []
	 * @return: void
	 * @date: 2023/9/6 11:33
	 **/
	@Test
	public void testProduct(){
		List<QueryFilterParam> filterParamList = new ArrayList<>();
		filterParamList.add(new QueryFilterParam("NAME", "Q2333597", QueryFilterCombine.AND));
//		if (StringUtils.isNotEmpty(productCode)) {
//			filterParamList.add(new QueryFilterParam("NAME", productCode, QueryFilterCombine.AND));
//		}

		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));
		List<PmilaProduct> pmilaProducts = burgeonRestClient.query(PmilaProduct.class, 1, 100,
				filterParamList, orderByParamList);
		System.out.println(pmilaProducts.size());
	}

	/**
	 * @title: testColor
	 * @author: 陈苏洲
	 * @description: 查询颜色
	 * @param: []
	 * @return: void
	 * @date: 2023/9/7 9:28
	 **/
	@Test
	public void testColor(){
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));
		List<PmilaColor> colors = burgeonRestClient.query(PmilaColor.class, 1, 1000, null,
				orderByParamList);
		System.out.println(colors.size());
	}

	/**
	 * @title: testDim
	 * @author: 陈苏洲
	 * @description: 查询属性
	 * @param: []
	 * @return: void
	 * @date: 2023/9/7 9:28
	 **/
	@Test
	public void testDim(){
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));
		List<PmilaDim> dims = burgeonRestClient.query(PmilaDim.class, 1, 1000, null, orderByParamList);
		System.out.println(dims.size());
	}

	/**
	 * @title: testPurchaseReturn
	 * @author: 陈苏洲
	 * @description: 查询采购退货单
	 * @param: []
	 * @return: void
	 * @date: 2023/9/7 9:28
	 **/
	@Test
	public void testPurchaseReturn(){
		List<QueryFilterParam> filterParamList = new ArrayList<>();
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));

		List<PmilaCuspurchaseReturn> purchasesReturn = burgeonRestClient.query(PmilaCuspurchaseReturn.class, 1, 1000, filterParamList, orderByParamList);
		System.out.println(purchasesReturn.size());
	}


	/**
	 * @title: testWbPurchase
	 * @author: 陈苏洲
	 * @description: 查询所有的符合条件的采购单
	 * @param: []
	 * @return: void
	 * @date: 2023/9/7 9:29
	 **/
	@Test
	public void testWbPurchase(){
		List<PurchaseEntity> purchaseEntities = purchaseDao.queryPurchase();
		System.out.println(purchaseEntities.size());
	}

	/**
	 * @title: testWbPurchaseReturn
	 * @author: 陈苏洲
	 * @description: 测试查询采购退货单
	 * @param: []
	 * @return: void
	 * @date: 2023/9/8 9:45
	 **/
	@Test
	public void testWbPurchaseReturn(){
		List<PurchaseReturnEntity> purchaseReturnEntityList = purchaseDao.queryPurchaseReturn();
		System.out.println(purchaseReturnEntityList.size());
	}

	/**
	 * @title: testAttributes
	 * @author: 陈苏洲
	 * @description: 查询所有的特征值 包含尺寸组和颜色组
	 * @param: []
	 * @return: void
	 * @date: 2023/9/14 9:46
	 **/
	@Test
	public void testAttributes(){
		List<AttributeEntity> attributeEntities = productDao.queryAttribute();
		System.out.println(attributeEntities.size());
	}

	/**
	 * @title: testQueryId
	 * @author: 陈苏洲
	 * @description: 查找店仓id 和 供应商id
	 * @param: []
	 * @return: void
	 * @date: 2023/9/14 9:47
	 **/
	@Test
	public void testQueryId(){
		Long storeId = purchaseDao.queryStoreId("515200101","盐城仓库");
		System.out.println("收货店仓id ------> "+storeId);
		// 供应商id
		Long supplierId = purchaseDao.querySupplierId("温州外购仓");
		System.out.println("供应商id"+supplierId);
	}

	/**
	 * @title: testQueryProductByDIM2
	 * @author: 陈苏洲
	 * @description: 获取所有年份为2023的款号档案
	 * @param: []
	 * @return: void
	 * @date: 2023/9/18 16:45
	 **/
	@Test
	public void testQueryProductByDIM2(){
		List<QueryFilterParam> filterParamList = new ArrayList<>();
		filterParamList.add(new QueryFilterParam("M_DIM2_ID;ATTRIBNAME", "2023", QueryFilterCombine.AND));
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));
		List<PmilaProduct> pmilaProducts = burgeonRestClient.query(PmilaProduct.class, 1, 2000,
				filterParamList, orderByParamList);
		System.out.println(pmilaProducts.size());
	}

}
