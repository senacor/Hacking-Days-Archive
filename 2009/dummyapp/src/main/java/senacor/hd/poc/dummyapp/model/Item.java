package senacor.hd.poc.dummyapp.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="items")
@XmlRootElement
public class Item {
	public enum ItemState { ACTIVE, INACTIVE };

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Integer itemNo;
	@Enumerated
	private ItemState itemState;
	private String beschreibung;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getItemNo() {
		return itemNo;
	}
	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}
	public ItemState getItemState() {
		return itemState;
	}
	public void setItemState(ItemState itemState) {
		this.itemState = itemState;
	}
	public String getBeschreibung() {
		return beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
}
