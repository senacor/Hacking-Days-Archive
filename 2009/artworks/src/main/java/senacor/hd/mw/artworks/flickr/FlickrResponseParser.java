package senacor.hd.mw.artworks.flickr;

import senacor.hd.domain.Artwork;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hannes
 * Date: Nov 28, 2009
 * Time: 2:33:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlickrResponseParser {
    private static final String PHOTO = "photo";
    private static final String TITLE = "title";
    private static final String ID = "id";
    private static final String URL = "url";

    public List<Artwork> parseSearchResult(String xml) {
        List<Artwork> artworks = new ArrayList<Artwork>();
        try {
            XMLEventReader eventReader = getEventReaderForXml(xml);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();

                    if (startElement.getName().getLocalPart().equals(PHOTO)) {
                        Artwork artwork = new Artwork();
                        artworks.add(artwork);

                        Iterator attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = (Attribute) attributes.next();

                            if (attribute.getName().toString().equals(TITLE)) {
                                artwork.setName(attribute.getValue());
                            }
                            if (attribute.getName().toString().equals(ID)) {
                                artwork.setId(new Long(attribute.getValue()));
                            }
                        }
                    }
                }
            }

        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return artworks;
    }

    private XMLEventReader getEventReaderForXml(String xml) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        // Setup a new eventReader

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xml.getBytes());
        XMLEventReader eventReader = inputFactory.createXMLEventReader(byteArrayInputStream);
        return eventReader;
    }

    public Artwork parseGetInfo(String xml) {
        Artwork artwork = new Artwork();
        try {
            XMLEventReader eventReader = getEventReaderForXml(xml);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();

                    if (startElement.getName().getLocalPart().equals(TITLE)) {
                        event = eventReader.nextEvent();
                        artwork.setName(event.asCharacters().getData());
                    }
                    if (startElement.getName().getLocalPart().equals(URL)) {
                        event = eventReader.nextEvent();
                        artwork.setPictureUrl(event.asCharacters().getData());
                    }
                }
            }

        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }

        return artwork;
    }
}
