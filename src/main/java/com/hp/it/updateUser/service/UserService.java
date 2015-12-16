package com.hp.it.updateUser.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.it.util.Configure;
import com.hp.it.util.LdapConnection;

public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
		
	//读取当前用户的信心并去ldap中执行查询,并获取在ldap系统中已经变为hpe的用户信息
	public static  Map<String, String> queryLdapUser(String uid, String filterString){
		Map<String,String> map = new HashMap<String, String>();
		log.info("execute the ldap query");
		//获取ldap连接
		LdapConnection con = null;
		try {
			con = new LdapConnection(Configure.get("initContextFactory"), Configure.get("providerUrl"), null, null);
			//查询剩余用户在ladp服务器中的名字信息
				String filter = uid;
				filter = "(uid="+filter+")";
				NamingEnumeration results;
				log.debug("filter:"+filter);
				results = con.search(Configure.get("baseDn"), filter);
				if (results.hasMoreElements()) {
		            SearchResult searchResults = (SearchResult) (results.next());
		            if(filterString != null && !"".equals(filterString)){
		            	Attribute attr = searchResults.getAttributes().get(filterString);
		            	if(attr != null){
		            		map.put(filterString, attr.toString());
		            	}else{
		            		map.put("error", "the query condition is wrong!");
		            	}
		            }else{
		            	Attributes attrs = searchResults.getAttributes();
		            	NamingEnumeration enIds = attrs.getIDs();
		            	while(enIds.hasMoreElements()){
		            		String attrId = enIds.next().toString();
		            		map.put(attrId,attrs.get(attrId).toString());
		            	}
		            }
		            
		        }else{
		        	map.put("result", "fail");
		        }
			
		} catch (NamingException e) {
			e.printStackTrace();
			throw new RuntimeException("LdapConnection failed!",e);
		}finally{
			if(con != null){
				con.closeConnection();
			}
		}
		return map;
	}
	
	
	public static void main(String[] args) {
		String flag = "";
		do{
			Pattern pattern = Pattern.compile(Configure.get("checkConditon").toString());
			Scanner sc = new Scanner(System.in);
			System.out.println("Please input the mail:");
			String mail = sc.nextLine();
			System.out.println("Please input the query condition(default null):");
			String condition = sc.nextLine();
			System.out.println("mail:"+mail+", condition:"+condition);
			System.out.println();
			if(mail != null && !"".equals(mail)){
				Matcher matcher = pattern.matcher(mail);
				if(!matcher.matches()){
					System.out.println(Configure.get("checkTips"));
					flag = "y";
					continue;
				}
				Map<String,String> map = queryLdapUser(mail,condition);
				if(map.get("error") != null){
					System.out.println(map.get("error"));
				}else if(map.get("result") != null){
					System.out.println("not this user on server!");
				}else{
					for(Map.Entry<String, String> m : map.entrySet()){
						System.out.println(m.getValue());
					}
				}
			}else{
				System.out.println("Please input the parameter!");
			}
			System.out.println("if continue?(y/n)");
			flag = sc.nextLine();
		}while (!"".equals(flag) && flag.equals("y"));
		System.exit(0);
	}
}
