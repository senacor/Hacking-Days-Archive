package senacor.hd.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum AuctionState {
	CREATED, STARTED, HAS_BIDS, FINISHED, HAS_SUCCESSOR
}
