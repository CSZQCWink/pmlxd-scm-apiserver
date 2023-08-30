package com.sungeon.bos.util;

import com.alibaba.fastjson.JSON;
import com.sungeon.bos.core.constants.Constants;
import com.sungeon.bos.core.model.HttpResponse;
import com.sungeon.bos.core.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.Map;

/**
 * @author : 刘国帅
 * @date : 2016-12-6
 */
@Slf4j
public class HttpClientUtil {

	/**
	 * HttpClient方式发送Http请求
	 *
	 * @param requestUrl 请求URL
	 * @return HttpResponse
	 */
	public static HttpResponse httpGet(String requestUrl) {
		HttpResponse result = new HttpResponse();
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(3000)
					.setConnectionRequestTimeout(30000).build();
			CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
			HttpGet http = new HttpGet(requestUrl);
			org.apache.http.HttpResponse response = client.execute(http);

			result = getHttpResponse(response);
			client.close();
		} catch (SocketTimeoutException e) {
			log.error("http请求超时：" + e.getMessage(), e);
			result.setCode(408);
			result.setMessage("http请求超时：" + e.getMessage());
		} catch (IOException e) {
			log.error("http请求失败：" + e.getMessage(), e);
			result.setCode(400);
			result.setMessage("http请求失败：" + e.getMessage());
		}
		return result;
	}


	/**
	 * HttpClient方式发送Http请求
	 *
	 * @param requestUrl  请求URL
	 * @param requestData POST方法的表单数据
	 * @return HttpResponse
	 */
	public static HttpResponse httpPostGBK(String requestUrl, String requestData) {
		HttpResponse result = new HttpResponse();
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(3000)
					.setConnectionRequestTimeout(30000).build();
			CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
			HttpPost http = new HttpPost(requestUrl);
			if (null != requestData) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(requestData, Constants.CHARSET_GBK);
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/x-www-form-urlencoded");
				http.setEntity(entity);
				// method.setHeader("Authorization", "APP_KEYS " + appkeys);
			}
			org.apache.http.HttpResponse response = client.execute(http);
			result = getHttpResponse(response);
			client.close();
		} catch (SocketTimeoutException e) {
			log.error("http请求超时：" + e.getMessage(), e);
			result.setCode(408);
			result.setMessage("http请求超时：" + e.getMessage());
		} catch (IOException e) {
			log.error("http请求失败：" + e.getMessage(), e);
			result.setCode(400);
			result.setMessage("http请求失败：" + e.getMessage());
		}
		return result;
	}

	/**
	 * HttpClient方式发送Http请求
	 *
	 * @param requestUrl  请求URL
	 * @param headers     Header参数
	 * @param requestData POST方法的表单数据
	 * @return HttpResponse
	 */
	public static HttpResponse httpPost(String requestUrl, Map<String, String> headers, String requestData) {
		HttpResponse result = new HttpResponse();
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(3000)
					.setConnectionRequestTimeout(30000).build();
			CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
			HttpPost http = new HttpPost(requestUrl);
			if (null != requestData) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(requestData, Constants.CHARSET_UTF8);
				entity.setContentEncoding(Constants.CHARSET_UTF8);
				entity.setContentType("application/x-www-form-urlencoded");
				http.setEntity(entity);
				for (String key : headers.keySet()) {
					http.setHeader(key, headers.get(key));
				}
				// method.setHeader("Authorization", "APP_KEYS " + appkeys);
			}
			org.apache.http.HttpResponse response = client.execute(http);
			result = getHttpResponse(response);
			client.close();
		} catch (SocketTimeoutException e) {
			log.error("http请求超时：" + e.getMessage(), e);
			result.setCode(408);
			result.setMessage("http请求超时：" + e.getMessage());
		} catch (IOException e) {
			log.error("http请求失败：" + e.getMessage(), e);
			result.setCode(400);
			result.setMessage("http请求失败：" + e.getMessage());
		}
		return result;
	}

	/**
	 * HttpClient方式发送Http请求
	 *
	 * @param requestUrl 请求URL
	 * @param jsonData   POST方法的表单数据
	 * @return HttpResponse
	 */
	public static HttpResponse httpPost(String requestUrl, JSON jsonData) {
		HttpResponse result = new HttpResponse();
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(3000)
					.setConnectionRequestTimeout(30000).build();
			CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
			HttpPost http = new HttpPost(requestUrl);
			if (null != jsonData) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonData.toString(), Constants.CHARSET_UTF8);
				entity.setContentEncoding(Constants.CHARSET_UTF8);
				entity.setContentType("application/json");
				http.setEntity(entity);
				// method.setHeader("Authorization", "APP_KEYS " + appkeys);
			}
			org.apache.http.HttpResponse response = client.execute(http);
			result = getHttpResponse(response);
			client.close();
		} catch (SocketTimeoutException e) {
			log.error("http请求超时：" + e.getMessage(), e);
			result.setCode(408);
			result.setMessage("http请求超时：" + e.getMessage());
		} catch (IOException e) {
			log.error("http请求失败：" + e.getMessage(), e);
			result.setCode(400);
			result.setMessage("http请求失败：" + e.getMessage());
		}
		return result;
	}

	/**
	 * HttpClient方式发送Http请求
	 *
	 * @param requestUrl 请求URL
	 * @param headers    Header参数
	 * @param jsonData   POST方法的表单数据
	 * @return HttpResponse
	 */
	public static HttpResponse httpPost(String requestUrl, Map<String, String> headers, JSON jsonData) {
		HttpResponse result = new HttpResponse();
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(3000)
					.setConnectionRequestTimeout(30000).build();
			CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
			HttpPost http = new HttpPost(requestUrl);
			if (null != jsonData) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonData.toString(), Constants.CHARSET_UTF8);
				entity.setContentEncoding(Constants.CHARSET_UTF8);
				entity.setContentType("application/json");
				http.setEntity(entity);
				for (String key : headers.keySet()) {
					http.setHeader(key, headers.get(key));
				}
				// method.setHeader("Authorization", "APP_KEYS " + appkeys);
			}
			org.apache.http.HttpResponse response = client.execute(http);
			result = getHttpResponse(response);
			client.close();
		} catch (SocketTimeoutException e) {
			log.error("http请求超时：" + e.getMessage(), e);
			result.setCode(408);
			result.setMessage("http请求超时：" + e.getMessage());
		} catch (IOException e) {
			log.error("http请求失败：" + e.getMessage(), e);
			result.setCode(400);
			result.setMessage("http请求失败：" + e.getMessage());
		}
		return result;
	}

	/**
	 * HttpClient方式发送Http请求
	 *
	 * @param requestUrl  请求URL
	 * @param requestData POST方法的表单数据
	 * @return
	 */
	public static HttpResponse httpsPost(String requestUrl, String requestData) {
		HttpResponse result = new HttpResponse();
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpClient = SSLClient.getHttpClient();
			HttpPost httpPost = new HttpPost(requestUrl);
			// 实体设置

			StringEntity entity = new StringEntity(requestData, Constants.CHARSET_UTF8);
			httpPost.setEntity(entity);

			// 发起请求
			httpResponse = httpClient.execute(httpPost);
			result = getHttpResponse(httpResponse);
			httpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * HttpClient方式发送Http请求
	 *
	 * @param requestUrl  请求URL
	 * @param requestData POST方法的表单数据
	 * @return
	 */
	public static HttpResponse httpsPostGBK(String requestUrl, String requestData) {
		HttpResponse result = new HttpResponse();
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpClient = SSLClient.getHttpClient();
			HttpPost httpPost = new HttpPost(requestUrl);
			// 实体设置

			StringEntity entity = new StringEntity(requestData, Constants.CHARSET_GBK);
			httpPost.setEntity(entity);

			// 发起请求
			httpResponse = httpClient.execute(httpPost);
			result = getHttpResponse(httpResponse);
			httpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static HttpResponse getHttpResponse(org.apache.http.HttpResponse response) throws ParseException, IOException {
		HttpResponse result = new HttpResponse();
		result.setCode(response.getStatusLine().getStatusCode());
		result.setMessage(response.getStatusLine().getReasonPhrase());
		result.setBody(EntityUtils.toString(response.getEntity()));
		return result;
	}

	public static HttpResponse download(String fileUrl, String destFileName) {
		HttpResponse result = new HttpResponse();
		try {
			// 生成一个httpclient对象
			CloseableHttpClient client = HttpClients.createDefault();
			HttpGet http = new HttpGet(fileUrl);
			org.apache.http.HttpResponse response = client.execute(http);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();

			File file = FileUtils.getFile(destFileName);
			FileOutputStream fos = new FileOutputStream(file);
			int line = -1;
			byte[] fileData = new byte[1024];
			while ((line = is.read(fileData)) != -1) {
				fos.write(fileData, 0, line);
				// 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
			}
			fos.flush();
			fos.close();
			is.close();

			result.setCode(response.getStatusLine().getStatusCode());
			result.setMessage("File[" + fileUrl + "] Download Success");
			client.close();
		} catch (SocketTimeoutException e) {
			log.error("http请求超时：" + e.getMessage(), e);
			result.setCode(408);
			result.setMessage("http请求超时：" + e.getMessage());
		} catch (IOException e) {
			log.error("http请求失败：" + e.getMessage(), e);
			result.setCode(400);
			result.setMessage("http请求失败：" + e.getMessage());
		}
		return result;
	}

	public static HttpResponse upload(String requestUrl, String fileName) {
		HttpResponse result = new HttpResponse();
		CloseableHttpClient client = HttpClients.createDefault();
		// CloseableHttpClient client = HttpClientBuilder.create().build();
		try {
			HttpPost http = new HttpPost(requestUrl);

			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000)
					.build();
			http.setConfig(requestConfig);

			File file = FileUtils.getFile(fileName);
			FileBody bin = new FileBody(file);
			StringBody comment = new StringBody("This is comment", ContentType.TEXT_PLAIN);

			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("file", bin).addPart("comment", comment)
					.build();

			http.setEntity(reqEntity);

			System.out.println("executing request " + http.getRequestLine());
			CloseableHttpResponse response = client.execute(http);
			try {
				System.out.println(response.getStatusLine());
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					String responseEntityStr = EntityUtils.toString(response.getEntity());
					System.out.println(responseEntityStr);
					System.out.println("Response content length: " + resEntity.getContentLength());
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
			result = getHttpResponse(response);
			client.close();
		} catch (SocketTimeoutException e) {
			log.error("http请求超时：" + e.getMessage(), e);
			result.setCode(408);
			result.setMessage("http请求超时：" + e.getMessage());
		} catch (IOException e) {
			log.error("http请求失败：" + e.getMessage(), e);
			result.setCode(400);
			result.setMessage("http请求失败：" + e.getMessage());
		}
		return result;
	}

}
