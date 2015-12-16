package com.hp.it.util;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class LdapHelper {

	private static final String INITIAL_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	private static final String PROVIDER_URL = "ldaps://ldap.hp.com:636/";
	private static final String BASE_DN = "ou=Peoplec";

	private static LdapContext context;

	public static boolean authenticate(String userName, String password) throws NamingException {

		boolean validate = false;
		String userDN = "uid=" + userName + "," + BASE_DN;

		//loadCertificate();

		try {
			context = createLdapContext(userDN, password);
			context.getAttributes("");
			validate = true;
		} catch (AuthenticationException e) {
			validate = false;
		} finally {
			if(context != null){
				context.close();
			}
		}

		return validate;
	}

	private static void loadCertificate() {
		System.setProperty("javax.net.ssl.trustStore", LdapHelper.class.getClassLoader().getResource("java6_cacerts")
				.getPath());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static LdapContext createLdapContext(String userDN, String password) throws NamingException {
		loadCertificate();
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldaps://ldap.hp.com:636/ou=People,o=hp.com");
		env.put(Context.SECURITY_PRINCIPAL, "uid=xi-yaoh@hp.com,ou=People,o=hp.com");
		env.put(Context.SECURITY_CREDENTIALS, "hxy@1988");

		return new InitialLdapContext(env, null);
	}
	public static void main(String[] args) {
		loadCertificate();
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldaps://ldap.hp.com:636/ou=People,o=hp.com");
		env.put(Context.SECURITY_PRINCIPAL, "uid=xi-yaoh@hp.com,ou=People,o=hp.com");
		env.put(Context.SECURITY_CREDENTIALS, "hxy@1988");
		try {
			new InitialLdapContext(env, null).getAttributes("");
			System.out.println("333");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
