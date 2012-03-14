package senacor.hd.poc.googlecontacts;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import senacor.hd.poc.googlecontacts.rome.Contact;
import senacor.hd.poc.googlecontacts.rome.impl.ContactImpl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndLink;
import com.sun.syndication.io.SyndFeedInput;

public class ContactList {
	private Authentication gooAuth = null;

	public ContactList(Authentication gooAuth) {
		super();
		this.gooAuth = gooAuth;
	}

	public List<Contact> retrieveContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		
		Client client = Client.create();	
		WebResource.Builder builder = client.resource("http://www.google.com/m8/feeds/contacts/default/full").getRequestBuilder();		
		builder = builder.header("Authorization", gooAuth.getAuthHeader());
		builder = builder.header("GData-Version", "3.0"); 
		ClientResponse cr = builder.get(ClientResponse.class);		

	    if (cr.getClientResponseStatus() == ClientResponse.Status.OK) {
	    	System.out.println("\nretrieval succeeded\n");

	    	SyndFeed feed = null;
	    	try {
	    		SyndFeedInput input = new SyndFeedInput();
		    	feed = input.build(new InputStreamReader(cr.getEntityInputStream()));
			} catch (Throwable t) {
				t.printStackTrace();
			}

			// System.out.println("received Feed-Type: "+feed.getFeedType());

			List entries = feed.getEntries();
			Iterator entIt = entries.iterator();
			while (entIt.hasNext()) {
				SyndEntry entry = (SyndEntry)entIt.next();
				Contact contact = (Contact) entry.getModule(Contact.URI);
				if (contact != null) {
					List<SyndLink> links = entry.getLinks();
					for (SyndLink link : links) {
						((ContactImpl)contact).getLinks().put(link.getRel(), link.getHref());
					}
					contacts.add(contact);
				}
			}
	    } else {
	    	System.out.println("retrieve failed - "+cr.getClientResponseStatus().getReasonPhrase());
	    }
	    
	    return contacts;
	}
}

