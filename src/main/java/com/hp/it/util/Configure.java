package com.hp.it.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configure {
	// action配置文件路径  
	public static final String ACTIONPATH = "conf.properties";  
	
	static Logger log = LoggerFactory.getLogger(Configure.class);
	
	public static HashMap<String, String> map = new HashMap<String, String>();
	
	// 属性文件   
	public static Properties prop = new Properties();  
	 static InputStream fis = null;
	static {
		//获取当前类加载的根目录，如：/C:/Program Files/Apache/Tomcat 6.0/webapps/fee/WEB-INF/classes/  
		String path;
		try {
			URL u = Thread.currentThread().getContextClassLoader().getResource("conf.properties");
			fis=Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties");
			//fis=Configure.class.getResourceAsStream("conf.properties");
			//加载文件流的属性     
			prop.load(fis);
			//遍历propers 并存储到map中
			Enumeration enu2=prop.propertyNames();
			while(enu2.hasMoreElements()){
			    String key = (String)enu2.nextElement();
			    String value = prop.getProperty(key);
			    map.put(key, value);
			    log.debug(key+":"+value);
			} 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fis != null){
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		   
		
	}
	
	public static String get(String key){
		return map.get(key);
	}
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		// 把文件读入文件输入流，存入内存中  
		//fis = Configure.class.getClassLoader().getResourceAsStream("conf.properties");
		System.out.println(fis);
	}
}
