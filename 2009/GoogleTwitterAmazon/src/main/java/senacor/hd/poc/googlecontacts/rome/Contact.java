package senacor.hd.poc.googlecontacts.rome;

public interface Contact {
	public static final String URI = "http://schemas.google.com/g/2005";

	/**
	 * Vorname
	 * Nachname
	 * 
	 * Strasse
	 * Hausnummer
	 * PLZ
	 * Ort
	 * 
	 * eMail
	 * Telefon Privat
	 * Telefon Arbeit
	 */
	
	public String getVorname();
	public void setVorname(String vorname);
	
	public String getNachname();
	public void setNachname(String nachname);	
	
	public String getStrasse();
	public void setStrasse(String strasse);	
	
	public String getHausnummer();
	public void setHausnummer(String hausnummer);	
	
	public String getPlz();
	public void setPlz(String plz);	
	
	public String getOrt();
	public void setOrt(String ort);	
	
	public String getEmail();
	public void setEmail(String email);	
	
	public String getTelefonPrivat();
	public void setTelefonPrivat(String telefonPrivat);	
	
	public String getTelefonArbeit();
	public void setTelefonArbeit(String telefonArbeit);		
}

