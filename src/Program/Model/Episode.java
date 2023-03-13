/*
 * Jakob Mukka
 *
 * Version 1,1 Date: 2020-08-26
 *
 * This is the episode class of the program that represents a single episode within
 * the schedule.
 * Each episode has a name, start date, end date, start time, end time, description, episodenumber
 * and an image that is displayed as a small thumbnail in the table of the GUI.
 *
 */

package Program.Model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Episode {

    private String episodeName;
    private String episodeStartDate;
    private String episodeEndDate;
    private String episodeStartTime;
    private String episodeEndTime;
    private String episodeDescription;
    private JLabel episodeImage;

    /**
     * Constructor for Episode
     */
    public Episode() {

    }

    /**
     * This method sets the name for the episode
     *
     * @param name, the name for the episode
     */
    public void setEpisodeName(String name) {
        episodeName = name;
    }

    /**
     * This method sets the start date for the episode
     *
     * @param startDate, the start date for the episode
     */
    public void setEpisodeStartDate(String startDate) {
        episodeStartDate = startDate;
    }

    /**
     * This method sets the end date for the episode
     *
     * @param endDate, the end date for the episode
     */
    public void setEpisodeEndDate(String endDate) {
        episodeEndDate = endDate;
    }

    /**
     * This method sets the start time for the episode
     *
     * @param startTime, the start time for the episode
     */
    public void setEpisodeStartTime(String startTime) {
        episodeStartTime = startTime;
    }

    /**
     *  This method sets the end time for the episode
     *
     * @param endTime, the end time for the episode
     */
    public void setEpisodeEndTime(String endTime) {
        episodeEndTime = endTime;
    }

    /**
     * This method sets the description for the episode
     *
     * @param description, the description for the episode
     */
    public void setEpisodeDescription(String description) {
        episodeDescription = description;
    }

    /**
     * This method creates the image for the episode and resizes it to
     * fit in a table cell.
     *
     * @param imageURL, the image url
     * @throws IOException
     */
    public void createEpisodeImage(String imageURL) throws IOException {
        URL url = new URL(imageURL);
        BufferedImage inBuffImage = ImageIO.read(url);
        BufferedImage outBuffImage = new BufferedImage(50, 35, inBuffImage.getType());
        Graphics2D g2d = outBuffImage.createGraphics();
        g2d.drawImage(inBuffImage,0,0,50,35, null);
        g2d.dispose();

        ImageIcon imageIcon = new ImageIcon(outBuffImage);
        episodeImage = new JLabel();
        episodeImage.setIcon(imageIcon);
    }

    /**
     * This method sets the name of the episode
     *
     * @return the name of the episode
     */
    public String getEpisodeName() {
        return episodeName;
    }

    /**
     * This method gets the start date of the episode
     *
     * @return the start date of the episode
     */
    public String getEpisodeStartDate() {
        return episodeStartDate;
    }

    /**
     * This method gets the end date of the episode
     *
     * @return the end date of the episode
     */
    public String getEpisodeEndDate() {
        return episodeEndDate;
    }

    /**
     * This method gets the start time of the episode
     *
     * @return the start time of the episode
     */
    public String getEpisodeStartTime() {
        return episodeStartTime;
    }

    /**
     * This method gets the end time of the episode
     *
     * @return the end time of the episode
     */
    public String getEpisodeEndTime() {
        return episodeEndTime;
    }

    /**
     * This method gets the description of the episode
     *
     * @return the description of the episode
     */
    public String getEpisodeDescription() {
        return episodeDescription;
    }

    /**
     * This method returns the episode image.
     *
     * @return the episode image.
     */
    public JLabel getEpisodeImage() {
        return episodeImage;
    }
}
