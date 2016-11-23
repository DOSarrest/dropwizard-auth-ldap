package com.yammer.dropwizard.authenticator;

import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.ldap.StartTlsResponse;

import java.io.IOException;
import java.util.Hashtable;

public class AutoclosingLdapContext extends InitialLdapContext implements AutoCloseable {
    private StartTlsResponse tls;

	public AutoclosingLdapContext() throws NamingException {
        super();
    }

    public AutoclosingLdapContext(Hashtable<?, ?> environment, Boolean tls) throws NamingException {
        super(environment, null);
        if (tls==true) {
        	loadTLS();
		this.addToEnvironment(Context.SECURITY_AUTHENTICATION, "simple");
		this.addToEnvironment(Context.SECURITY_PRINCIPAL, this.getEnvironment().get(Context.SECURITY_PRINCIPAL));
		this.addToEnvironment(Context.SECURITY_CREDENTIALS, this.getEnvironment().get(Context.SECURITY_CREDENTIALS));
        }
    }
    
    public void loadTLS() {
    	try {
			tls = (StartTlsResponse) this.extendedOperation(new StartTlsRequest());
			tls.negotiate();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void close() throws NamingException {
    	try {
			tls.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        super.close();
    }
}
