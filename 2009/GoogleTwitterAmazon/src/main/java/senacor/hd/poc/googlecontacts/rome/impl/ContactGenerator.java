package senacor.hd.poc.googlecontacts.rome.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jdom.Element;
import org.jdom.Namespace;

import senacor.hd.poc.googlecontacts.rome.Contact;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.io.ModuleGenerator;

public class ContactGenerator implements ModuleGenerator {
	private static final Set NAMESPACES;
	private static final Namespace GD_NAMESPACE = Namespace.getNamespace("gd",
			Contact.URI);
	static {
		Set<Namespace> namespaces = new HashSet<Namespace>();
		namespaces.add(GD_NAMESPACE);
		NAMESPACES = Collections.unmodifiableSet(namespaces);
	}

	public String getNamespaceUri() {
		return Contact.URI;
	}

	public Set getNamespaces() {
		return NAMESPACES;
	}

	public void generate(Module module, Element rootElem) {
		Contact contact = (Contact) module;

		// Vorname, Nachname
		if ((contact.getVorname() != null) || (contact.getNachname() != null)) {
			Element nameElem = new Element("name", GD_NAMESPACE);
			if (contact.getVorname() != null) {
				Element givenNameElem = new Element("givenName", GD_NAMESPACE);
				givenNameElem.addContent(contact.getVorname());
				nameElem.addContent(givenNameElem);
			}
			if (contact.getNachname() != null) {
				Element familyNameElem = new Element("familyName", GD_NAMESPACE);
				familyNameElem.addContent(contact.getNachname());
				nameElem.addContent(familyNameElem);
			}
			rootElem.addContent(nameElem);
		}
		
		// Strasse, Hausnummer, PLZ, Ort
		if ((contact.getStrasse() != null) || (contact.getOrt() != null)) {
			Element addressElem = new Element("structuredPostalAddress", GD_NAMESPACE);
			addressElem.setAttribute("primary", "false");
			addressElem.setAttribute("rel", "http://schemas.google.com/g/2005#home");
			if (contact.getStrasse() != null) {
				String strasse = contact.getStrasse();
				if (contact.getHausnummer() != null) {
					strasse = strasse+" "+contact.getHausnummer();
				}
				Element streetElem = new Element("street", GD_NAMESPACE);
				streetElem.addContent(strasse);
				addressElem.addContent(streetElem);
			}
			if (contact.getPlz() != null) {
				Element zipElem = new Element("postcode", GD_NAMESPACE);
				zipElem.addContent(contact.getPlz());
				addressElem.addContent(zipElem);
			}
			if (contact.getOrt() != null) {
				Element cityElem = new Element("city", GD_NAMESPACE);
				cityElem.addContent(contact.getOrt());
				addressElem.addContent(cityElem);
			}
			rootElem.addContent(addressElem);
		}
		
		// email
		if (contact.getEmail() != null) {
			Element mailElem = new Element("email", GD_NAMESPACE);
			mailElem.setAttribute("address", contact.getEmail());
			mailElem.setAttribute("primary", "false");
			mailElem.setAttribute("rel", "http://schemas.google.com/g/2005#home");
			rootElem.addContent(mailElem);
		}
		
		// Telefon Privat
		if (contact.getTelefonPrivat() != null) {
			Element phonePrivElem = new Element("phoneNumber", GD_NAMESPACE);
			phonePrivElem.setAttribute("rel", "http://schemas.google.com/g/2005#home");
			phonePrivElem.addContent(contact.getTelefonPrivat());
			rootElem.addContent(phonePrivElem);
		}
		
		// Telefon Arbeit
		if (contact.getTelefonArbeit() != null) {
			Element phoneWorkElem = new Element("phoneNumber", GD_NAMESPACE);
			phoneWorkElem.setAttribute("rel", "http://schemas.google.com/g/2005#work");
			phoneWorkElem.addContent(contact.getTelefonArbeit());
			rootElem.addContent(phoneWorkElem);
		}
	}
}
