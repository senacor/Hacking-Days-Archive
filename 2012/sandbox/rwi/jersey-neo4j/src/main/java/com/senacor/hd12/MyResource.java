
package com.senacor.hd12;

import com.senacor.hd12.network.TheSenacorNetwork;
import com.senacor.hd12.network.util.Populator;
import com.senacor.hd12.network.util.SpringApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/myresource")
public class MyResource {
    /**
     * http://localhost:8080/jersey-neo4j/jersey/myresource
     *
     * Method processing HTTP GET requests, producing "text/plain" MIME media type.
     *
     * @return String that will be send back as a response of type "text/plain".
     */
    @GET 
    @Produces("text/plain")
    public String getIt() {
        TheSenacorNetwork tns = (TheSenacorNetwork) SpringApplicationContext.getBean(TheSenacorNetwork.class);
        System.err.println("TNS is now: "+tns);
        tns.startNetworkBuilding();
        return "Hi there!";
    }
}
