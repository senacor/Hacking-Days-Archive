package senacor.hd.poc.googlecontacts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * {@link http://code.google.com/intl/de-DE/apis/contacts/docs/3.0/developers_guide_protocol.html#Auth
 * @author fmito057
 * 
 */
public class Authentication {
	private static Logger LOG = Logger.getLogger(Authentication.class.getName());
	
	private final static String AUTH_PREFIX = "Auth=";	
	private String authToken = null;

	public void login() {
		// LOG.info("authenticating to Google ...");
		Client client = Client.create();	
		WebResource wr = client.resource("https://www.google.com/accounts/ClientLogin");

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("accountType", "GOOGLE");
	    queryParams.add("Email", "ralph.winzinger@senacor.com");
	    queryParams.add("Passwd", "hackingdays");
	    queryParams.add("service", "cp");
	    queryParams.add("source", "senacor-hackingdays-1");	    
	    wr = wr.queryParams(queryParams);	    

	    WebResource.Builder b = wr.entity("");
	    b = b.type("application/x-www-form-urlencoded");
	    b = b.header("GData-Version", "3.0"); 

	    ClientResponse cr = b.post(ClientResponse.class);	    
	    int retCode = cr.getClientResponseStatus().getStatusCode();

	    System.out.println("HTTP response: "+retCode+"\n");

	    if (cr.getClientResponseStatus() == ClientResponse.Status.OK) {
	    	System.out.println("login succeeded");
	    	String retval = cr.getEntity(String.class);	    	
	    	System.out.println("\n\nretval:\n"+retval);	    	
	    	BufferedReader br = new BufferedReader(new StringReader(retval));

	    	String line = null;
	    	do {
	    		try {
					line = br.readLine();
					// System.out.println("l: "+line);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
	    		if ((line != null) && line.startsWith(AUTH_PREFIX)) {
	    			authToken = line.substring(AUTH_PREFIX.length());
	    			System.out.println("Auth-Token:"+authToken);
	    			break;
	    		}
	    	} while (line != null);
	    } else if (cr.getClientResponseStatus() == ClientResponse.Status.FORBIDDEN) {
	    	System.err.println("login failed - forbidden");
	    } else {
	    	System.out.println("login failed - "+cr.getClientResponseStatus().getReasonPhrase());	    	
	    }
	}

	public String getAuthToken() {
		return authToken;
	}

	public String getAuthHeader() {
		return "GoogleLogin auth="+getAuthToken();
	}
}

