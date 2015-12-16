package com.hp.it.util;

import java.net.URL;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jason
 *
 */
public class LdapConnection {
	
	public LdapContext context = null;
	
	private static Logger log = LoggerFactory.getLogger(LdapConnection.class);
	
	
	private static void loadCertificate() {
		URL u = Thread.currentThread().getContextClassLoader().getResource("java6_cacerts");
		String path;
		if(u == null){
			path = Configure.get("cerpath");
		}else{
			path = u.getPath();
		}
			log.info(path);
			System.setProperty("javax.net.ssl.trustStore", path);
	}
	
	public LdapConnection(String contextFactory, String providerUrl, String securityPrincipal, String securityCredentials) throws NamingException{
		log.info("check the certificate file");
		loadCertificate();
    	Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
		env.put(Context.PROVIDER_URL, providerUrl);
		//env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
		//env.put(Context.SECURITY_CREDENTIALS, securityCredentials);
		log.info("get the ldap connection!");
		context = new InitialLdapContext(env,null);
		log.info("LdapConnection: ldap connection successful!");
	}
    
	public NamingEnumeration search(String baseDn, String filter){
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		try {
			NamingEnumeration results;
			results = context.search(baseDn, filter, searchControls);
			return results;
		} catch (NamingException e) {
			e.printStackTrace();
			throw new RuntimeException("LdapConnection: ldap search exception!", e);
		}
	}
	
	
	public void closeConnection(){
			try {
				if(context != null){
					context.close();
					log.info("LdapConnection: ldap connection have closed!");
				}
			} catch (NamingException e) {
				e.printStackTrace();
				throw new RuntimeException("LdapConnection: context.close() exception!", e);
			}
	}
    public static void main(String[] args) throws NamingException{    	
    	
    	LdapContext context = null;
    	NamingEnumeration results = null;
		
		loadCertificate();
    	Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, Configure.get("initContextFactory"));
		env.put(Context.PROVIDER_URL, Configure.get("providerUrl"));
		env.put(Context.SECURITY_PRINCIPAL, "uid=xi-yaoh@hpe.com,ou=People,o=hp.com");
		env.put(Context.SECURITY_CREDENTIALS, "hxy.@.1988");
		
		context = new InitialLdapContext(env,null);
//		//context.
		//results = context.search("ou=People,o=hp.com", "(uid=xi-yaoh@hpe.com)", new SearchControls());
		results = context.search("ou=Groups,o=hp.com", "(cn=ADMIN-NEXUS-118361-DEV)", new SearchControls());
			//while (results.hasMoreElements()) {
	            SearchResult searchResults = (SearchResult) (results.next());
	            //if(searchResults.getAttributes().get("cn") != null && searchResults.getAttributes().get("cn").toString().lastIndexOf("@hp")>0){
//	            	String uid = searchResults.getAttributes().get("cn").toString();
	            	System.out.println(searchResults.getAttributes().get("dn"));
//	 	            System.out.println("============00000000000000=============");
	           // }
	           // System.out.println("33333");
	        //    System.out.println(searchResults.getAttributes().get("baseDn"));
	       // }
			Attributes attrs = searchResults.getAttributes();
			System.out.println(searchResults.getName()+"----------------");
        	NamingEnumeration enIds = attrs.getIDs();
        	while(enIds.hasMoreElements()){
        		String attrId = enIds.next().toString();
        		System.out.println(attrs.get(attrId).toString());
        		attrs.get(attrId);
        	}
  //      	System.out.println(attrs.get("member").toString());
        	ModificationItem[] mods = new ModificationItem[1];
        	//attrs.get("member").remove("uid=xi-yaoh@hpe.com,ou=People,o=hp.com");
        	mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("member1", "uid=yu-juan.zhang@hpe.com,ou=People,o=hp.com"));
        	Attributes attrs1 = new BasicAttributes(true);
        	
        	attrs1.put("st","beijing");
        	//System.out.println(attrs1.get("member").toString());
        	//context.modifyAttributes("uid=xi-yaoh@hpe.com,ou=People,o=hp.com", DirContext.ADD_ATTRIBUTE, attrs1);
        	context.modifyAttributes("cn=ADMIN-200359-DEV,ou=Groups,o=hp.com", mods);
        	//System.out.println(attrs.get("member").toString());
    }
}
