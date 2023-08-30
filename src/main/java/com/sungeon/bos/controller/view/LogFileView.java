package com.sungeon.bos.controller.view;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.core.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.apache.logging.log4j.web.WebLoggerContextUtils.getServletContext;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@RequestMapping("/LogFile")
@Controller
@Slf4j
public class LogFileView {

	@RequestMapping("/index")
	public String getLogFiles(Integer page, HttpServletRequest req) {
		dealRequest(page, req);
		return "/logfile/index";
	}

	private void dealRequest(Integer page, HttpServletRequest req) {
		if (null == page) {
			page = 1;
		}

		// String filePath = req.getSession().getServletContext().getRealPath("/WEB-INF/logs/INFO/");
		String catalina = System.getProperty("catalina.home");
		String webAppRootKey = getServletContext().getInitParameter("webAppRootKey");
		String filePath = catalina + "\\..\\logs\\" + webAppRootKey + "\\";
		int pageAllCount = 20;

		File fileDir = new File(filePath);
		fileList.clear();
		getAllFiles(fileDir, 0);
		sortFileList();

		JSONArray jsons = new JSONArray();
		for (File file : fileList) {
			JSONObject json = new JSONObject();
			json.fluentPut("fileName", file.getName());
			json.fluentPut("fileSize", (file.length() / 1024 + 1) + " KB");
			json.fluentPut("fileTime", DateTimeUtils.print(DateTimeUtils.date(file.lastModified())));
			jsons.add(json);
		}

		int fileSize = fileList.size();
		int pageSize = (fileSize % pageAllCount) == 0 ? (fileSize / pageAllCount) : (fileSize / pageAllCount + 1);
		String countDesc;
		if (fileSize <= pageAllCount) {
			countDesc = "1-" + fileSize + "  /  " + fileSize;
			req.setAttribute("files", jsons);
		} else if (page >= pageSize) {
			countDesc = ((pageSize - 1) * pageAllCount + 1) + "-" + fileSize + "  /  " + fileSize;
			List<Object> arr = jsons.subList((pageSize - 1) * pageAllCount, fileSize);
			req.setAttribute("files", JSONArray.parseArray(arr.toString()));
		} else {
			countDesc = ((page - 1) * pageAllCount + 1) + "-" + (page * pageAllCount) + "  /  " + fileSize;
			List<Object> arr = jsons.subList((page - 1) * pageAllCount, page * pageAllCount);
			req.setAttribute("files", JSONArray.parseArray(arr.toString()));
		}

		req.setAttribute("page", page);
		if (page == 1) {
			req.setAttribute("lastPage", page);
		} else {
			req.setAttribute("lastPage", page - 1);
		}
		if (page == pageSize) {
			req.setAttribute("nextPage", page);
		} else {
			req.setAttribute("nextPage", page + 1);
		}
		req.setAttribute("countDesc", countDesc);
		req.setAttribute("pageSize", pageSize);
	}

	/**
	 * 文件下载 ResponseEntity：该类实现响应头、文件数据（以字节存储）、状态封装在一起交给浏览器处理以实现浏览器的文件下载
	 * <p>
	 * ResponseEntity 也可作为响应数据使用 与@ResponseBody 注解功能相似
	 * 但是ResponseEntity的优先级高于@ResponseBody
	 * 在不是ResponseEntity的情况下才去检查有没有@ResponseBody注解。
	 * 如果响应类型是ResponseEntity可以不写@ResponseBody注解，写了也没有关系。
	 * <p>
	 * 简单粗暴的讲，个人理解： @ResponseBody可以直接返回Json结果，
	 * <p>
	 * ResponseEntity 不仅可以返回json结果，还可以定义返回的HttpHeaders和HttpStatus
	 */
	@RequestMapping("/download")
	public ResponseEntity<byte[]> download(@RequestParam String fileName, HttpServletRequest req) throws IOException {
		// 文件所在位置
		// String filePath = req.getSession().getServletContext().getRealPath("/WEB-INF/logs/INFO/");
		String catalina = System.getProperty("catalina.home");
		String webAppRootKey = getServletContext().getInitParameter("webAppRootKey");
		String filePath = catalina + "\\..\\logs\\" + webAppRootKey + "\\";
		// 获取要下载的文件
		File file = new File(filePath, fileName);
		if (!file.exists()) {
			File fileDir = new File(filePath);
			fileList.clear();
			getAllFiles(fileDir, 0);
			sortFileList();
			for (File f : fileList) {
				if (f.getName().equals(fileName)) {
					file = f;
					break;
				}
			}
		}

		// http头信息 设置一些约束之类的东西
		HttpHeaders headers = new HttpHeaders();
		// 设置编码 为了解决中文名称乱码问题
		String downloadFileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
		// 将编码加到http头信息中
		headers.setContentDispositionFormData("attachment", downloadFileName);
		/**
		 * MediaType:互联网媒介类型 contentType：具体请求中的媒体类型信息 MediaType： 很多常量 多种类型可设置
		 * APPLICATION_OCTET_STREAM：二进制流数据（如常见的文件下载）
		 *
		 * 还有一种常见的： MULTIPART_FORM_DATA： 需要在表单中进行文件上传时，就需要使用该格式
		 */
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		/**
		 * FileUtils.readFileToByteArray：读取文件到字节数组
		 *
		 * CREATED：201状态码：创建
		 *
		 * 简而言之，个人理解： 创建/下载 一个根据http头信息约束的 字节数组（文件）
		 */
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
	}

	private List<File> fileList = new ArrayList<>();

	/**
	 * 使用递归获取所有info文件
	 *
	 * @param dir   目录
	 * @param level 层级
	 */
	private void getAllFiles(File dir, int level) {
		System.out.println(getLevel(level) + dir.getName());
		level++;
		File[] files = dir.listFiles();
		for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
			if (files[i].isDirectory()) {
				//这里面用了递归的算法
				getAllFiles(files[i], level);
			} else {
				System.out.println(getLevel(level) + files[i]);
				if (files[i].getName().startsWith("info")) {
					fileList.add(files[i]);
				}
			}
		}
	}

	/**
	 * 获取层级的方法
	 *
	 * @param level 层级
	 * @return 树状层级
	 */
	private String getLevel(int level) {
		StringBuilder sb = new StringBuilder();
		for (int l = 0; l < level; l++) {
			sb.append("|--");
		}
		return sb.toString();
	}

	/**
	 * 排序
	 */
	private void sortFileList() {
		fileList.sort(new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				long diff = f1.lastModified() - f2.lastModified();
				if (diff > 0) {
					return -1;
				} else if (diff == 0) {
					return 0;
				} else {
					return 1;
				}
			}

			@Override
			public boolean equals(Object obj) {
				return true;
			}
		});
	}

}
