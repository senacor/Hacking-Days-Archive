package senacor.hd.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Faker extends Customer {
	
	String material;
	String technique;
	String description;
	List<Order> orderTasks = new ArrayList<Order>();

	public List<Order> getOrderTasks() {
		return orderTasks;
	}

	public void setOrderTasks(List<Order> orderTasks) {
		this.orderTasks = orderTasks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTechnique() {
		return technique;
	}

	public void setTechnique(String googleContactUrl) {
		this.technique = googleContactUrl;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String password) {
		this.material = password;
	}
}
