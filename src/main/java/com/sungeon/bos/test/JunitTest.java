package com.sungeon.bos.test;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.model.ObjectOperateType;
import com.burgeon.framework.restapi.request.QueryFilterCombine;
import com.burgeon.framework.restapi.request.QueryFilterParam;
import com.burgeon.framework.restapi.request.QueryOrderByParam;
import com.burgeon.framework.restapi.response.ProcessOrderResponse;
import com.sungeon.bos.dao.IPurchaseDao;
import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.pmila.*;
import com.sungeon.bos.service.IPurchaseService;
import com.sungeon.bos.util.BurgeonRestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
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
		filterParamList.add(new QueryFilterParam("OUT_STATUS", "2", QueryFilterCombine.AND));
		filterParamList.add(new QueryFilterParam("IN_STATUS", "1", QueryFilterCombine.AND));
		filterParamList.add(new QueryFilterParam("C_DEST_ID", "445", QueryFilterCombine.AND));
//		if (StringUtils.isNotEmpty(docNo)) {
//			filterParamList.add(new QueryFilterParam("DOCNO", docNo, QueryFilterCombine.AND));
//		}

		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));
		// 获取品牌方的经销商采购单
		List<PmilaCuspurchase> cuspurchaseList = burgeonRestClient.query(PmilaCuspurchase.class, 1, 100, filterParamList, orderByParamList);
		for (PmilaCuspurchase pmilaCuspurchase : cuspurchaseList
		) {
			System.out.println(pmilaCuspurchase);
		}
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
		List<PmilaSize> sizes = burgeonRestClient.query(PmilaSize.class, 1, 100, null, orderByParamList);
		System.out.println(sizes);
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
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));
		List<PmilaProduct> pmilaProducts = burgeonRestClient.query(PmilaProduct.class, 1, 30000,
				null, orderByParamList);
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
//		for (PurchaseEntity purchaseEntity : purchaseEntities) {
//			PmilaCuspurchase pmilaCuspurchase = new PmilaCuspurchase();
//			BeanUtils.copyProperties(pmilaCuspurchase, purchaseEntity);
//			ProcessOrderResponse processOrderResponse = burgeonRestClient.processOrder(pmilaCuspurchase, ObjectOperateType.MODIFY);
//			System.out.println(JSONObject.toJSONString(processOrderResponse));
//		}
	}
}
