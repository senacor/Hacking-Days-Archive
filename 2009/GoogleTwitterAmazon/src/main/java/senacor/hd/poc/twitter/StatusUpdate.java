package senacor.hd.poc.twitter;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class StatusUpdate {
	private Authentication twitAuth = null;

	public StatusUpdate(Authentication twitAuth) {
		super();
		this.twitAuth = twitAuth;
	}
	
	public void createUpdate(String msg) {
		System.out.println("\ncreating tweet: "+msg);
		
		Client client = Client.create();	
		WebResource wr = client.resource("http://twitter.com/statuses/update.xml");

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("status", "unchanged");
	    wr = wr.queryParams(queryParams);	    

	    WebResource.Builder b = wr.entity("");
	    b = b.type("application/x-www-form-urlencoded");
	    b = b.header("Authorization", "Basic "+twitAuth.getBase64credentials()); 

	    ClientResponse cr = b.post(ClientResponse.class);	    
	    if (cr.getClientResponseStatus() == ClientResponse.Status.OK) {
	    	System.out.println("HTTP OK - tweet posted\n");
	    } else {
	    	System.out.println("twitter update failed - "+cr.getClientResponseStatus().getReasonPhrase());
	    	System.out.println(cr.getEntity(String.class));
	    }
	}
}
