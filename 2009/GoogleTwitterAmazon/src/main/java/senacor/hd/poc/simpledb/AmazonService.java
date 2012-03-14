package senacor.hd.poc.simpledb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AmazonService {

	private static final String UTF8_CHARSET = "UTF-8";
	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

	private static final String REQUEST_METHOD = "GET";
	private String endpoint = "sdb.eu-west-1.amazonaws.com"; // must be lowercase
	private String awsAccessKeyId = "AKIAJ2RGFUTFGIF3DZDA";
	private String awsSecretKey = "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";
	private String REQUEST_URI = "/"; // "/onca/xml";
	private SecretKeySpec secretKeySpec = null;
	private Mac mac = null;

	protected AmazonService() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		byte[] secretyKeyBytes = awsSecretKey.getBytes(UTF8_CHARSET);
		secretKeySpec = new SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM);
		mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
		mac.init(secretKeySpec);		
	}
	/*
	public AmazonService(String url, String uri, String accessKey, String secretKey) throws UnsupportedEncodingException, NoSuchAlgorithmException,
			InvalidKeyException {
		endpoint = url;
		REQUEST_URI = uri;
		awsAccessKeyId = accessKey;
		awsSecretKey = secretKey;
		byte[] secretyKeyBytes = awsSecretKey.getBytes(UTF8_CHARSET);
		secretKeySpec = new SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM);
		mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
		mac.init(secretKeySpec);
	}
	*/

	public String callAWS(Map<String, String> params) throws MalformedURLException, IOException {
		String result = "";
		String signed_url = sign(params);
		// call the url and retrive the result
		System.out.println(signed_url);
		URL az = new URL(signed_url);
		URLConnection ac = az.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(ac.getInputStream()));

		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			result += inputLine;
		}
		in.close();

		return result;
	}

	protected String sign(Map<String, String> params) {
		params.put("AWSAccessKeyId", awsAccessKeyId);
		params.put("Timestamp", timestamp());

		SortedMap<String, String> sortedParamMap = new TreeMap<String, String>(params);
		String canonicalQS = canonicalize(sortedParamMap);
		String toSign = REQUEST_METHOD + "\n" + endpoint + "\n" + REQUEST_URI + "\n" + canonicalQS;
		System.out.println(toSign);

		String hmac = hmac(toSign);
		String sig = percentEncodeRfc3986(hmac);
		String url = "http://" + endpoint + REQUEST_URI + "?" + canonicalQS + "&amp;Signature=" + sig;

		return url;
	}

	private String hmac(String stringToSign) {
		String signature = null;
		byte[] data;
		byte[] rawHmac;
		try {
			data = stringToSign.getBytes(UTF8_CHARSET);
			rawHmac = mac.doFinal(data);
			Base64 encoder = new Base64();
			signature = new String(encoder.encode(rawHmac));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(UTF8_CHARSET + " is unsupported!", e);
		}
		return signature;
	}

	protected String timestamp() {
		String timestamp = null;
		Calendar cal = Calendar.getInstance();
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
		timestamp = dfm.format(cal.getTime());
		return timestamp;
	}

	private String canonicalize(SortedMap<String, String> sortedParamMap) {
		if (sortedParamMap.isEmpty()) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();
		Iterator<Map.Entry<String, String>> iter = sortedParamMap.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry<String, String> kvpair = iter.next();
			buffer.append(percentEncodeRfc3986(kvpair.getKey()));
			buffer.append("=");
			buffer.append(percentEncodeRfc3986(kvpair.getValue()));
			if (iter.hasNext()) {
				buffer.append("&amp;");
			}
		}
		String cannoical = buffer.toString();
		return cannoical;
	}

	private String percentEncodeRfc3986(String s) {
		String out;
		try {
			out = URLEncoder.encode(s, UTF8_CHARSET).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
		} catch (UnsupportedEncodingException e) {
			out = s;
		}
		return out;
	}

}