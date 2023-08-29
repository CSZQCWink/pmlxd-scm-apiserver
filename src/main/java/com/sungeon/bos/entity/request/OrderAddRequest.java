package com.sungeon.bos.entity.request;

import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.core.model.AuthHolder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderAddRequest extends AuthHolder {


    private static final long serialVersionUID = 1L;

    @Override
    public String getApiMethod() {
        return "Burgeon.Bos.Order.Add";
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
