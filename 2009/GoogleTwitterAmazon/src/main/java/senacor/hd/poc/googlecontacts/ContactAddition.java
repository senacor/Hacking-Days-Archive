package senacor.hd.poc.googlecontacts;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import senacor.hd.poc.googlecontacts.rome.Contact;
import senacor.hd.poc.googlecontacts.rome.impl.ContactImpl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.WireFeedOutput;

public class ContactAddition {
	private Authentication gooAuth = null; 

	public ContactAddition(Authentication gooAuth) {
		super();
		this.gooAuth = gooAuth;
	}

	public void addContact() {
	    SyndFeed feed = new SyndFeedImpl();
	    feed.setFeedType("atom_1.0");
	    feed.setTitle("contact addition");

	    // create contact entry
	    SyndEntry entry = new SyndEntryImpl();

	    SyndContent content = new SyndContentImpl();
	    content.setValue("");
	    List contentList = new ArrayList();
	    contentList.add(content);
	    entry.setContents(contentList);

	    entry.setAuthor("hackingdays");	    

	    SyndCategory category= new SyndCategoryImpl();
	    category.setName("http://schemas.google.com/contact/2008#contact"); 
	    category.setTaxonomyUri("http://schemas.google.com/g/2005#kind"); 
	    List categories= new ArrayList();
	    categories.add(category);
	    entry.setCategories(categories); 

	    // create the module, populate and add to the item
	    Contact contact = new ContactImpl();
	    contact.setVorname("Ralph");
	    contact.setNachname("Winzinger");
	    contact.setStrasse("Helene-Grünberg-Straße");
	    contact.setHausnummer("40");
	    contact.setPlz("90480");
	    contact.setOrt("Nürnberg");
	    contact.setEmail("ralph.winzinger@senacor.com");
	    contact.setTelefonPrivat("+49911476762");
	    contact.setTelefonArbeit("+491728142483");
	    entry.getModules().add(contact);
	    feed.getEntries().add(entry);

	    StringWriter sw = null;
	    try {
			WireFeedOutput output = new WireFeedOutput();
			WireFeed wireFeed = feed.createWireFeed("atom_1.0");
			Document feedDoc = output.outputJDom(wireFeed);
			Element entryElement = (Element)feedDoc.getRootElement().getChildren("entry", Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom")).get(0);
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			sw = new StringWriter();
			outputter.output(entryElement, sw);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Client client = Client.create();	
		WebResource wr = client.resource("http://www.google.com/m8/feeds/contacts/default/full");		

	    WebResource.Builder b = null;

		// ###

		System.out.println("CONTENT:\n"+sw.toString());
    	b = wr.entity(sw.toString());

	    b = b.type("application/atom+xml");
	    b = b.header("Authorization", gooAuth.getAuthHeader());
	    b = b.header("GData-Version", "3.0"); 

		ClientResponse cr = b.post(ClientResponse.class);		

	    if (cr.getClientResponseStatus() == ClientResponse.Status.CREATED) {
	    	System.out.println("\naddition succeeded - created\n");
	    } else {
	    	System.out.println("addition failed - "+cr.getClientResponseStatus().getReasonPhrase());
	    	System.out.println(cr.getEntity(String.class));
	    }
	}
}

