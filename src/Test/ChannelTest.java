package Test;

import Program.Model.Channel;
import Program.Model.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChannelTest {

    Channel channel;

    @BeforeEach
    public void setUp() {
        channel = new Channel();
    }

    @Test
    public void shouldHaveANameAfterSettingName () {
        channel.setChannelName("test");
        assertNotNull(channel.getChannelName());
    }

    @Test
    public void shouldHaveAnIDAfterSettingID () {
        channel.setChannelID("test");
        assertNotNull(channel.getChannelID());
    }

    @Test
    public void shouldHaveAScheduleAfterAddingSchedule () {
        Schedule schedule = new Schedule();
        channel.addSchedule(schedule);
        assertNotNull(channel.getSchedule());
    }
}