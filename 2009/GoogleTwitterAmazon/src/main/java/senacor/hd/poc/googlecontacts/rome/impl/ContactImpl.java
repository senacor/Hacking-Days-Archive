package senacor.hd.poc.googlecontacts.rome.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import senacor.hd.poc.googlecontacts.rome.Contact;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.feed.module.ModuleImpl;

@SuppressWarnings("serial")
public class ContactImpl extends ModuleImpl implements Contact, Module {
	private String etag;
	
	private String vorname;
	private String nachname;
	private String strasse;
	private String hausnummer;
	private String plz;
	private String ort;
	private String email;
	private String telefonPrivat;
	private String telefonArbeit;
	
	private Map links = new HashMap();

	public ContactImpl() {
		super(ModuleImpl.class, URI);
	}

	public void copyFrom(Object obj) {
		ContactImpl other = (ContactImpl) obj;

		setEtag(other.getEtag());
		
		setVorname(other.getVorname());
		setNachname(other.getNachname());
		setStrasse(other.getStrasse());
		setHausnummer(other.getHausnummer());
		setPlz(other.getPlz());
		setOrt(other.getOrt());
		setEmail(other.getEmail());
		setTelefonPrivat(other.getTelefonPrivat());
		setTelefonArbeit(other.getTelefonArbeit());
		
		links.putAll(other.getLinks());
	}

	@SuppressWarnings("unchecked")
	public Class getInterface() {
		return Module.class;
	}

	public String getEtag() {
		return etag;
	}

	public void setEtag(String etag) {
		this.etag = etag;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getHausnummer() {
		return hausnummer;
	}

	public void setHausnummer(String hausnummer) {
		this.hausnummer = hausnummer;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefonPrivat() {
		return telefonPrivat;
	}

	public void setTelefonPrivat(String telefonPrivat) {
		this.telefonPrivat = telefonPrivat;
	}

	public String getTelefonArbeit() {
		return telefonArbeit;
	}

	public void setTelefonArbeit(String telefonArbeit) {
		this.telefonArbeit = telefonArbeit;
	}

	public Map getLinks() {
		return links;
	}

	public void setLinks(Map links) {
		this.links = links;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("ETag          : "+getEtag()).append("\n");
		sb.append("===").append("\n");
		sb.append("Vorname       : "+getVorname()).append("\n");
		sb.append("Nachname      : "+getNachname()).append("\n");
		sb.append("Strasse       : "+getStrasse()).append("\n");
		sb.append("Hausnummer    : "+getHausnummer()).append("\n");
		sb.append("PLZ           : "+getPlz()).append("\n");
		sb.append("Ort           : "+getOrt()).append("\n");
		sb.append("eMail         : "+getEmail()).append("\n");
		sb.append("Telefon Privat: "+getTelefonPrivat()).append("\n");
		sb.append("Telefon Arbeit: "+getTelefonArbeit()).append("\n");
		sb.append("===").append("\n");

		Iterator<String> it = links.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			sb.append(key).append(" -> ").append(links.get(key)).append("\n");
		}
		
		return sb.toString(); 
	}
}
