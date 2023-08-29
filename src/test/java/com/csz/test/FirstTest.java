package com.csz.test;

import com.burgeon.framework.restapi.RestfulException;
import com.sungeon.bos.util.BurgeonRestClient;
import org.junit.Test;

/**
 * @BelongsPackage: com.csz.test
 * @ClassName: FirstTest
 * @Author: QC_Wink
 * @Description: 第一次接口测试
 * @CreateTime: 2023-08-29 11:31
 * @Version: 1.0
 */

public class FirstTest {

    @Test
    public void LoginTest() throws RestfulException {
        BurgeonRestClient burgeonRestClient = new BurgeonRestClient(
             "http://47.102.144.188:90","zzn@mdyc.com","zhaozinan123"
        );
        burgeonRestClient.login();
    }
}
