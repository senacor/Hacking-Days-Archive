package senacor.hd.mw.artworks.flickr;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

	public class FlickrAccess {
		private final static String API_KEY = "582d861840e0ac5dd08a74026a969517";
		private final static String SECRET_KEY = "2d0972cd57ab6d90";
		
		private final static String FROB = "72157622657364265-646e867a8c9e80e5-383555";
		private final static String TOKEN = "72157622657182465-11f92fd1fdd1701b";
		
		
		public String getUserId(String username) {
			System.out.println("getting userId for "+username);
			
			Client client = Client.create();	
			WebResource wr = client.resource("http://api.flickr.com/services/rest/");		

			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("method", "flickr.people.findByUsername");
		    queryParams.add("api_key", API_KEY);
		    queryParams.add("username", username);
		    wr = wr.queryParams(queryParams);	    
		    WebResource.Builder b = wr.entity("");
		    
		    String retval = b.get(String.class);
		    System.out.println("retval:\n"+retval);

		    return retval;
		}
		
		public String getPhotosets(String username) {		
			String userId = "44152036@N02"; // getUserId(username);
			
			System.out.println("getting photosets for user_id "+userId);
			
			Client client = Client.create();	
			WebResource wr = client.resource("http://api.flickr.com/services/rest/");		

			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("method", "flickr.galleries.getList");
		    queryParams.add("api_key", API_KEY);
		    queryParams.add("user_id", userId);
		    wr = wr.queryParams(queryParams);	    
		    WebResource.Builder b = wr.entity("");
		    
		    String retval = b.get(String.class);
		    System.out.println("retval:\n"+retval);
			
		    return retval;
		}
		
		public String getFavorites(String username) {
			String userId = "44152036@N02"; // getUserId(username);
			
			System.out.println("getting favorites for user_id "+userId);
			
			Client client = Client.create();	
			WebResource wr = client.resource("http://api.flickr.com/services/rest/");

			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("method", "flickr.favorites.getList");
		    queryParams.add("api_key", API_KEY);
		    queryParams.add("user_id", userId);
		    queryParams.add("auth_token", TOKEN);
		    queryParams.add("api_sig", getSignature(queryParams));
		    
		    wr = wr.queryParams(queryParams);
		    WebResource.Builder b = wr.entity("");
		    
		    String retval = b.get(String.class);
		    System.out.println("retval:\n"+retval);
			
		    return retval;
		}
		
		public String getPhotoInfo(String photoId) {
			Client client = Client.create();	
			WebResource wr = client.resource("http://api.flickr.com/services/rest/");		

			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("method", "flickr.photos.getInfo");
		    queryParams.add("api_key", API_KEY);
		    queryParams.add("photo_id", photoId);
		    wr = wr.queryParams(queryParams);	    
		    WebResource.Builder b = wr.entity("");
		    
		    String retval = b.get(String.class);
		    System.out.println("retval:\n"+retval);

		    return retval;		
		}

		public String getPhotoSizes(String photoId) {
			Client client = Client.create();	
			WebResource wr = client.resource("http://api.flickr.com/services/rest/");		

			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("method", "flickr.photos.getSizes");
		    queryParams.add("api_key", API_KEY);
		    queryParams.add("photo_id", photoId);
		    wr = wr.queryParams(queryParams);	    
		    WebResource.Builder b = wr.entity("");
		    
		    String retval = b.get(String.class);
		    System.out.println("retval:\n"+retval);

		    return retval;		
		}

		public String getToken() {
			System.out.println("getting token - hardcoded");
			return TOKEN;
		}
		
		public String createToken(String frob) {
			System.out.println("creating token");
			
			Client client = Client.create();	
			WebResource wr = client.resource("http://api.flickr.com/services/rest/");		

			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("method", "flickr.auth.getToken");
		    queryParams.add("api_key", API_KEY);
		    queryParams.add("frob", frob);
		    // add signature
		    queryParams.add("api_sig", getSignature(queryParams));
		    
		    wr = wr.queryParams(queryParams);	    
		    
		    WebResource.Builder b = wr.entity("");
		    
		    String retval = b.get(String.class);
		    System.out.println("retval:\n"+retval);
			
		    return retval;
		}
		
		public String getFrob() {
			System.out.println("getting frob - hardcoded!");
			return FROB;
		}
		
		public String createFrob() {
			System.out.println("creating frob");
			
			Client client = Client.create();	
			WebResource wr = client.resource("http://api.flickr.com/services/rest/");		

			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("method", "flickr.auth.getFrob");
		    queryParams.add("api_key", API_KEY);
		    
		    String sig = getSignature(queryParams);
		    queryParams.add("api_sig", sig);
		    
		    wr = wr.queryParams(queryParams);	    
		    
		    WebResource.Builder b = wr.entity("");
		    
		    String retval = b.get(String.class);
		    System.out.println("retval:\n"+retval);
			
		    return retval;
		}

		public String getAuthUrl(String frob) {
			// http://flickr.com/services/auth/?api_key=[api_key]&perms=[perms]&frob=[frob]&api_sig=[api_sig]
			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("perms", "read");
		    queryParams.add("api_key", API_KEY);
		    queryParams.add("frob", frob);
		    
		    return "http://flickr.com/services/auth/?api_key="+API_KEY+"&perms=read&frob="+frob+"&api_sig="+getSignature(queryParams);
		}

		private String getSignature(MultivaluedMap<String, String> queryParams) {
			List paramNameList = new ArrayList();
			paramNameList.addAll(queryParams.keySet());
			Collections.sort(paramNameList);
			
			StringBuffer sb = new StringBuffer(SECRET_KEY);
			Iterator<String> it = paramNameList.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				sb.append(key).append(queryParams.getFirst(key));
			}

			System.out.println("string2sign: "+sb);
			
			String md5Text = null;
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				md5Text = new BigInteger(1, digest.digest((sb.toString()).getBytes())).toString(16);
			} catch (Exception e) {
				System.out.println("Error in call to MD5");
			}		
			if (md5Text.length() == 31) {
	            md5Text = "0" + md5Text;
	        }
			System.out.println("MD5-hash   : " + md5Text);
			
			return md5Text;
		}
	}
