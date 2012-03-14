package senacor.hd.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Bid {

	BigDecimal price;
	String comment;
	Date creationDate;
	Faker faker;
	Artwork artwork;
	Auction auction;

	
	public Artwork getArtwork() {
		return artwork;
	}

	public void setArtwork(Artwork artefact) {
		this.artwork = artefact;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}
}
