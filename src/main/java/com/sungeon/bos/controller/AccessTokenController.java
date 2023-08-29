package com.sungeon.bos.controller;

import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.core.application.SungeonBaseController;
import com.sungeon.bos.core.constants.ValueCode;
import com.sungeon.bos.core.exception.SungeonException;
import com.sungeon.bos.core.model.ValueHolder;
import com.sungeon.bos.core.utils.RandomUtils;
import com.sungeon.bos.entity.AccessToken;
import com.sungeon.bos.service.IBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Slf4j
@Controller
public class AccessTokenController extends SungeonBaseController {

    @Value("${ExpireIn}")
    private String expireIn;
    @Value("${AppId}")
    private String localAppId;
    @Value("${AppSecret}")
    private String localAppSecret;
    @Autowired
    private IBaseService baseService;

    @RequestMapping(value = "/accessToken/get", method = RequestMethod.GET)
    @ResponseBody
    public ValueHolder<JSONObject> getAccessToken(String appId, String appSecret) {
        JSONObject json = new JSONObject();
        if (null == appId) {
            return ValueHolder.build(ValueCode.PARAM_MISS_ERROR.getCode(), "缺少appId参数", null);
        }
        if (null == appSecret) {
            return ValueHolder.build(ValueCode.PARAM_MISS_ERROR.getCode(), "缺少appSecret参数", null);
        }
        if (!appId.equals(localAppId)) {
            return ValueHolder.build(ValueCode.AUTH_APPID_ERROR, null);
        }
        if (!appSecret.equals(localAppSecret)) {
            return ValueHolder.build(ValueCode.AUTH_APPSECRET_ERROR, null);
        }
        AccessToken accessToken = baseService.getAccessToken(appId);
        Date now = new Date();
        if (null == accessToken) {
            accessToken = new AccessToken();
        }
        accessToken.setAppId(appId);
        accessToken.setAppSecret(appSecret);
        accessToken.setAccessToken(RandomUtils.getRandomString(64, true));
        accessToken.setExpireIn(Integer.parseInt(expireIn));
        accessToken.setDateBegin(now);
        accessToken.setDateEnd(getBeforeTime(now, Integer.parseInt(expireIn) / 60));
        baseService.addAccessToken(accessToken);
        json.put("accessToken", accessToken.getAccessToken());
        json.put("expireIn", expireIn);
        return ValueHolder.build(ValueCode.SUCCESS.getCode(), "Access-Token获取成功", json);
    }

    @RequestMapping(value = "/accessToken/verify", method = RequestMethod.GET)
    @ResponseBody
    public ValueHolder<Void> verifyAccessToken(String appId, String accessToken) {
        if (!appId.equals(localAppId)) {
            return ValueHolder.build(ValueCode.AUTH_APPID_ERROR, null);
        }
        try {
            if (baseService.verifyAccessToken(appId, accessToken)) {
                return ValueHolder.build(ValueCode.SUCCESS.getCode(), "Access-Token正确", null);
            } else {
                return ValueHolder.build(ValueCode.AUTH_ACCESS_TOKEN_ERROR, null);
            }
        } catch (SungeonException e) {
            return ValueHolder.build(e.getValueCode().getCode(), e.getMessage(), null);
        }
    }

    public static Date getBeforeTime(Date date, Integer minutes) {
        Calendar time = Calendar.getInstance();
        time.setTime(date);
        time.add(Calendar.MINUTE, minutes);
        return time.getTime();
    }

}
