package com.sungeon.bos.aop;

import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.annotation.*;
import com.sungeon.bos.core.constants.Constants;
import com.sungeon.bos.core.exception.ParamNullException;
import com.sungeon.bos.core.model.AuthHolder;
import com.sungeon.bos.core.model.ValueHolder;
import com.sungeon.bos.core.utils.StringUtils;
import com.sungeon.bos.entity.InterfaceLog;
import com.sungeon.bos.entity.InterfaceType;
import com.sungeon.bos.service.IBaseService;
import com.sungeon.bos.util.SystemProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author 刘国帅
 * @date 2023-1-6
 **/
@Component
@Aspect
public class SungeonInterfaceLogAspect {

	private static final Logger log = LoggerFactory.getLogger(SungeonInterfaceLogAspect.class);

	private final IBaseService baseService;

	@Autowired
	public SungeonInterfaceLogAspect(IBaseService baseService) {
		this.baseService = baseService;
	}

	/**
	 * SgReceiveLog-Aspect切入点
	 */
	@Pointcut("@annotation(com.sungeon.bos.annotation.SgReceiveLog)")
	private void doReceivePoint() {
	}

	/**
	 * SgSendLog-Aspect切入点
	 */
	@Pointcut("@annotation(com.sungeon.bos.annotation.SgSendLog)")
	private void doSendPoint() {
	}

	/**
	 * SgReceiveLog-环绕处理
	 *
	 * @param joinPoint JoinPoint
	 * @param field     SgLog
	 */
	@Around("doReceivePoint() && @annotation(field)")
	public Object doReceiveAroundPoint(ProceedingJoinPoint joinPoint, SgReceiveLog field) {
		Object[] params = joinPoint.getArgs();
		if (params.length == 0) {
			throw new ParamNullException("无请求参数");
		}
		AuthHolder ah;
		InterfaceLog interfaceLog = new InterfaceLog(InterfaceType.RECEIVE);
		interfaceLog.setApplication(SystemProperties.ScheduleGroup);
		interfaceLog.setName(field.name());
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		Annotation[] annotations = method.getAnnotations();
		String url = null;
		for (Annotation annotation : annotations) {
			if (annotation instanceof RequestMapping) {
				RequestMapping mapping = (RequestMapping) annotation;
				url = (StringUtils.isNotEmpty(mapping.value()[0]) ? mapping.value()[0] : mapping.path()[0]);
			}
			if (annotation instanceof PostMapping) {
				PostMapping mapping = (PostMapping) annotation;
				url = (StringUtils.isNotEmpty(mapping.value()[0]) ? mapping.value()[0] : mapping.path()[0]);
			}
			if (annotation instanceof GetMapping) {
				GetMapping mapping = (GetMapping) annotation;
				url = (StringUtils.isNotEmpty(mapping.value()[0]) ? mapping.value()[0] : mapping.path()[0]);
			}
		}
		interfaceLog.setUrl(url);
		for (Object param : params) {
			if (param instanceof AuthHolder) {
				ah = (AuthHolder) param;
				interfaceLog.setRequestId(ah.getRequestId());
				interfaceLog.setMethod(ah.getApiMethod());
				interfaceLog.setData(field.contentType().equals(Constants.FORMAT_JSON.toUpperCase())
						? ah.toJSONString() : ah.toXMLString());
			}
		}
		Object data = null;
		Object result = null;
		ValueHolder<?> vh;
		try {
			// 执行
			result = joinPoint.proceed();
			vh = (ValueHolder<?>) result;
			data = vh.getData();

			interfaceLog.setResult(vh.isSuccess() ? Constants.BURGEON_YES : Constants.BURGEON_FAIL);
			interfaceLog.setResultMessage(field.contentType().equals(Constants.FORMAT_JSON.toUpperCase())
					? vh.toJSONString() : vh.toXMLString());
			interfaceLog.setErrorMessage(vh.isSuccess() ? "" : vh.getMessage());
		} catch (Throwable e) {
			log.info(e.getMessage(), e);
			interfaceLog.setResult(Constants.BURGEON_FAIL);
			interfaceLog.setResultMessage(e.getMessage());
			interfaceLog.setErrorMessage(e.getMessage());
		}
		if (null != data) {
			try {
				method = data.getClass().getMethod("getId");
				Object id = method.invoke(data);
				if (id instanceof Long) {
					interfaceLog.setSourceId((Long) id);
				} else if (id instanceof Integer) {
					interfaceLog.setSourceId(((Integer) id).longValue());
				}
			} catch (Exception e) {
				interfaceLog.setSourceId(null);
			}
			try {
				method = data.getClass().getMethod("get" + toCamelStyle(field.sourceColumn()));
				interfaceLog.setSource((String) method.invoke(data));
			} catch (Exception e) {
				interfaceLog.setSource(null);
			}
		}
		baseService.addInterfaceLog(interfaceLog);
		return result;
	}

	/**
	 * SgSendLog-环绕处理
	 *
	 * @param joinPoint JoinPoint
	 * @param field     SgLog
	 */
	@Around("doSendPoint() && @annotation(field)")
	public Object doSendAroundPoint(ProceedingJoinPoint joinPoint, SgSendLog field) {
		Object[] params = joinPoint.getArgs();
		if (params.length == 0) {
			throw new ParamNullException("无请求参数");
		}
		InterfaceLog interfaceLog = null;
		Object data = null;
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			Annotation[] annotations = parameter.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof SgSendLogBean) {
					interfaceLog = (InterfaceLog) params[i];
				} else if (annotation instanceof SgSendLogRequest) {
					data = params[i];
				}
			}
		}

		if (null == interfaceLog) {
			interfaceLog = new InterfaceLog(InterfaceType.SEND);
			interfaceLog.setApplication(SystemProperties.ScheduleGroup);
			interfaceLog.setName(field.name());
		}

		Object result = null;
		try {
			String url = null;
			Field[] fields = joinPoint.getTarget().getClass().getDeclaredFields();
			for (Field f : fields) {
				SgSendLogUrl annotation = f.getAnnotation(SgSendLogUrl.class);
				if (null != annotation) {
					url = (String) f.get(joinPoint.getTarget());
					break;
				}
			}
			interfaceLog.setUrl(url);
			interfaceLog.setData(null != data ? data.toString() : null);

			// 执行
			result = joinPoint.proceed();

			boolean isSuccess = true;
			if (field.contentType().equals(Constants.FORMAT_JSON.toUpperCase())) {
				JSONObject response = JSONObject.parseObject(result.toString());
				isSuccess = response.getString(field.successName()).equals(field.successValue());
			}
			interfaceLog.setResult(isSuccess ? Constants.BURGEON_YES : Constants.BURGEON_FAIL);
			interfaceLog.setResultMessage(result.toString());
			interfaceLog.setErrorMessage(null);
		} catch (Throwable e) {
			log.info(e.getMessage(), e);
			interfaceLog.setResult(Constants.BURGEON_FAIL);
			interfaceLog.setResultMessage(e.getMessage());
			interfaceLog.setErrorMessage(e.getMessage());
		}
		baseService.addInterfaceLog(interfaceLog);
		return result;
	}


	private static String toCamelStyle(String name) {
		StringBuilder newName = new StringBuilder();
		int len = name.length();
		for (int i = 0; i < len; i++) {
			char c = name.charAt(i);
			if (i == 0) {
				newName.append(Character.toUpperCase(c));
			} else {
				newName.append(c);
			}
		}
		return newName.toString();
	}

}
