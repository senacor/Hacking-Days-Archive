package senacor.hd.poc.googlecontacts;

import java.util.List;

import senacor.hd.poc.googlecontacts.rome.Contact;
import senacor.hd.poc.googlecontacts.rome.impl.ContactImpl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;

public class ContactDeletion {
	private Authentication gooAuth = null;

	public ContactDeletion(Authentication gooAuth) {
		super();
		this.gooAuth = gooAuth;
	}


	public void deleteContact(Contact contact) {
		String editLink = (String) ((ContactImpl)contact).getLinks().get("edit");
		if (editLink == null) {
			return;
		}
		
		Client client = Client.create();
		WebResource.Builder builder = client.resource(editLink).getRequestBuilder();
		builder = builder.header("Authorization", gooAuth.getAuthHeader());
		builder = builder.header("GData-Version", "3.0");
		builder = builder.header("If-Match", ((ContactImpl)contact).getEtag());

		ClientResponse deletionResponse = builder.delete(ClientResponse.class);

		if (deletionResponse.getClientResponseStatus() != Status.OK) {
			System.out.println(">> " + deletionResponse.getClientResponseStatus().getReasonPhrase());
			System.out.println(">> " + deletionResponse.getEntity(String.class));
		} else {
			System.out.println("contact '"+contact.getVorname()+" "+contact.getNachname()+"' deleted");
		}
	}

	public void deleteContacts(List<Contact> contacts) {
		for (Contact contact : contacts) {
			deleteContact(contact);
		}
	}
}
