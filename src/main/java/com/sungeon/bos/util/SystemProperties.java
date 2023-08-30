package com.sungeon.bos.util;

import com.sungeon.bos.core.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
public class SystemProperties {

	private static final Logger log = LoggerFactory.getLogger(SystemProperties.class);

	private static PropertiesUtils pros = null;
	public static final Long origDate = 1262275200000L;
	public static final Long defaultLong = Long.parseLong("0");

	public static String AppId;
	public static String AppSecret;
	public static String Version;
	public static String Brand;
	public static String ScheduleGroup;
	public static String ParamStore;
	public static String ParamSignStatus;
	public static String ParamSignType;
	public static String ParamSignQhsStatus;
	public static int ParamDataCount;
	public static String ParamVIPMobileRepeatable;
	public static int ParamTimeCalled;
	public static int ParamDefaultUserId;
	public static String ParamPropelStatus;
	public static String ParamPropelURL;
	public static String ParamProductDIMBrand;
	public static String ParamProductDIMClass;

	static {
		try {
			// 到/Tomcat/bin目录
			// String binPath = System.getProperty("user.dir");
			// 到/Tomcat目录
			// String catalinaHomePath = System.getProperty("catalina.home");
			// String filePath = catalinaHomePath + "/system.properties";
			// 相对路径classes读取文件
			InputStream inputStream = SystemProperties.class.getClassLoader().getResourceAsStream("system.properties");
			// 绝对路劲读取文件
			// InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
			pros = new PropertiesUtils();
			pros.load(inputStream);

			AppId = pros.getString("AppId", "");
			AppSecret = pros.getString("AppSecret", "");
			Version = pros.getString("Version", "2.0");
			Brand = pros.getString("Brand", "");
			ScheduleGroup = pros.getString("ScheduleGroup", "");

			ParamStore = pros.getString("Param.Store", "");
			ParamSignStatus = pros.getString("Param.Sign.Status", "Y");
			ParamSignType = pros.getString("Param.Sign.Type", "T3");
			ParamSignQhsStatus = pros.getString("Param.Sign.qhs.Status", "N");
			ParamDataCount = pros.getInt("Param.Data.Count", 300);
			ParamVIPMobileRepeatable = pros.getString("Param.VIP.Mobile.Repeatable", "N");
			ParamTimeCalled = pros.getInt("Param.Time.Called", 0);
			ParamDefaultUserId = pros.getInt("Param.Default.UserId", 893);
			ParamPropelStatus = pros.getString("Param.Propel.Status", "N");
			ParamPropelURL = pros.getString("Param.Propel.URL", "");
			ParamProductDIMBrand = pros.getString("Param.Product.DIM.Brand", "M_DIM1_ID");
			ParamProductDIMClass = pros.getString("Param.Product.DIM.Class", "M_DIM2_ID");
		} catch (IOException e) {
			log.info("file is not exist", e);
		}
	}

}
