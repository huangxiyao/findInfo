package com.hp.it.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dbutil {
	private Connection con = null;
	static Logger log = LoggerFactory.getLogger(Dbutil.class);
    static {
    	try {
			Class.forName("com.mysql.jdbc.Driver");
			log.info("load my driver successful");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    public static Connection getConnetcion(){
    	try {
    		log.info("get the sql connetcion");
			return DriverManager.getConnection(Configure.get("jdbc") , Configure.get("username"), Configure.get("dbPasswd"));
		} catch (SQLException e) {
			e.printStackTrace();
		} 
    	return null;
    }
    
    public static ResultSet select(String sql){
    	Connection con = null;
    	Statement stat = null;  
    	ResultSet rs = null;
		try {
			con = getConnetcion();
			stat = con.createStatement();  
	    	rs = stat.executeQuery(sql);
	    	return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static int update(String sql){
    	Connection con = null;
    	Statement stat = null;  
    	int rs = 0;
		try {
			con = getConnetcion();
			stat = con.createStatement();  
	    	rs = stat.executeUpdate(sql);
	    	return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(con,stat,null);
		}
    	return 0;
    }
    
    public static int insert(String sql){
    	Connection con = null;
    	Statement stat = null;  
    	int rs = 0;
		try {
			con = getConnetcion();
			stat = con.createStatement();  
	    	rs = stat.executeUpdate(sql);
	    	return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(con,stat,null);
		}
    	return 0;
    }
    
    public static void close(Connection con,Statement stat, ResultSet res){
    	if(stat != null){
			try {
				log.info("close the statement");
				stat.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		if(res != null){
			try {
				log.info("close the resultSet");
				res.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		if(con != null){
			try {
				log.info("close the connection");
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
}
