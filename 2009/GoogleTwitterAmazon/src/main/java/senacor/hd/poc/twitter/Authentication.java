package senacor.hd.poc.twitter;

import com.sun.jersey.core.util.Base64;

public class Authentication {
	private String base64credentials = null;
	
	public Authentication(String username, String password) {
		byte[] hash = Base64.encode(username+":"+password);
    	
    	base64credentials = new String(hash);
	}

	public String getBase64credentials() {
		return base64credentials;
	}
}
