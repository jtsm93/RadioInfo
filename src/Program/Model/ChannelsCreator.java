/*
 * Jakob Mukka
 *
 * Version 1,1 Date: 2020-08-26
 *
 * This is the ChannelsCreator (following the MVC format) class of the RadioInfo program.
 * This class is used to get channels from the SR-Api and parse the channels id's.
 *
 */

package Program.Model;

import Program.Model.Channel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ChannelsCreator {

    List<Channel> channels;

    /**
     * Constructor for the Channel Program.Model
     */
    public ChannelsCreator() {
        channels = new ArrayList<Channel>();
    }

    /**
     * This method gets the channels method from the SR-Api as an InputStream and returns it.
     *
     * @return response, the channels method as an inputstream.
     * @throws IOException
     * @throws InterruptedException
     */
    public InputStream getChannels (String url) throws IOException, InterruptedException {

        // Creates client, httprequest and response to get the channels from the SR-Api
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<InputStream> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        // Converts it in to an input stream and returns it
        InputStream response = httpResponse.body();
        return response;
    }

    /**
     * This method parses the channels from the input stream and returns it as a list
     * of channels
     *
     * @param response, the input stream response
     * @return the list of channels
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public List<Channel> parseChannels (InputStream response) throws ParserConfigurationException,
            IOException, SAXException {

        // Prepares document parsing
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(response);
        doc.getDocumentElement().normalize();

        // Gets all the channel items and stores them in  a node list
        NodeList channelItems = doc.getElementsByTagName("channel");

        // Goes through the list of channel items
        int index = 0;
        while (index < channelItems.getLength()) {

            // Gets the current channel item in the list
            Element channelItem = (Element)channelItems.item(index);

            // Creates a new channel from the current channel item
            Channel channel = new Channel();
            channel.setChannelName(channelItem.getAttribute("name"));
            channel.setChannelID(channelItem.getAttribute("id"));

            // Adds the channel to the list of channel
            channels.add(index, channel);
            index++;
        }
        return channels;
    }
}
