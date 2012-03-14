package senacor.hd.mw.artworks.resources;

import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.util.ApplicationDescriptor;
import org.junit.Assert;
import org.junit.Test;
import senacor.hd.domain.Artwork;
import senacor.hd.domain.container.Artworks;

import java.util.List;

public class ArtworkWebAppTest extends JerseyTest {

    public ArtworkWebAppTest() throws Exception {
        ApplicationDescriptor appDescriptor = new ApplicationDescriptor();
                appDescriptor.setContextPath("/artwork-manager");
                appDescriptor.setRootResourcePackageName("senacor.hd.mw.artworks.resources");
        super.setupTestEnvironment(appDescriptor);
    }

    @Test
    public void testListAllArtworks() {
        Artworks responseMsg = webResource.path("artworks").accept("application/json").get(Artworks.class);
        List<Artwork> artworks = responseMsg.getList();
        Assert.assertNotNull(responseMsg);
        Assert.assertFalse(artworks.isEmpty());
        Assert.assertNotNull(artworks.get(0));
        Assert.assertNull(artworks.get(0).getPictureUrl());
        Assert.assertNotNull(artworks.get(0).getUri());

        Artwork artwork = webResource.path("artwork/3116904796").accept("application/json").get(Artwork.class);
        Assert.assertNotNull(artwork);
        Assert.assertNotNull(artwork.getPictureUrl());
        System.out.println("artwork.getPictureUrl() = " + artwork.getPictureUrl());

        webResource.path("artwork/999").delete();
    }
}
