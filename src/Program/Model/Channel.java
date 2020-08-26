/*
 * Jakob Mukka
 *
 * Version 1,1 Date: 2020-08-26
 *
 * This is the channel class of the program. Each channel has a name, an id and a schedule of
 * episodes.
 *
 */

package Program.Model;

public class Channel {

    private String channelName;
    private String channelID;
    private Schedule schedule;

    /**
     * Constructor or Channel
     */
    public Channel () {

    }

    /**
     * This method is used to set the name of the channel
     *
     * @param name the name to be set for the channel
     */
    public void setChannelName (String name) {
        channelName = name;
    }

    /**
     * This method is used to set the id of the channel
     *
     * @param id the id to be set for the channel
     */
    public void setChannelID (String id) {
        channelID = id;
    }

    /**
     * This method is used to set the schedule (of episodes) for the channel
     *
     * @param _schedule the schedule to be set for the channel
     */
    public void addSchedule(Schedule _schedule) {
        schedule = _schedule;
    }

    /**
     * This method gets the name of the channel
     *
     * @return the name of the channel
     */
    public String getChannelName () {
        return channelName;
    }

    /**
     * This method gets the id of the channel
     *
     * @return the id of the channel
     */
    public String getChannelID () {
        return channelID;
    }

    /**
     * This method gets the channels schedule
     *
     * @return the schedule of the channel
     */
    public Schedule getSchedule () {
        return  schedule;
    }
}
