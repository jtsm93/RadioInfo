package Test;

import Program.Model.Episode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class EpisodeTest {

    private Episode episode;

    @BeforeEach
    public void setUp () {
        episode = new Episode();
    }

    @Test
    public void shouldHaveANameAfterSettingName () {
        episode.setEpisodeName("test");
        assertNotNull(episode.getEpisodeName());
    }

    @Test
    public void shouldHaveAStartDateAfterSettingStartDate () {
        episode.setEpisodeStartDate("test");
        assertNotNull(episode.getEpisodeStartDate());
    }

    @Test
    public void shouldHaveAnEndDateAfterSettingEndDate () {
        episode.setEpisodeEndDate("test");
        assertNotNull(episode.getEpisodeEndDate());
    }

    @Test
    public void shouldHaveAStartTimeAfterSettingStartTime () {
        episode.setEpisodeStartTime("test");
        assertNotNull(episode.getEpisodeStartTime());
    }

    @Test
    public void shouldHaveAnEndTimeAfterSettingEndTime () {
        episode.setEpisodeEndTime("test");
        assertNotNull(episode.getEpisodeEndTime());
    }

    @Test
    public void shouldHaveADescriptionAfterSettingDescription () {
        episode.setEpisodeDescription("test");
        assertNotNull(episode.getEpisodeDescription());
    }

    @Test
    public void shouldThrowIOExceptionWhenTryingToCreateImageWithNonExistingURL () throws IOException {
        Assertions.assertThrows(IOException.class, () -> {
            String nonExistingURL = "https://test.test.test/test/test/1/test.jpg?preset=api-default-square";
            episode.createEpisodeImage(nonExistingURL);
        });
    }

    @Test
    public void shouldNotThrowIOExceptionWhenTryingToCreateImageWithExistingURL () throws IOException {
        String existingURL = "https://static-cdn.sr.se/sida/images/5380/bd64d128-9787-4a22-8a62-d7e83abbbb6e.jpg";
        episode.createEpisodeImage(existingURL);
    }
}