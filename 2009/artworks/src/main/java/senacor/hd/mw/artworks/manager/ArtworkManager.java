package senacor.hd.mw.artworks.manager;


import senacor.hd.domain.Artwork;
import senacor.hd.domain.MaterialEnum;
import senacor.hd.domain.TechniqueEnum;
import senacor.hd.mw.artworks.flickr.FlickrAccess;
import senacor.hd.mw.artworks.flickr.FlickrResponseParser;

import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hannes
 * Date: Nov 27, 2009
 * Time: 5:14:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArtworkManager {
    FlickrAccess flickrAccess = new FlickrAccess();
    FlickrResponseParser responseParser = new FlickrResponseParser();
    UriInfo uriInfo;

    public ArtworkManager(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public List<Artwork> listAllArtworks() {
        String flickrResponse = flickrAccess.getFavorites(null);
        List<Artwork> artworks = responseParser.parseSearchResult(flickrResponse);

        for (Artwork artwork : artworks) {
            artwork.setDescription("für eine Hand voll Möpse");
            artwork.setThumbnailUrl("http://data.blogg.de/2328/images/toronto.jpg");
            artwork.setArtistName("Mopi S.");
            artwork.setCreationDate(new Date());
            artwork.setDescription("für eine Hand voll Möpse");
            artwork.setEstimatedPrice(new BigDecimal("1000000.00"));
            artwork.setMaterial(MaterialEnum.OILPAINT);
            artwork.setTechnique(TechniqueEnum.CLASSIC);
            URI uri = uriInfo.getAbsolutePathBuilder().path("..")
                    .path("artwork").path(artwork.getId().toString()).build().normalize();
            artwork.setUri(uri.toString());
            artwork.setId(null);
            // HACK!!
        }

        return artworks;
    }

    private Artwork getDummy() {
        Artwork artwork = new Artwork();
        artwork.setName("James Danger Rodney");
        artwork.setArtistName("Mopi S.");
        artwork.setCreationDate(new Date());
        artwork.setDescription("für eine Hand voll Möpse");
        artwork.setEstimatedPrice(new BigDecimal("1000000.00"));
        artwork.setMaterial(MaterialEnum.OILPAINT);
        artwork.setTechnique(TechniqueEnum.CLASSIC);
        artwork.setThumbnailUrl("http://data.blogg.de/2328/images/toronto.jpg");
        //artwork.setId(new Long(999)); -- no, the client does not see an ID -- only the URI
        URI uri = uriInfo.getAbsolutePathBuilder().path("..")
                .path("artwork").path("999").build().normalize();

        artwork.setUri(uri.toString());

        return artwork;
    }

    public Artwork getArtworkInfo(Long id) {
        String responseFromFlickr = flickrAccess.getPhotoInfo(id.toString());
        System.out.println("responseFromFlickr = " + responseFromFlickr);

        FlickrResponseParser parser = new FlickrResponseParser();
        Artwork artwork = parser.parseGetInfo(responseFromFlickr);

        return artwork;
    }

    public void update(Artwork updatedArtwork) {
        System.out.println("updatedArtwork = " + updatedArtwork);
    }


    public void delete(Long id) {
        System.out.println("deleted id = " + id);
    }
}
