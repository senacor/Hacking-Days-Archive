package senacor.hd.mw.artworks.resources;

import senacor.hd.bdl.messaging.MessageFailedException;
import senacor.hd.bdl.messaging.MessagingService;
import senacor.hd.domain.Artwork;
import senacor.hd.mw.artworks.manager.ArtworkManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
@Path("/artwork")
public class ArtworkResource {

	private MessagingService messagingService;
	
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Artwork getInfo(@PathParam("id") Long id, @Context UriInfo uriInfo) {

    	if(messagingService != null) { // if we run locally in eclipse, then this can't work...
    		try {
    			messagingService.send("Artwork " + id + " requested.", null);
			} catch (MessageFailedException e) {
				e.printStackTrace();
			}
    	} else {
    		System.out.println("no twittering");
    	}
        
    	return new ArtworkManager(uriInfo).getArtworkInfo(id);
    }
    
	public void setMessagingService(MessagingService messagingService) {
		this.messagingService = messagingService;
	}

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void update(@PathParam("id") Long id, Artwork artwork, @Context UriInfo uriInfo) {
        Artwork updatedArtwork = null;

        if(messagingService != null) { // if we run locally in eclipse, then this can't work...
	    	try {
				messagingService.send("New Artwork added.", null);
			} catch (MessageFailedException e) {
				e.printStackTrace();
			}
        } else {
    		System.out.println("no twittering");
    	}
        
        new ArtworkManager(uriInfo).update(updatedArtwork);
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        new ArtworkManager(null).delete(id);
    }
}