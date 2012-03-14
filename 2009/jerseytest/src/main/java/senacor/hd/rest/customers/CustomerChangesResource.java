package senacor.hd.rest.customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import senacor.hd.poc.dummyapp.model.Customer;
import senacor.hd.poc.dummyapp.services.CustomerService;
import senacor.hd.poc.dummyapp.services.impl.CustomerServiceImpl;

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.feed.synd.SyndLink;
import com.sun.syndication.feed.synd.SyndLinkImpl;
import com.sun.syndication.io.WireFeedOutput;

@Path("/customers/changes")
public class CustomerChangesResource {
	private static Logger LOG = Logger.getLogger("CustomerChangesResource");
	private static CustomerService customerService = CustomerServiceImpl.getInstance();

	@GET
	@Produces("text/html")
	public String getCustomerChangesHTML() {
		LOG.info("getCustomerChanges - text/html");
		List<Customer> customers = customerService.getNewCustomers();
		
		StringBuffer retval = new StringBuffer();
		retval.append("<html>");
		retval.append("<head>");
		retval.append("<title>REST-Test</title>");
		retval.append("</head>");
		retval.append("<body>");
		retval.append("ten newest customers: ").append("<br/>");
		retval.append("<table border=1>");
		retval.append("<tr>");
		retval.append("<th>").append("ID").append("</th>");
		retval.append("<th>").append("Vorname").append("</th>");
		retval.append("<th>").append("Nachname").append("</th>");
		retval.append("<th>").append("Kunde seit").append("</th>");
		retval.append("</tr>");

		for (Customer customer : customers) {
			retval.append("<tr>");
			retval.append("<td>").append(customer.getId()).append("</td>");
			retval.append("<td>").append(customer.getVorname()).append("</td>");
			retval.append("<td>").append(customer.getNachname()).append("</td>");
			retval.append("<td>").append(customer.getKundeSeit()).append("</td>");
			retval.append("</tr>");			
		}

		retval.append("</table>");
		retval.append("</body>");
		retval.append("<html>");

		
		return retval.toString();
	}
	
	@GET
	@Produces("application/atom+xml")
	public String getCustomerChangesATOM() {
		LOG.info("getCustomerChanges - application/atom+xml");
		
		List<Customer> customers = customerService.getNewCustomers();

		SyndFeed feed = new SyndFeedImpl();
	    feed.setFeedType("atom_1.0");
	    feed.setTitle("contact addition");
	    feed.setUri("http://localhost:8080/resttest/customers/changes");
	    feed.setAuthor("ich");
	    feed.setPublishedDate(new Date());
	    
	    // create entries
	    
	    for (Customer customer : customers) {
		    SyndEntry entry = new SyndEntryImpl();

		    SyndContent content = new SyndContentImpl();
		    content.setValue("");
		    List contentList = new ArrayList();
		    contentList.add(content);
		    entry.setContents(contentList);

		    entry.setAuthor("hackingdays");	   
		    entry.setTitle(customer.getVorname()+" "+customer.getNachname());
		    entry.setUri("http://localhost:8080/resttest/customers/changes/"+customer.getId());
		    entry.setPublishedDate(customer.getKundeSeit());
		    
		    SyndContent desc = new SyndContentImpl();
		    desc.setValue("hallo test");
		    entry.setDescription(desc);
		    
		    SyndLink link = new SyndLinkImpl();
		    link.setRel("alternate");
		    link.setHref("www.wo.de");
		    
		    List links = new ArrayList();
		    links.add(link);
		    entry.setLinks(links); 

		    
		    feed.getEntries().add(entry);
		}
	    
		WireFeedOutput output = new WireFeedOutput();
		WireFeed wireFeed = feed.createWireFeed("atom_1.0");
	    try {
			return output.outputString(wireFeed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
