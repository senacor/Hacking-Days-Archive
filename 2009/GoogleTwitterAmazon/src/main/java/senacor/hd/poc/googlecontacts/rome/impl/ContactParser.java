package senacor.hd.poc.googlecontacts.rome.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import senacor.hd.poc.googlecontacts.rome.Contact;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.io.ModuleParser;

public class ContactParser implements ModuleParser {
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

    public Module parse(Element rootElem) { 
        ContactImpl contact = new ContactImpl(); 
        
        Attribute etagAttr = rootElem.getAttribute("etag", GD_NAMESPACE);
        if (etagAttr != null) {
        	contact.setEtag(etagAttr.getValue());
        }
        
        Element nameElem = rootElem.getChild("name", GD_NAMESPACE);
        if (nameElem != null) {
        	Element givenNameElem = nameElem.getChild("givenName", GD_NAMESPACE);
        	if (givenNameElem != null) {
        		contact.setVorname(givenNameElem.getValue());
        	}
        	Element familyNameElem = nameElem.getChild("familyName", GD_NAMESPACE);
        	if (familyNameElem != null) {
        		contact.setNachname(familyNameElem.getValue());
        	}
        }

        return contact; 
    }
}

