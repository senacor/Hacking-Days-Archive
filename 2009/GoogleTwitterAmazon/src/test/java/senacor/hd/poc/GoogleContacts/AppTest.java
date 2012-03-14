package senacor.hd.poc.GoogleContacts;

import com.sun.jersey.core.util.Base64;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testBasicAuth() {
    	String username="Aladdin";
    	String password="open sesame";
    	
    	String expectedResult="Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==";
    	
        assertEquals(expectedResult, createBasicAuthHeader(username, password));
    }
    
    private String createBasicAuthHeader(String username, String password) {
    	byte[] hash = Base64.encode(username+":"+password);
    	
    	System.out.println("Basic "+new String(hash));
    	return "Basic "+new String(hash);
    }
}
