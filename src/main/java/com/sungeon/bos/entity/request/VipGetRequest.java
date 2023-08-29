package com.sungeon.bos.entity.request;

import java.util.List;

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
public class VipGetRequest extends AuthHolder {


    private static final long serialVersionUID = 1L;

    private List<String> mobiles;
    private Integer page;
    private Integer pageSize;

    @Override
    public String getApiMethod() {
        return "Burgeon.Bos.VIP.Get";
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
