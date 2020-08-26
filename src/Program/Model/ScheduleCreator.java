/*
 * Jakob Mukka
 *
 * Version 1,1 Date: 2020-08-26
 *
 * This is the ScheduleCreator (following the MVC format) class of the RadioInfo program.
 * This class is used to get the schedule, containing a list of episodes, for a channel the from SR-Api.
 *
 */

package Program.Model;

import Program.Model.Episode;
import Program.Model.Schedule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleCreator {

    private Schedule schedule;

    /**
     * The constructor for the ScheduleCreator.
     */
    public ScheduleCreator() {

    }

    /**
     *
     * This method gets the schedule for a certain channel. The timespan of the schedule is from 12
     * hours before the current time to 12 hours after the current time. Because of this it is
     * necessary to check the schedule from the previous dat and the next day in case the 12
     * hour interval stretches beyond the current day. The method is called from control and
     * then uses several private classes within the schedule creator class to create the
     * schedule.
     *
     * @param channelID, the id of the channel to get the schedule for.
     * @return schedule, the schedule of the channel.
     * @throws IOException
     * @throws InterruptedException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public Schedule getChannelSchedule (String channelID) throws IOException, InterruptedException,
            ParserConfigurationException, SAXException, ParseException {

        // Creates new schedule
        schedule = new Schedule();

        // Gets the current date in yyyy-MM-dd format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = formatter.format(date.getTime());

        // Parses out the current day from the current date
        int currentDayInt = Integer.parseInt(currentDate.substring(8, 10));

        // Decreases/Increases the current day to get the previous day and next day
        int previousDayInt = currentDayInt - 1;
        int nextDayInt = currentDayInt + 1;

        // Creates the previous date and next date in the same format as the current date
        String previousDate = currentDate.substring(0,8) + previousDayInt;
        String nextDate = currentDate.substring(0,8) + nextDayInt;

        // Joins together the previous, current and next dates schedules in to one schedule
        int programIndex = 0;
        programIndex = addToScheduleFromDate(channelID, previousDate, programIndex);
        programIndex = addToScheduleFromDate(channelID, currentDate, programIndex);
        programIndex = addToScheduleFromDate(channelID, nextDate, programIndex);

        return schedule;
    }

    /**
     * This method adds all episodes within 12 hours from the currenthour, from a certain date which
     * is either the previous day, current day or the next day.
     * The episode index variable is increased for every episode thats within the 12 hour intervall.
     * If an episode is not in the intervall the episodeIndex value gets set to zero.
     *
     * @param channelID, the id of the channel
     * @param date, the date to add to the schedule from
     * @param episodeIndex, the index in the schedule
     * @return episodeIndex
     * @throws InterruptedException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private Integer addToScheduleFromDate (String channelID, String date, int episodeIndex) throws InterruptedException,
            ParserConfigurationException, SAXException, IOException, ParseException {

        // Gets the first page of the schedule from the SR api
        String firstPage = "http://api.sr.se/api/v2/scheduledepisodes?channelid=" + channelID + "&date=" + date;

        // Gets the amount of pages of the schedule
        int amountOfPages = getAmountOfPagesForSchedule(firstPage);

        // Creates a http client and initializes a request and response
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        HttpResponse<InputStream> httpResponse = null;
        InputStream response = null;

        // Goes through each page of the schedule to get all the episodes
        int index = 1;
        while (index <= amountOfPages) {

            String page = "http://api.sr.se/api/v2/scheduledepisodes?channelid=" + channelID +
                    "&date=" + date + "&page=" + index;
            request = HttpRequest.newBuilder().uri(URI.create(page)).build();
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            response = httpResponse.body();
            episodeIndex = parseSchedule(response, episodeIndex);

            index++;
        }

        return episodeIndex;
    }

    /**
     * This method gets the amount of pages a schedule for a certain date has which is
     * then used to determine how many times the loop in the method addToScheduleFromDate
     * must be run (one for each page).
     *
     * @param firstPage, the address of the firstPage in the SR-Api for a certain date.
     * @return the amount of pages the schedule has.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws InterruptedException
     */
    private Integer getAmountOfPagesForSchedule (String firstPage) throws ParserConfigurationException, IOException,
            SAXException, InterruptedException {

        // Creates a client, a http request and a response
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(firstPage)).build();
        HttpResponse<InputStream> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        InputStream response = httpResponse.body();
        String amountOfPages = null;

        // Prepares document parsing
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(response);
        doc.getDocumentElement().normalize();

        // Gets the totalpages element and converts it in to an int
        NodeList totalPages = doc.getElementsByTagName("totalpages");
        Node totalPageAmount = totalPages.item(0);
        Element test = (Element)totalPageAmount;
        amountOfPages = test.getTextContent();

        return Integer.parseInt(amountOfPages);
    }

    /**
     *
     * This method parses a page of a schedule from a certain date and returns the
     * index of the last episode parsed (which is 0 if its not within 12 hours of the
     * current time). This method is called from the method addToScheduleFromDate in a
     * loop, one time for each page in the schedule (from that day)
     *
     * @param response, the input stream reponse
     * @param episodeIndex, the episode index
     * @return the index of the last episode that was added to the schedule
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private Integer parseSchedule (InputStream response, int episodeIndex) throws ParserConfigurationException,
            IOException, SAXException, ParseException {

        // Prepares document parsing
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(response);
        doc.getDocumentElement().normalize();

        // Gets all the scheduleepisode items from the schedule and stores in a node list
        NodeList scheduleEpisodeItems = doc.getElementsByTagName("scheduledepisode");

        // Goes through the list of scheduleepisode items
        int index = 0;
        while (index < scheduleEpisodeItems.getLength()) {

            // The current scheduleepisode item
            Element scheduleEpisode = (Element)scheduleEpisodeItems.item(index);

            // Takes out the title, start, end, description and imageurl of the scheduleepisode
            String episodeTitle = getEpisodeTitle(scheduleEpisode);
            List<String> episodeStart = getEpisodeStartTime(scheduleEpisode);
            List<String> episodeEnd = getEpisodeEndTime(scheduleEpisode);
            String episodeDescription = getEpisodeDescription(scheduleEpisode);
            String episodeImageURL = getEpisodeImageURL(scheduleEpisode);

            // Reformats the start/end date/time in to a day/month hour:minute format
            String startDate = episodeStart.get(0) + "/" + episodeStart.get(1);
            String startTime = episodeStart.get(2) + ":" + episodeStart.get(3);
            String endDate = episodeEnd.get(0) + "/" + episodeEnd.get(1);
            String endTime = episodeEnd.get(2) + ":" + episodeEnd.get(3);

            /* Checks if is within twelvehours of the current time, if it
            is it gets added to the schedule, otherwise it doesn't */
            boolean withinTwelveHoursCheck = isWithinTwelveHours(episodeStart);
            if (withinTwelveHoursCheck) {

                // Creates a new episode and sets its title, start/end time/date, description
                Episode episode = new Episode();
                episode.setEpisodeName(episodeTitle);
                episode.setEpisodeStartDate(startDate);
                episode.setEpisodeEndDate(endDate);
                episode.setEpisodeStartTime(startTime);
                episode.setEpisodeEndTime(endTime);
                episode.setEpisodeDescription(episodeDescription);

                // Creates an episode image if the image url isn't null (not all episodes have an image)
                if (episodeImageURL != null) {
                    episode.createEpisodeImage(episodeImageURL);
                }

                // Adds the episode to the schedule
                schedule.addEpisodeToSchedule(episode);

                // Checks if the episode is on right now
                boolean onRightNowCheck = checkIfEpisodeIsOnRightNow(episodeStart, episodeEnd);
                if (onRightNowCheck) {

                    // Sets the current episode number (the episode that is on right now), in the schedule
                    schedule.setCurrentEpisodeNumber(episodeIndex);
                }
                episodeIndex++;
            } else {
                episodeIndex = 0;
            }
            index++;
        }

        return episodeIndex;
    }

    /**
     * This method checks if an episode is on right now.
     * The episode start and episode end parameters are in the form of a list
     * where index 0 is the month, index 1 is the day, index 2 is the hour, index 3 is the minute
     *
     * @param episodeStart, the start of time program.
     * @param episodeEnd, the end of the program.
     * @return true if the program is on right now, else false.
     * @throws ParseException
     */
    private Boolean checkIfEpisodeIsOnRightNow (List<String> episodeStart, List<String> episodeEnd)
            throws ParseException {

        // Formats the time right now
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm:ss");
        Date nowDateAndTime = new Date();
        String timeNow = formatter.format(nowDateAndTime);

        // Formats the start and end time of the episode in the same format
        String programStart = episodeStart.get(0) + "/" + episodeStart.get(1) + " " +
                episodeStart.get(2) + ":" + episodeStart.get(3) + ":00";
        String programEnd = episodeEnd.get(0) + "/" + episodeEnd.get(1) + " " +
                episodeEnd.get(2) + ":" + episodeEnd.get(3) + ":00";

        // Parses out the actual times
        Date now = formatter.parse(timeNow);
        Date start = formatter.parse(programStart);
        Date end = formatter.parse(programEnd);

        // Checks if the time right now is between the start time and end time
        if ((now.after(start)) && (now.before(end))) {
            return true;
        }

        return false;
    }

    /**
     * This method is used to get the current time and date
     *
     * @return
     */
    private String getCurrentTimeAndDate () {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = formatter.format(date.getTime());

        return time;
    }

    /**
     * This method is used to check if an episode is within 12 hours of the current time
     *
     * @param episodeStart, the start time of the episode
     * @return true if it's within twelve hours, otherwise false
     */
    private Boolean isWithinTwelveHours (List<String> episodeStart) {

        // Gets the current time and date
        String currentTimeAndDate = getCurrentTimeAndDate();

        // Separates the day and hour from the current time and date
        String currentDay = currentTimeAndDate.substring(8,10);
        String currentHour = currentTimeAndDate.substring(11,13);

        // Gets the day and hour from the episode start
        String episodeStartDay = episodeStart.get(1);
        String episodeStartHour = episodeStart.get(2);

        // Removing zeroes at index 0 (if there are any)
        if (currentDay.charAt(0) == '0') {
            currentDay = currentDay.substring(1);
        }
        if (currentHour.charAt(0) == '0') {
            currentHour = currentHour.substring(1);
        }
        if (episodeStartDay.charAt(0) == '0') {
            episodeStartDay = episodeStartDay.substring(1);
        }
        if (episodeStartHour.charAt(0) == '0') {
            episodeStartHour = episodeStartHour.substring(1);
        }

        // Parses the days and hours in to integers
        int currentDayInt = Integer.parseInt(currentDay);
        int currentHourInt = Integer.parseInt(currentHour);
        int programStartDayInt = Integer.parseInt(episodeStartDay);
        int programStartHourInt = Integer.parseInt(episodeStartHour);

        // Compares the program start time and day with the current time and day
        if (currentDayInt == programStartDayInt) {

            if (((currentHourInt - programStartHourInt) < 12) && ((currentHourInt - programStartHourInt) > -12)) {
                return true;
            }  else {
                return false;
            }
        } else if (programStartDayInt  == (currentDayInt + 1)) {

            int hoursUntilNextDay = 24 - currentHourInt;
            if ((hoursUntilNextDay + programStartHourInt) <= 12) {
                return true;
            } else {
                return false;
            }
        } else if ((programStartDayInt == (currentDayInt - 1))) {

            int hoursFromPreviousDay = currentHourInt;
            if ((hoursFromPreviousDay + (24 - programStartHourInt)) <= 12) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * This method is used to get the title of a schedule episode
     *
     * @param scheduleEpisode, the schedule episode
     * @return the title of the episode
     */
    private String getEpisodeTitle(Element scheduleEpisode) {

        // Gets all the child nodes in the schedule episode and stores it in a list
        NodeList scheduleEpisodeNodeList = scheduleEpisode.getChildNodes();

        // Goes through the list of child nodes
        int index = 0;
        while (index < scheduleEpisodeNodeList.getLength()) {

            // If the child node has the name "title"
            if (scheduleEpisodeNodeList.item(index).getNodeName().equals("title")) {

                // Returns its text content
                return scheduleEpisodeNodeList.item(index).getTextContent();
            }
            index++;
        }

        return null;
    }

    /**
     * This method is used to get the start time of an episode as a list with
     * day, hour, minute as separate elements in the list
     *
     * @param scheduleEpisode, the schedule episode
     * @return the start time of the schedule episode
     */
    private List<String> getEpisodeStartTime (Element scheduleEpisode) {

        // Gets all the child nodes in the schedule episode
        NodeList scheduleEpisodeNodeList = scheduleEpisode.getChildNodes();

        // Goes through the list of child nodes
        int index = 0;
        while (index < scheduleEpisodeNodeList.getLength()) {

            // If the child node has the name "starttimeutc"
            if (scheduleEpisodeNodeList.item(index).getNodeName().equals("starttimeutc")) {

                // Gets its text content
                String startTime = scheduleEpisodeNodeList.item(index).getTextContent();

                // Converts it in to a list format and returns it
                List<String> startDateAndTime = timeAndDateToList(startTime);
                return startDateAndTime;
            }
            index++;
        }

        return null;
    }

    /**
     * This method is used to get the end time of an episode as a list with
     * day, hour, minute as separate elements in the list
     *
     * @param scheduleEpisode, the schedule episode
     * @return the end time of the schedule episode
     */
    private List<String> getEpisodeEndTime(Element scheduleEpisode) {

        // Gets all the child nodes in the schedule episode
        NodeList scheduleEpisodeNodeList = scheduleEpisode.getChildNodes();

        // Goes through the list of child nodes
        int index = 0;
        while (index < scheduleEpisodeNodeList.getLength()) {

            // If the child node has the name "endtimeutc"
            if (scheduleEpisodeNodeList.item(index).getNodeName().equals("endtimeutc")) {

                // Gets its text content
                String endTime = scheduleEpisodeNodeList.item(index).getTextContent();

                // Converts it in to a list format and returns it
                List<String> endDateAndTime = timeAndDateToList(endTime);
                return endDateAndTime;
            }
            index++;
        }

        return null;
    }

    /**
     * This method is used to get the description of an schedule episode
     *
     * @param scheduleEpisode, the schedule episode
     * @return the description of the schedule episode
     */
    private String getEpisodeDescription(Element scheduleEpisode) {

        // Gets all the child nodes in the schedule episode
        NodeList scheduleepisodenodelist = scheduleEpisode.getChildNodes();

        // Goes through the list of child nodes
        int index = 0;
        while (index < scheduleepisodenodelist.getLength()) {

            // If the child node has the name "description"
            if (scheduleepisodenodelist.item(index).getNodeName().equals("description")) {

                // Return its text content
                return scheduleepisodenodelist.item(index).getTextContent();
            }
            index++;
        }

        return null;
    }

    /**
     * This method is used to get the image url of an episode in
     * a schedule
     *
     * @param scheduleEpisode, the scheduleEpisode
     * @return the episode image url
     */
    private String getEpisodeImageURL(Element scheduleEpisode) {

        // Gets all the child nodes in the schedule episode
        NodeList scheduleEpisodeNodeList = scheduleEpisode.getChildNodes();

        // Goes through the list of child nodes
        int index = 0;
        while (index < scheduleEpisodeNodeList.getLength()) {

            // If the child node has the name "imageurl"
            if (scheduleEpisodeNodeList.item(index).getNodeName().equals("imageurl")) {

                // Returns its text content
                return scheduleEpisodeNodeList.item(index).getTextContent();
            }
            index++;
        }

        return null;
    }

    /**
     * This method is used to convert a time in the time format in the SR Api,
     * ex 2020-08-20T22:00:00Z, in to a list with the times month, day, hour and
     * minute separated.
     *
     * @param time, the time to be converted
     * @return the time in a list format
     */
    private List<String> timeAndDateToList (String time) {

        /* Creates the list where the time will be stored as Month(index 0), Day(index 1),
           Hour(index 2), Minute(index 3) */
        List<String> timeAsList = new ArrayList<>(4);

        // Gets the end index of the date which is then used to get the start indexes of the hour/minute
        int dateEndIndex = time.indexOf("T");
        int hourStartIndex = dateEndIndex + 1;
        int minuteStartIndex = hourStartIndex + 3;

        // Creates the substrings for month, day, hour and minute out of the time string
        String month = time.substring(5, 7);
        String day = time.substring(8,10);
        String hour = time.substring(hourStartIndex, hourStartIndex + 2);
        String minute = time.substring(minuteStartIndex, minuteStartIndex + 2);

        // Adds the substrings to the list
        timeAsList.add(0, month);
        timeAsList.add(1, day);
        timeAsList.add(2, hour);
        timeAsList.add(3, minute);

        return timeAsList;
    }

    public class NonExistingChannelException extends Exception {
        public NonExistingChannelException(String errorMessage, Throwable error) {
            super(errorMessage, error);
        }
    }
}
