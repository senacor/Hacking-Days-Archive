package senacor.hd.sr.registry.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceList {
	@XmlElement
	private List<Service> services = new ArrayList<Service>();
	
	public ServiceList() {
	};
	
	public List<Service> getServices() {
		return services;
	}
}
