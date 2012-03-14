package senacor.hd.sr.registry.client.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Service {
	
	private String name;
	private String uri;
	
	public Service() {
	}
	
	public Service(String name, String uri) {
		super();
		this.name = name;
		this.uri = uri;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
