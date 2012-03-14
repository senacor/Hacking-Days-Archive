package senacor.hd.poc;

import java.util.Date;
import java.util.List;

import senacor.hd.poc.flickr.FlickrAccess;
import senacor.hd.poc.googlecontacts.Authentication;
import senacor.hd.poc.googlecontacts.ContactAddition;
import senacor.hd.poc.googlecontacts.ContactDeletion;
import senacor.hd.poc.googlecontacts.ContactList;
import senacor.hd.poc.googlecontacts.rome.Contact;
// import senacor.hd.poc.simpledb.DomainService;
import senacor.hd.poc.twitter.StatusUpdate;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		try {
			FlickrAccess fa = new FlickrAccess();
			
			boolean createFrob = false;
			boolean authorizeApp = false;
			boolean retrieveToken = false;
			boolean doStuff = true;
			
			if (createFrob) {
				String frob = fa.createFrob();
				System.out.println("frob: "+frob);
			}
			if (authorizeApp) {
				String url = fa.getAuthUrl(fa.getFrob());
				System.out.println("url:"+url);				
			}
			if (retrieveToken) {
				String token = fa.createToken(fa.getFrob());
			}
			if (doStuff) {
				String favs = fa.getFavorites("hackingdays", fa.getToken());
				System.out.println("favs:\n"+favs);
				String photoInfo = fa.getPhotoInfo("562946325");
				System.out.println("photo:\n"+photoInfo);
				String photoSizes = fa.getPhotoSizes("562946325");
				System.out.println("photo:\n"+photoSizes);
			}

			
			/* Amazon ... geht nicht
			DomainService ds = new DomainService();
			ds.createDomain("testDomain");
		    */
			
			Authentication gooAuth = new Authentication();
			gooAuth.login();
			System.out.println("Auth-Header: " + gooAuth.getAuthHeader());

			// setProxy(true);
						
			ContactAddition ca = new ContactAddition(gooAuth);
			ca.addContact();

			ContactList cl = new ContactList(gooAuth);
			List<Contact> contacts = cl.retrieveContacts();
			for (Contact contact : contacts) {
				System.out.println("\nContact:\n"+contact);
			}
			
			ContactDeletion cd = new ContactDeletion(gooAuth);
			cd.deleteContacts(contacts);
			
			senacor.hd.poc.twitter.Authentication twitAuth = new senacor.hd.poc.twitter.Authentication("hackingdays", "hackingdays09");
			StatusUpdate su = new StatusUpdate(twitAuth);
			su.createUpdate("this is a tweet - "+(new Date()).toString());
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
		}
	}
}
