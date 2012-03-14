package senacor.hd.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Order {

	BigDecimal price;
	String comment;
	OrderState orderState;
	Customer customer;
	Faker faker;
	Artwork artwork;
	List<Auction> auctions = new ArrayList<Auction>();
	
	public Auction getLastAuction() {
		if(auctions != null && auctions.size() > 0) {
			return auctions.get(auctions.size()-1);
		}
		return null; 
	}
	
	public List<Auction> getAuctions() {
		return auctions;
	}

	public void setAuctions(List<Auction> auctions) {
		this.auctions = auctions;
	}

	public OrderState getOrderState() {
		return orderState;
	}
	
	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
	}
	
	public Artwork getArtwork() {
		return artwork;
	}

	public void setArtwork(Artwork artefact) {
		this.artwork = artefact;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Faker getFaker() {
		return faker;
	}

	public void setFaker(Faker faker) {
		this.faker = faker;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String googleContactUrl) {
		this.comment = googleContactUrl;
	}
}
