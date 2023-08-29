package com.sungeon.bos.entity.request;

import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.core.model.AuthHolder;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
public class RetailGetRequest extends AuthHolder {


    private static final long serialVersionUID = 1L;

    @Override
    public String getApiMethod() {
        return "Burgeon.Bos.Retail.Get";
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
