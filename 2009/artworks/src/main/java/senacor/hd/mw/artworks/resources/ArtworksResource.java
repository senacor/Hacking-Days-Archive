package senacor.hd.mw.artworks.resources;

import senacor.hd.bdl.messaging.MessageFailedException;
import senacor.hd.bdl.messaging.MessagingService;
import senacor.hd.domain.Artwork;
import senacor.hd.domain.container.Artworks;
import senacor.hd.mw.artworks.manager.ArtworkManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("request")
@Path("/artworks")
public class ArtworksResource {
	
    private MessagingService messagingService;

	@GET
    @Produces("application/json")
    public Artworks listAll(@Context UriInfo uriInfo) {
        
		if(messagingService != null) { // if we run locally in eclipse, then this can't work because it's wired by osgi...
	    	try {
				messagingService.send("All Artworks requested.", null);
			} catch (MessageFailedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("no twittering");
		}
		
    	List<Artwork> artworks = new ArtworkManager(uriInfo).listAllArtworks();
        
        return new Artworks(artworks);
    }

	public void setMessagingService(MessagingService messagingService) {
		this.messagingService = messagingService;
	}
}
