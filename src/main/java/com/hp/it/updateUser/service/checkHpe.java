package com.hp.it.updateUser.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.it.util.Configure;

public class checkHpe {

	private static Logger log = LoggerFactory.getLogger(checkHpe.class);
	public static void readFileByLines(String fileName) {
        File file = new File(fileName);
//		URL u = Thread.currentThread().getContextClassLoader().getResource(fileName);
//        File file = new File(u.getPath());
        BufferedReader reader = null;
        FileWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            String str = null;
            StringBuffer sb= new StringBuffer("");
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null && !"".equals(tempString)) {
            	String strUser = tempString.trim();
            	//将员工中的hp转换为hpe
            	if(!strUser.contains("@hpe")){
            		strUser = strUser.replaceAll("@hp", "@hpe");
            	}
            	Map<String, String> map = UserService.queryLdapUser(strUser, null);
            	if(map != null && (map.get("result") == null) && !map.get("hpStatus").contains("Terminated")){
            		sb.append(strUser+"\n");
            		log.info(strUser+"\n");
            	}
            }
            
            // write string to file
            writer= new FileWriter(new File("result_casci-hpe-user.txt"));
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(sb.toString());
            bw.close();
            System.out.println("写入完毕");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            
            
            if(writer != null){
            	try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
    }
	
	public static void main(String[] args) {
		readFileByLines("casci-user.txt");
	}
}
