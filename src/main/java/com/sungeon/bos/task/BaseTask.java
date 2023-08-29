package com.sungeon.bos.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.annotation.SgSendLog;
import com.sungeon.bos.annotation.SgSendLogBean;
import com.sungeon.bos.annotation.SgSendLogRequest;
import com.sungeon.bos.annotation.SgSendLogUrl;
import com.sungeon.bos.core.constants.Constants;
import com.sungeon.bos.core.model.HttpResponse;
import com.sungeon.bos.core.utils.DateTimeUtils;
import com.sungeon.bos.core.utils.EncryptUtils;
import com.sungeon.bos.core.utils.HttpUtils;
import com.sungeon.bos.core.utils.RandomUtils;
import com.sungeon.bos.entity.InterfaceLog;
import com.sungeon.bos.entity.InterfaceType;
import com.sungeon.bos.service.IBaseService;
import com.sungeon.bos.util.SystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.HttpURLConnection;

/**
 * @author : 刘国帅
 * @date : 2016-12-6
 */
@Slf4j
@Component
public class BaseTask {

    @Resource
    protected IBaseService baseService;
    @Value("${Param.InterfaceLog.Status}")
    protected String InterfaceLogStatus;

    @SgSendLogUrl
    @Value("${Param.Propel.URL}")
    public String propelUrl;

    @SgSendLog(successName = "code", successValue = "1")
    public JSONObject httpRequest(String method, @SgSendLogRequest JSONObject request, @SgSendLogBean InterfaceLog interfaceLog) {
        JSONObject response = new JSONObject();
        response.put("code", 1);
        response.put("message", "调用WMS接口成功");
        return response;
    }

    protected static JSONObject yunhuanHttpRequest(String method, JSON jsonData) throws Exception {
        String nonceStr = RandomUtils.getRandomString(32, true);
        String timestamp = DateTimeUtils.now();
        String sign = EncryptUtils.md5(SystemProperties.AppId + SystemProperties.AppSecret + nonceStr + timestamp
                + method + jsonData.toString(), false);
        String url = SystemProperties.ParamPropelURL + "&method=" + method + "&timestamp=" + timestamp + "&noncestr="
                + nonceStr + "&appid=" + SystemProperties.AppId + "&sign=" + sign;
        // 发送请求
        HttpResponse result = HttpUtils.doPostWithJson(url, jsonData.toString());
        // 处理响应-转换为JSON格式
        if (result.getCode() == HttpURLConnection.HTTP_OK) {
            return JSONObject.parseObject(result.getBody().trim().replace("\n", "").replace("\r", ""));
        } else {
            JSONObject resp = new JSONObject();
            log.error("请求异常或返回信息异常：" + result.getMessage());
            resp.put("Code", 0);
            resp.put("Desc", "请求异常或返回信息异常");
            return resp;
        }
    }

    protected JSONObject httpRequest(String param) throws Exception {
        HttpResponse result = HttpUtils.doPostWithForm(SystemProperties.ParamPropelURL, param);
        if (result.getCode() == HttpURLConnection.HTTP_OK) {
            return JSONObject.parseObject(result.getBody().trim().replace("\n", "").replace("\r", ""));
        } else {
            JSONObject resp = new JSONObject();
            log.error("请求异常或返回信息异常：" + result.getMessage());
            resp.put("Code", 0);
            resp.put("Desc", "请求异常或返回信息异常");
            return resp;
        }
    }

    protected Integer addInterfaceLog(String name, String method, String data, String source, Long sourceId,
                                      JSONObject resp) {
        if (InterfaceLogStatus.equals(Constants.BURGEON_YES)) {
            InterfaceLog interfaceLog = new InterfaceLog(InterfaceType.SEND, "", method);
            interfaceLog.setName(name);
            interfaceLog.setData(data);
            interfaceLog.setSource(source);
            interfaceLog.setSourceId(sourceId);
            interfaceLog.setRequestId(RandomUtils.getRandomString(16, true));
            interfaceLog.setResult(resp.getInteger("code") == 0 ? Constants.BURGEON_YES : Constants.BURGEON_FAIL);
            interfaceLog.setResultMessage(resp.toJSONString());
            return baseService.addInterfaceLog(interfaceLog);
        } else {
            return 0;
        }
    }

}
