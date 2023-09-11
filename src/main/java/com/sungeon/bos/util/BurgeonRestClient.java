package com.sungeon.bos.util;

import com.burgeon.framework.restapi.*;
import com.burgeon.framework.restapi.annotation.RestTable;
import com.burgeon.framework.restapi.model.BaseRestBean;
import com.burgeon.framework.restapi.model.ObjectOperateType;
import com.burgeon.framework.restapi.model.ParseBeanColumnValueResult;
import com.burgeon.framework.restapi.request.*;
import com.burgeon.framework.restapi.response.*;
import com.sungeon.bos.core.utils.CollectionUtils;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2023-4-20
 **/
public class BurgeonRestClient {

	private final String serverUrl;
	private final String userName;
	private final String userPassword;
	private final DefaultBurgeonClient burgeonClient;
	private final BurgeonRestService burgeonService;

	public BurgeonRestClient(String serverUrl, String userName, String userPassword) {
		this.serverUrl = serverUrl;
		this.userName = userName;
		this.userPassword = userPassword;
		this.burgeonClient = new DefaultBurgeonClient(serverUrl, userName, userPassword);
		this.burgeonService = new BurgeonRestService(serverUrl, userName, userPassword);
	}

	public BurgeonRestClient(String serverUrl, String userName, String userPassword, int connectTimeout, int readTimeout) {
		this.serverUrl = serverUrl;
		this.userName = userName;
		this.userPassword = userPassword;
		this.burgeonClient = new DefaultBurgeonClient(serverUrl, userName, userPassword, connectTimeout, readTimeout);
		this.burgeonService = new BurgeonRestService(serverUrl, userName, userPassword, connectTimeout, readTimeout);
	}

	/**
	 * 登录
	 *
	 * @return LoginResponse
	 * @throws RestfulException e
	 */
	public LoginResponse login() throws RestfulException {
		return burgeonClient.login();
	}

	/**
	 * 查询List
	 *
	 * @param clazz         返回值Class - T extends BaseRestBean
	 * @param filterParams  过滤条件
	 * @param orderByParams 排序条件
	 * @param <T>           BaseRestBean
	 * @return List<T extends BaseRestBean>
	 */
	public <T extends BaseRestBean> List<T> query(Class<T> clazz, List<QueryFilterParam> filterParams,
	                                              List<QueryOrderByParam> orderByParams) {
		return query(clazz, filterParams, orderByParams, true);
	}

	/**
	 * 查询List
	 *
	 * @param clazz           返回值Class - T extends BaseRestBean
	 * @param filterParams    过滤条件
	 * @param orderByParams   排序条件
	 * @param isQuerySubTable 是否查询子表
	 * @param <T>             BaseRestBean
	 * @return List<T extends BaseRestBean>
	 */
	public <T extends BaseRestBean> List<T> query(Class<T> clazz, List<QueryFilterParam> filterParams,
	                                              List<QueryOrderByParam> orderByParams, boolean isQuerySubTable) {
		return query(clazz, 0, 100, filterParams, orderByParams, isQuerySubTable);
	}

	/**
	 * 查询List
	 *
	 * @param clazz         返回值Class - T extends BaseRestBean
	 * @param start         开始游标
	 * @param range         返回数量
	 * @param filterParams  过滤条件
	 * @param orderByParams 排序条件
	 * @param <T>           BaseRestBean
	 * @return List<T extends BaseRestBean>
	 */
	public <T extends BaseRestBean> List<T> query(Class<T> clazz, int start, int range, List<QueryFilterParam> filterParams,
	                                              List<QueryOrderByParam> orderByParams) {
		return query(clazz, start, range, filterParams, orderByParams, true);
	}

