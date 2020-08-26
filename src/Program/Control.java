/*
 * Jakob Mukka
 *
 * Version 1,1 Date: 2020-08-26
 *
 * This is the Control (following the MVC format) class of the RadioInfo program.
 * This class gets the channels and each channels schedule from the ScheduleCreator and ChannelsCreator
 * classes and then sends the information to the View class.
 *
 */

package Program;

import Program.Model.Channel;
import Program.Model.ChannelsCreator;
import Program.Model.Schedule;
import Program.Model.ScheduleCreator;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class Control {

    private View view;
    private ScheduleCreator scheduleCreator;
    private ChannelsCreator channelsCreator;

    /**
     * This is the constructor for the Control which creates a View, ScheduleModel, ChannelsCreator a
     * and a SwingWorker.
     *
     */
    public Control() {

        // Creates a new view, schedule creater and channels creator
        view = new View();
        scheduleCreator = new ScheduleCreator();
        channelsCreator = new ChannelsCreator();

        // Creates a new swing worker and executes it
        SwingWorker swingWorker = new SwingWorker();
        swingWorker.execute();
    }

    /*
     * This is a nested inner class for the SwingWorker.
     */
    private class SwingWorker extends javax.swing.SwingWorker {

        List<Channel> channels;

        /**
         * The doInBackground method of the SwingWorker.
         *
         * @return null;
         * @throws Exception
         */
        @Override
        protected Object doInBackground() throws Exception {

            // Sets the visibility of the loading window as true
            view.setLoadingRadioInfoVisibility(true);

            // Gets the channels
            channels = getChannels();

            // Creates the schedules for each channel
            addChannelSchedules(channels);

            // Sets the default schedule that will be visible upon start
            Schedule p1 = scheduleCreator.getChannelSchedule(channels.get(0).getChannelID());
            setChannelSchedule(p1, channels.get(0).getChannelName());

            // Sets the visibility of the loading window as true as the whole GUI should now be visible
            view.setLoadingRadioInfoVisibility(false);

            // Action listeners for the channel buttons, which changes the schedule in the table
            view.getP1Button().addActionListener(actionEvent -> {
                Channel p1Channel = channels.get(0);
                Schedule p1Schedule = p1Channel.getSchedule();

                setChannelSchedule(p1Schedule, p1Channel.getChannelName());
            });

            view.getP2Button().addActionListener(actionEvent -> {
                Channel p2Channel = channels.get(1);
                Schedule p2Schedule = p2Channel.getSchedule();

                setChannelSchedule(p2Schedule, p2Channel.getChannelName());

            });

            view.getP3Button().addActionListener(actionEvent -> {
                Channel p3Channel = channels.get(2);
                Schedule p3Schedule = p3Channel.getSchedule();

                setChannelSchedule(p3Schedule, p3Channel.getChannelName());
            });

            view.getP4BlekingeButton().addActionListener(actionEvent -> {
                Channel p4BlekingeChannel = channels.get(3);
                Schedule p4BlekingeSchedule = p4BlekingeChannel.getSchedule();

                setChannelSchedule(p4BlekingeSchedule, p4BlekingeChannel.getChannelName());
            });

            view.getP4DalarnaButton().addActionListener(actionEvent -> {
                Channel p4DalarnaChannel = channels.get(4);
                Schedule p4DalarnaSchedule = p4DalarnaChannel.getSchedule();

                setChannelSchedule(p4DalarnaSchedule, p4DalarnaChannel.getChannelName());
            });

            view.getP4GotlandButton().addActionListener(actionEvent -> {
                Channel p4GotlandChannel = channels.get(5);
                Schedule p4GotlandSchedule = p4GotlandChannel.getSchedule();

                setChannelSchedule(p4GotlandSchedule, p4GotlandChannel.getChannelName());
            });

            view.getP4GaevleborgButton().addActionListener(actionEvent -> {
                Channel p4GaevleborgChannel = channels.get(6);
                Schedule p4GaevleborgSchedule = p4GaevleborgChannel.getSchedule();

                setChannelSchedule(p4GaevleborgSchedule, p4GaevleborgChannel.getChannelName());
            });

            view.getP4GoeteborgButton().addActionListener(actionEvent -> {
                Channel p4GoeteborgChannel = channels.get(7);
                Schedule p4GoeteborgSchedule = p4GoeteborgChannel.getSchedule();

                setChannelSchedule(p4GoeteborgSchedule, p4GoeteborgChannel.getChannelName());
            });

            view.getP4HallandButton().addActionListener(actionEvent -> {
                Channel p4HallandChannel = channels.get(8);
                Schedule p4HallandSchedule = p4HallandChannel.getSchedule();

                setChannelSchedule(p4HallandSchedule, p4HallandChannel.getChannelName());
            });

            view.getP4JaemtlandButton().addActionListener(actionEvent -> {
                Channel p4JaemtlandChannel = channels.get(9);
                Schedule p4JaemtlandSchedule = p4JaemtlandChannel.getSchedule();

                setChannelSchedule(p4JaemtlandSchedule, p4JaemtlandChannel.getChannelName());
            });

            // Action listener for the update button
            view.getUpdateButton().addActionListener(actionEvent -> {
                try {

                    // Sets the visibility of the updating window as true
                    view.setUpdatingRadioInfoVisibility(true);

                    // Adds schedules to the channels again, which in effect updates them.
                    addChannelSchedules(channels);

                    // Gets the name of the current channel in the table
                    String currentChannelName = view.getCurrentChannel();

                    // Goes through the list of channels
                    int index = 0;
                    while (index < channels.size()) {

                        // If the the channel name is equal to the current channel name
                        if (currentChannelName.equals(channels.get(index).getChannelName())) {

                            // Get the channel
                            Channel currentChannel = channels.get(index);

                            // And sets its schedule in the table
                            Schedule currentChannelSchedule = currentChannel.getSchedule();
                            setChannelSchedule(currentChannelSchedule, currentChannelName);
                        }
                        index++;
                    }

                    // Sets the visibility of the updating window as false (updating is done)
                    view.setUpdatingRadioInfoVisibility(false);

                } catch (InterruptedException | ParserConfigurationException | SAXException
                        | ParseException | IOException e) {
                    e.printStackTrace();
                }
            });

            return null;
        }

        /**
         * This method gets the channels from the channels creator from the SR-api.
         *
         * @return the list of channels
         * @throws IOException
         * @throws InterruptedException
         * @throws ParserConfigurationException
         * @throws SAXException
         */
        private List<Channel> getChannels () throws IOException, InterruptedException,
                ParserConfigurationException, SAXException {
            String url = "http://api.sr.se/api/v2/channels";
            return channelsCreator.parseChannels((channelsCreator.getChannels(url)));
        }

        /**
         * This method sets the channel schedule in the table in the GUI
         *
         * @param schedule the schedule
         * @param channelName the name of the channel
         */
        private void setChannelSchedule (Schedule schedule, String channelName) {

            int currentprogramnumber = schedule.getCurrentEpisodeNumber();
            view.setCurrentProgramNumber(currentprogramnumber);
            view.setTable(schedule);
            view.setCurrentChannel(channelName);
        }

        /**
         * This method adds the schedules to each channel
         *
         * @param channels the channels
         * @throws InterruptedException
         * @throws ParserConfigurationException
         * @throws SAXException
         * @throws ParseException
         * @throws IOException
         */
        private void addChannelSchedules (List<Channel> channels) throws InterruptedException,
                ParserConfigurationException, SAXException, ParseException, IOException {

            // Goes through the list of channels
            int index = 0;
            while (index < channels.size()) {

                // Gets the channel and its id
                Channel channel = channels.get(index);
                String channelID = channel.getChannelID();

                // Gets its schedule
                Schedule channelSchedule = scheduleCreator.getChannelSchedule(channelID);

                // Adds the schedule to the channel
                channel.addSchedule(channelSchedule);
                index++;
            }
        }
    }
}
