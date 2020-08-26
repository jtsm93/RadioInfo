package Test;

import Program.Model.Episode;
import Program.Model.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    private Schedule schedule;

    @BeforeEach
    public void setUp () {
        schedule = new Schedule();
    }

    @Test
    public void shouldIncreaseNumberOfEpisodesAfterAddingEpisodeToSchedule () {
        Episode episode = new Episode();
        schedule.addEpisodeToSchedule(episode);
        assertEquals(1, schedule.getNumberOfEpisodes());
    }

    @Test
    public void shouldHaveTheCorrectCurrentEpisodeNumberAfterSettingCurrentEpisodeNumber () {
        schedule.setCurrentEpisodeNumber(1);
        assertEquals(1, schedule.getCurrentEpisodeNumber());
    }

    @Test
    public void shouldNotReturnNullWhenGettingAnEpisode () {
        Episode episode = new Episode();
        schedule.addEpisodeToSchedule(episode);
        assertNotNull(schedule.getEpisode(0));
    }

    @Test
    public void shouldHaveCorrectNumberOfEpisodes () {
        Episode episode1 = new Episode();
        Episode episode2 = new Episode();
        schedule.addEpisodeToSchedule(episode1);
        schedule.addEpisodeToSchedule(episode2);
        assertEquals(2, schedule.getNumberOfEpisodes());
    }
}