	/**
	 * 查询List
	 *
	 * @param clazz           返回值Class - T extends BaseRestBean
	 * @param start           开始游标
	 * @param range           返回数量
	 * @param filterParams    过滤条件
	 * @param orderByParams   排序条件
	 * @param isQuerySubTable 是否查询子表
	 * @param <T>             BaseRestBean
	 * @return List<T extends BaseRestBean>
	 */
	public <T extends BaseRestBean> List<T> query(Class<T> clazz, int start, int range, List<QueryFilterParam> filterParams,
	                                              List<QueryOrderByParam> orderByParams, boolean isQuerySubTable) {
		return burgeonService.queryTableEx(clazz, start, range, filterParams, orderByParams, isQuerySubTable);
	}

	/**
	 * 查询单个对象
	 *
	 * @param clazz        返回值Class - T extends BaseRestBean
	 * @param filterParams 过滤条件
	 * @param <T>          BaseRestBean
	 * @return T extends BaseRestBean
	 */
	public <T extends BaseRestBean> T queryObject(Class<T> clazz, List<QueryFilterParam> filterParams) {
		List<T> results = query(clazz, 0, 1, filterParams, null);
		return CollectionUtils.isNotEmpty(results) ? results.get(0) : null;
	}

	/**
	 * 查询单个对象
	 *
	 * @param clazz           返回值Class - T extends BaseRestBean
	 * @param filterParams    过滤条件
	 * @param isQuerySubTable 是否查询子表
	 * @param <T>             BaseRestBean
	 * @return T extends BaseRestBean
	 */
	public <T extends BaseRestBean> T queryObject(Class<T> clazz, List<QueryFilterParam> filterParams, boolean isQuerySubTable) {
		List<T> results = query(clazz, 0, 1, filterParams, null, isQuerySubTable);
		return CollectionUtils.isNotEmpty(results) ? results.get(0) : null;
	}

	/**
	 * 新增对象
	 * @param objectCreateBean 对象
	 * @return ObjectCreateResponse
	 */
	public ObjectCreateResponse objectCreate(BaseRestBean objectCreateBean) {
		return burgeonService.objectCreate(objectCreateBean);
	}

	/**
	 * 修改对象
	 *
	 * @param objectModifyBean 对象
	 * @return ObjectModifyResponse
	 */
	public ObjectModifyResponse objectModify(BaseRestBean objectModifyBean) {
		return burgeonService.objectModify(objectModifyBean);
	}

	/**
	 * 删除对象
	 *
	 * @param objectDeleteBean 对象
	 * @return ObjectDeleteResponse
	 */
	public ObjectDeleteResponse objectDelete(BaseRestBean objectDeleteBean) {
		return burgeonService.objectDelete(objectDeleteBean);
	}

