package senacor.hd.mw.artworks.flickr;

import org.junit.Assert;
import org.junit.Test;
import senacor.hd.domain.Artwork;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hannes
 * Date: Nov 28, 2009
 * Time: 4:12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlickrTest {

    @Test
    public void testFlickrListResponseParsing() {
        FlickrAccess flickrAccess = new FlickrAccess();

        String xmlResponse = flickrAccess.getFavorites(null);

        List<Artwork> artworks = new FlickrResponseParser().parseSearchResult(xmlResponse);
        Long photoId = artworks.get(0).getId();

        xmlResponse = flickrAccess.getPhotoInfo(photoId.toString());

        Artwork artwork = new FlickrResponseParser().parseGetInfo(xmlResponse);
        Assert.assertNotNull(artwork.getPictureUrl());
        Assert.assertNotNull(artwork.getName());

        System.out.println("artwork.getPictureUrl() = " + artwork.getPictureUrl());
    }
}
