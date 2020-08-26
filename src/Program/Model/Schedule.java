/*
 * Jakob Mukka
 *
 * Version 1,1 Date: 2020-08-26
 *
 * This is the schedule class of the program. Each schedule consists of a list of episodes
 * as well as the currentEpisodeNumber which is the number of the episode that is on right now
 * and the number of episodes in the schedule
 */

package Program.Model;

import Program.Model.Episode;
import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private List<Episode> episodes;
    private int currentEpisodeNumber;
    private int numberOfEpisodes = 0;

    /**
     * Constructor for the Schedule
     */
    public Schedule () {
        episodes = new ArrayList<Episode>();
    }

    /**
     * This method adds an episode to the schedule
     *
     * @param episode the episode to be added
     */
    public void addEpisodeToSchedule(Episode episode) {
        episodes.add(episode);
        numberOfEpisodes++;
    }

    /**
     * This method sets the number of the current episode (episode that is on right now)
     *
     * @param _currentEpisodeNumber the number of the current episode
     */
    public void setCurrentEpisodeNumber(int _currentEpisodeNumber) {
        currentEpisodeNumber = _currentEpisodeNumber;
    }

    /**
     * This method gets the number of the current episode (episode that is on right now)
     *
     * @return the number of the current episode
     */
    public Integer getCurrentEpisodeNumber() {
        return currentEpisodeNumber;
    }

    /**
     * This method is used to get an episode with a certain index in the schedule
     *
     * @param index the index of the episode
     * @return the episode
     */
    public Episode getEpisode(int index) {
        return episodes.get(index);
    }

    /**
     * This method is used to get the number of episodes in the schedule
     *
     * @return the number of episodes
     */
    public Integer getNumberOfEpisodes() {
        return numberOfEpisodes;
    }
}
