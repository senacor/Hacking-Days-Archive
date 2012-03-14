package senacor.hd.domain;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement
public class Artwork {
	
	MaterialEnum material;
	TechniqueEnum technique;
	String description;
	String name;
	String artistName;
	String estimatedPrice;
	Date creationDate;
	String pictureUrl;
	Customer customer;
	List<Order> orders = new ArrayList<Order>();

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getEstimatedPrice() {
		return estimatedPrice;
	}

	public void setEstimatedPrice(String estimatedPrice) {
		this.estimatedPrice = estimatedPrice;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String flickrPhotoId) {
		this.pictureUrl = flickrPhotoId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MaterialEnum getMaterial() {
		return material;
	}

	public void setMaterial(MaterialEnum material) {
		this.material = material;
	}

	public TechniqueEnum getTechnique() {
		return technique;
	}

	public void setTechnique(TechniqueEnum technique) {
		this.technique = technique;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
