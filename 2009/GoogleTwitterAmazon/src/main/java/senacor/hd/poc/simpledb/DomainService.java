package senacor.hd.poc.simpledb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class DomainService extends AmazonService {
	public DomainService() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		super();
	}

	/**
	 * https://sdb.amazonaws.com/
?Action=CreateDomain
&AWSAccessKeyId=[valid access key id]
&DomainName=MyDomain
&SignatureVersion=2
&SignatureMethod=HmacSHA256
&Timestamp=2007-06-25T15%3A01%3A28-07%3A00
&Version=2009-04-15
&Signature=[valid signature]
	 * @param domainName
	 */
	public void createDomain(String domainName) {
		System.out.println("\ncreating domain: "+domainName);
		
		Client client = Client.create();	
		WebResource wr = client.resource("https://sdb.eu-west-1.amazonaws.com/");

		Map<String, String> params = new HashMap<String, String>();
		params.put("Action", "CreateDomain");
		params.put("DomainName", domainName);
		try {
			System.out.println(callAWS(params));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// params.put("AWSAccessKeyId", awsAccessKeyId);
		// params.put("Timestamp", timestamp());
		
		/*
		String sig = sign(params);
		
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("AWSAccessKeyId", "AKIAJ2RGFUTFGIF3DZDA");
		for (Entry<String, String> entry : params.entrySet()) {
			queryParams.add(entry.getKey(), entry.getValue());
		}
		queryParams.add("action", "CreateDomain");
		queryParams.add("action", "CreateDomain");
	    wr = wr.queryParams(queryParams);	    

	    WebResource.Builder b = wr.entity("");
	    b = b.type("application/x-www-form-urlencoded");
	    // b = b.header("Authorization", "Basic "+twitAuth.getBase64credentials()); 

	    ClientResponse cr = b.post(ClientResponse.class);	    
	    if (cr.getClientResponseStatus() == ClientResponse.Status.OK) {
	    	System.out.println("HTTP OK - tweet posted\n");
	    } else {
	    	System.out.println("twitter update failed - "+cr.getClientResponseStatus().getReasonPhrase());
	    	System.out.println(cr.getEntity(String.class));
	    }
        */
	}
}
