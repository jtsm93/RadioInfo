package Test;

import Program.Model.ScheduleCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleCreatorTest {

    ScheduleCreator scheduleCreator;

    @BeforeEach
    public void setUp() {
        scheduleCreator = new ScheduleCreator();
    }

    @Test
    public void shouldReturnAScheduleOnACorrectChannelIDOnGetChannelSchedule () throws InterruptedException,
            ParserConfigurationException, SAXException, ParseException, IOException {
            assertNotNull(scheduleCreator.getChannelSchedule("213"));
    }
}