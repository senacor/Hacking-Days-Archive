package senacor.hd.poc.dummyapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="orders")
@XmlRootElement
public class Order {
	public enum OrderState { ACTIVE, INACTIVE };
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Integer orderNo;
	@Enumerated
	private OrderState orderState;
	@Temporal(TemporalType.DATE)
	private Date eingang;
	@OneToMany(cascade=CascadeType.ALL)
	private List<Item> positionen = new ArrayList<Item>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public OrderState getOrderState() {
		return orderState;
	}
	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
	}
	public Date getEingang() {
		return eingang;
	}
	public void setEingang(Date eingang) {
		this.eingang = eingang;
	}
	public List<Item> getPositionen() {
		return positionen;
	}
	public void setPositionen(List<Item> positionen) {
		this.positionen = positionen;
	}
}
