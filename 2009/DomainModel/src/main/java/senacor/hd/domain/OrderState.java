package senacor.hd.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum OrderState {
	PENDING, IN_AUCTION, REFUSED, DELETED, ASSIGNED, ACCEPTED, PAID, IN_WORK, FINISHED
}
