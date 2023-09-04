package com.sungeon.bos.test;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.model.ObjectOperateType;
import com.burgeon.framework.restapi.response.ObjectCreateResponse;
import com.burgeon.framework.restapi.response.ObjectModifyResponse;
import com.burgeon.framework.restapi.response.ProcessOrderResponse;
import com.sungeon.bos.entity.pmila.PmilaIn;
import com.sungeon.bos.entity.pmila.PmilaInItem;
import com.sungeon.bos.util.BurgeonRestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.transform.Source;
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

}