	/**
	 * 提交对象
	 *
	 * @param objectSubmitBean 对象
	 * @return ObjectSubmitResponse
	 */
	public ObjectSubmitResponse objectSubmit(BaseRestBean objectSubmitBean) {
		ObjectSubmitRequest submitRequest = new ObjectSubmitRequest();
		try {
			ParseBeanColumnValueResult parseResult = objectSubmitBean.parseColumnValue();
			submitRequest.setAkValue(parseResult.getAkFieldValue());
			submitRequest.setTableName(getRestBeanTableName(objectSubmitBean.getClass()));
			ExecuteRestfulResponse<ObjectSubmitResponse> submitResponse = this.burgeonClient.execute(submitRequest);
			return submitResponse != null
					&& submitResponse.getSipStatus() == BurgeonSipStatus.SUCCESS
					&& CollectionUtils.isNotEmpty(submitResponse.getResponseList())
					? submitResponse.getResponseList().get(0) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 提交对象
	 *
	 * @param tableName 表名称
	 * @param objectId  对象ID
	 * @return ObjectSubmitResponse
	 */
	public ObjectSubmitResponse objectSubmit(String tableName, Long objectId) {
		ObjectSubmitRequest submitRequest = new ObjectSubmitRequest();
		try {
			submitRequest.setId(objectId.intValue());
			submitRequest.setTableName(tableName.toUpperCase());
			ExecuteRestfulResponse<ObjectSubmitResponse> submitResponse = this.burgeonClient.execute(submitRequest);
			return submitResponse != null
					&& submitResponse.getSipStatus() == BurgeonSipStatus.SUCCESS
					&& CollectionUtils.isNotEmpty(submitResponse.getResponseList())
					? submitResponse.getResponseList().get(0) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 取消提交对象
	 *
	 * @param objectUnSubmitBean 对象
	 * @return ObjectUnSubmitResponse
	 */
	public ObjectUnSubmitResponse objectUnSubmit(BaseRestBean objectUnSubmitBean) {
		ObjectUnSubmitRequest unSubmitRequest = new ObjectUnSubmitRequest();
		try {
			ParseBeanColumnValueResult parseResult = objectUnSubmitBean.parseColumnValue();
			unSubmitRequest.setAkValue(parseResult.getAkFieldValue());
			unSubmitRequest.setTableName(getRestBeanTableName(objectUnSubmitBean.getClass()));
			ExecuteRestfulResponse<ObjectUnSubmitResponse> unSubmitResponse = this.burgeonClient.execute(unSubmitRequest);
			return unSubmitResponse != null
					&& unSubmitResponse.getSipStatus() == BurgeonSipStatus.SUCCESS
					&& CollectionUtils.isNotEmpty(unSubmitResponse.getResponseList())
					? unSubmitResponse.getResponseList().get(0) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 取消提交对象
	 *
	 * @param tableName 表名称
	 * @param objectId  对象ID
	 * @return ObjectUnSubmitResponse
	 */
	public ObjectUnSubmitResponse objectUnSubmit(String tableName, Long objectId) {
		ObjectUnSubmitRequest unSubmitRequest = new ObjectUnSubmitRequest();
		try {
			unSubmitRequest.setId(objectId.intValue());
			unSubmitRequest.setTableName(tableName.toUpperCase());
			ExecuteRestfulResponse<ObjectUnSubmitResponse> unSubmitResponse = this.burgeonClient.execute(unSubmitRequest);
			return unSubmitResponse != null
					&& unSubmitResponse.getSipStatus() == BurgeonSipStatus.SUCCESS
					&& CollectionUtils.isNotEmpty(unSubmitResponse.getResponseList())
					? unSubmitResponse.getResponseList().get(0) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 操作表单，支持新增表单带明细，并自动提交单
	 *
	 * @param processOrderBean 对象
	 * @param operateType      操作方式，默认为NONE；CREATE、MODIFY、DELETE、NONE
	 * @return ProcessOrderResponse
	 */
	public ProcessOrderResponse processOrder(BaseRestBean processOrderBean, ObjectOperateType operateType) {
		return burgeonService.processOrder(processOrderBean, operateType);
	}

	/**
	 * 执行动作定义
	 *
	 * @param webActionName        动作定义名称
	 * @param executeWebActionBean 对象
	 * @return ExecuteWebActionResponse
	 */
	public ExecuteWebActionResponse executeWebAction(String webActionName, BaseRestBean executeWebActionBean) {
		ExecuteWebActionRequest webActionRequest = new ExecuteWebActionRequest();
		try {
			ParseBeanColumnValueResult parseResult = executeWebActionBean.parseColumnValue();
			webActionRequest.setAkValue(parseResult.getAkFieldValue());
			webActionRequest.setWebAction(webActionName);
			ExecuteRestfulResponse<ExecuteWebActionResponse> webActionResponse = this.burgeonClient.execute(webActionRequest);
			return webActionResponse != null
					&& webActionResponse.getSipStatus() == BurgeonSipStatus.SUCCESS
					&& CollectionUtils.isNotEmpty(webActionResponse.getResponseList())
					? webActionResponse.getResponseList().get(0) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 执行动作定义
	 *
	 * @param webActionName 动作定义名称
	 * @param objectId      对象ID
	 * @return ExecuteWebActionResponse
	 */
	public ExecuteWebActionResponse executeWebAction(String webActionName, Long objectId) {
		ExecuteWebActionRequest webActionRequest = new ExecuteWebActionRequest();
		try {
			webActionRequest.setId(objectId.intValue());
			webActionRequest.setWebAction(webActionName);
			ExecuteRestfulResponse<ExecuteWebActionResponse> webActionResponse = this.burgeonClient.execute(webActionRequest);
			return webActionResponse != null
					&& webActionResponse.getSipStatus() == BurgeonSipStatus.SUCCESS
					&& CollectionUtils.isNotEmpty(webActionResponse.getResponseList())
					? webActionResponse.getResponseList().get(0) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ExecuteSQLResponse executeSql(String name, String[] values) {
		ExecuteSQLRequest executeSqlRequest = new ExecuteSQLRequest();
		try {
			executeSqlRequest.setName(name);
			executeSqlRequest.setValues(values);
			ExecuteRestfulResponse<ExecuteSQLResponse> executeSqlResponse = this.burgeonClient.execute(executeSqlRequest);
			return executeSqlResponse != null
					&& executeSqlResponse.getSipStatus() == BurgeonSipStatus.SUCCESS
					&& CollectionUtils.isNotEmpty(executeSqlResponse.getResponseList())
					? executeSqlResponse.getResponseList().get(0) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public GetObjectResponse getObject(BaseRestBean getObjectBean) {
		GetObjectRequest getObjectRequest = new GetObjectRequest();
		try {
			ParseBeanColumnValueResult parseResult = getObjectBean.parseColumnValue();
			getObjectRequest.setAkValue(parseResult.getAkFieldValue());
			getObjectRequest.setTableName(getRestBeanTableName(getObjectBean.getClass()));
			ExecuteRestfulResponse<GetObjectResponse> getObjectResponse = this.burgeonClient.execute(getObjectRequest);
			return getObjectResponse != null
					&& getObjectResponse.getSipStatus() == BurgeonSipStatus.SUCCESS
					&& CollectionUtils.isNotEmpty(getObjectResponse.getResponseList())
					? getObjectResponse.getResponseList().get(0) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ExecuteRestfulResponse<ImportResponse> executeImport(ImportRequest importRequest) throws RestfulException {
		return burgeonClient.execute(importRequest);
	}

	/**
	 * 通用执行方法
	 *
	 * @param request 请求
	 * @param <T>     T extends AbstractResponse
	 * @return ExecuteRestfulResponse<T>
	 * @throws RestfulException e
	 */
	public <T extends AbstractResponse> ExecuteRestfulResponse<T> execute(AbstractRequest<T> request) throws RestfulException {
		return burgeonClient.execute(request);
	}

	/**
	 * 下载文件
	 * <br>经测试登录报错，所以无法使用，原因为缺少restlogin.jsp来负责登录，需要实现下
	 *
	 * @param downloadUrl  下载文件的地址
	 * @param saveFileName 保存本地的路径
	 * @return DownloadFileResponse
	 */
	@Deprecated
	public DownloadFileResponse downloadFile(String downloadUrl, String saveFileName) {
		try {
			return burgeonClient.downloadFile(downloadUrl, saveFileName);
		} catch (Exception e) {
			e.printStackTrace();
			DownloadFileResponse response = new DownloadFileResponse();
			response.setSuccess(false);
			response.setErrorMessage(e.getMessage());
			return response;
		}
	}

	private String getRestBeanTableName(Class<? extends BaseRestBean> clazz) {
		String tableName = "";
		RestTable tableAnnotation = clazz.getAnnotation(RestTable.class);
		if (tableAnnotation != null) {
			tableName = tableAnnotation.tableName();
		}
		return tableName;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

}
