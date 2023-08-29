package com.sungeon.bos.entity.request;

import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.core.model.AuthHolder;
import com.sungeon.bos.entity.base.VipEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class VipAddRequest extends AuthHolder {


    private static final long serialVersionUID = 1L;

    private List<VipEntity> vips;

    @Override
    public String getApiMethod() {
        return "Burgeon.Bos.VIP.Add";
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
