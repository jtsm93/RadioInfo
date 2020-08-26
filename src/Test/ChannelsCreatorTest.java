package Test;

import Program.Model.Channel;
import Program.Model.ChannelsCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChannelsCreatorTest {

    ChannelsCreator channelsCreator;

    @BeforeEach
    public void setUp () {
        channelsCreator = new ChannelsCreator();
    }

    @Test
    public void shouldThrowIOExceptionWhenTryingToGetChannelsFromIncorrectURL () {
        Assertions.assertThrows(IOException.class, () -> {
            String incorrectURL = "http://test.test.test/api/v2/test";
            channelsCreator.getChannels(incorrectURL);
        });
    }

    @Test
    public void shouldNotThrowIOExceptionWhenTryingToGetChannelsFromCorrectURL () throws IOException,
            InterruptedException {
        String correctURL = "http://api.sr.se/api/v2/channels";
        channelsCreator.getChannels(correctURL);
    }

    @Test
    public void shouldThrowExceptionIfInputStreamIsNullOnParseChannels () {
        Assertions.assertThrows(Exception.class, () -> {
            channelsCreator.parseChannels(null);
        });
    }

    @Test
    public void shouldReturnAListOfChannelsOnACorrectInputStreamOnParseChannels () throws IOException,
            InterruptedException, ParserConfigurationException, SAXException {
        String correctURL = "http://api.sr.se/api/v2/channels";
        List<Channel> returnList = channelsCreator.parseChannels(channelsCreator.getChannels(correctURL));
        assertNotNull(returnList);
    }
}