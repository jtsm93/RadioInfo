/*
 * Jakob Mukka
 *
 * Version 1,1 Date: 2022-05-17
 *
 * This is the View (following the MVC format) class of the RadioInfo program.
 * This class is used to create the GUI for the program.
 *
 */

package Program;

import Program.Model.Episode;
import Program.Model.Schedule;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class View {

    private JTable table;
    private int currentProgramNumber;
    private DefaultTableModel tableModel;
    private JTextField channel;
    private JButton p1Button;
    private JButton p2Button;
    private JButton p3Button;
    private JButton p4BlekingeButton;
    private JButton p4DalarnaButton;
    private JButton p4GotlandButton;
    private JButton p4GaevleborgButton;
    private JButton p4GoeteborgButton;
    private JButton p4HallandButton;
    private JButton p4JaemtlandButton;
    private JButton updateButton;
    private JDialog loadingRadioInfo;
    private JDialog updatingRadioInfo;

    /**
     * The Constructor for the view
     */
    public View() {

        // Frame
        JFrame frame = new JFrame();
        frame.setTitle("Radio Info");
        frame.setSize(900, 325);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table Program.Model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Image");
        tableModel.addColumn("Program");
        tableModel.addColumn("Start");
        tableModel.addColumn("End");
        tableModel.addColumn("Description");

        // Table
        table = new JTable(tableModel);
        table.getColumn("Image").setPreferredWidth(50);
        table.getColumn("Image").setMinWidth(50);
        table.getColumn("Image").setMaxWidth(50);
        table.getColumn("Program").setPreferredWidth(300);
        table.getColumn("Program").setMinWidth(300);
        table.getColumn("Program").setMaxWidth(300);
        table.getColumn("Start").setPreferredWidth(150);
        table.getColumn("Start").setMinWidth(150);
        table.getColumn("Start").setMaxWidth(150);
        table.getColumn("End").setPreferredWidth(150);
        table.getColumn("End").setMinWidth(150);
        table.getColumn("End").setMaxWidth(150);
        table.getColumn("Description").setPreferredWidth(10000);
        table.getColumn("Description").setMinWidth(10000);
        table.getColumn("Description").setMaxWidth(10000);
        table.setRowHeight(35);
        table.getColumn("Image").setCellRenderer(new MyLabelRenderer());
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        // Table Cell Renderer
        MyTableCellRenderer myTableCellRenderer = new MyTableCellRenderer();
        table.setDefaultRenderer(Object.class, myTableCellRenderer);
        myTableCellRenderer.setHorizontalAlignment(JLabel.LEFT);
        table.getColumnModel().getColumn(0).setHeaderRenderer(myTableCellRenderer);
        table.getColumnModel().getColumn(1).setHeaderRenderer(myTableCellRenderer);
        table.getColumnModel().getColumn(2).setHeaderRenderer(myTableCellRenderer);
        table.getColumnModel().getColumn(3).setHeaderRenderer(myTableCellRenderer);
        table.getColumnModel().getColumn(4).setHeaderRenderer(myTableCellRenderer);

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(25,25, 850, 200);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        // Panel
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Text Field
        channel = new JTextField();
        channel.setBounds(75,5, 100, 20);
        channel.setFocusable(false);

        // Label
        JLabel channelName = new JLabel("Channel: ");
        channelName.setBounds(25,5, 50, 20);

        // Buttons
        updateButton = new JButton("Update");
        updateButton.setBounds(775, 5, 100, 20);
        p1Button = new JButton("P1");
        p2Button = new JButton("P2");
        p3Button = new JButton("P3");
        p4BlekingeButton = new JButton("P4 Blekinge");
        p4DalarnaButton = new JButton("P4 Dalarna");
        p4GotlandButton = new JButton("P4 Gotland");
        p4GaevleborgButton = new JButton("P4 Gävleborg");
        p4GoeteborgButton = new JButton("P4 Göteborg");
        p4HallandButton = new JButton("P4 Halland");
        p4JaemtlandButton = new JButton("P4 Jämtland");

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(24, 230, 852, 25);
        menuBar.add(p1Button);
        menuBar.add(p2Button);
        menuBar.add(p3Button);
        menuBar.add(p4BlekingeButton);
        menuBar.add(p4DalarnaButton);
        menuBar.add(p4GotlandButton);
        menuBar.add(p4GaevleborgButton);
        menuBar.add(p4GoeteborgButton);
        menuBar.add(p4HallandButton);
        menuBar.add(p4JaemtlandButton);

        // Adding components to the Panel
        panel.add(scrollPane);
        panel.add(channel);
        panel.add(channelName);
        panel.add(updateButton);
        panel.add(menuBar);

        // JDialogs
        loadingRadioInfo = new JDialog(frame, "Loading Radio Info...");
        updatingRadioInfo = new JDialog(frame, "Updating Radio Info...");
        loadingRadioInfo.setBounds(325,100,250, 100);
        updatingRadioInfo.setBounds(325,100,250,100);
		
		// The JDialogs are set to dispose on close
		loadingRadioInfo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		updatingRadioInfo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
        // Adding Panel to the Frame
        frame.add(panel);
    }

    /**
     * This method displays the schedule of a certain channel on the table by going through the
     * list of episodes in the schedule and for each episode creating a new row on the table
     * and inserting the episodes image, name, start time/date, end time/date and description on
     * separate cells on that row.
     *
     * @param schedule, the schedule of a channel
     */
    public void setTable (Schedule schedule) {

        int scheduleIndex = 0;
        int rowIndex = 0;

        // Goes through the list of episodes in the schedule
        while (scheduleIndex < schedule.getNumberOfEpisodes()) {

            // Adds a row to the table
            tableModel.addRow(new Object[]{});

            // Gets the episode on the current index
            Episode episode = schedule.getEpisode(scheduleIndex);

            // Gets the episodes start/end date/time in a "dd/mm hh:mm" format
            String startDateAndTime = episode.getEpisodeStartDate() + " " + episode.getEpisodeStartTime();
            String endDateAndTime = episode.getEpisodeEndDate() + " " + episode.getEpisodeEndTime();

            // Sets the episodes image, name, start time/date, end time/date and description as values on the table
            table.setValueAt(episode.getEpisodeImage(), rowIndex, 0);
            table.setValueAt(episode.getEpisodeName(), rowIndex, 1);
            table.setValueAt(startDateAndTime, rowIndex, 2);
            table.setValueAt(endDateAndTime, rowIndex, 3);
            table.setValueAt(episode.getEpisodeDescription(), rowIndex, 4);

            scheduleIndex++;
            rowIndex++;
        }
    }

    /**
     * This method sets the visibility of the loading radio info dialogbox. This dialogbox
     * will be visible upon starting the program and dissapear after the program has loaded all the
     * schedules and displayed the default schedule in the table (P1).
     *
     * @param visibility, the visibility of the dialogbox.
     */
    public void setLoadingRadioInfoVisibility (boolean visibility) {
        loadingRadioInfo.setVisible(visibility);
    }

    /**
     * This method sets the visibility of the updating radio info dialogbox. This dialogbox
     * will be visible upon updating the program and dissapears after the program has updated all
     * the schedules.
     *
     * @param visibility, the visibility of the dialog box.
     */
    public void setUpdatingRadioInfoVisibility (boolean visibility) {
        updatingRadioInfo.setVisible(visibility);
    }

    /**
     * This method sets the name of the current channel dipplayed in the table
     *
     * @param currentChannel, the name of the current channel
     */
    public void setCurrentChannel (String currentChannel) {
        channel.setText(currentChannel);
    }

    /**
     * This method gets the name of the current channel displayed in the table
     *
     * @return the name of the current channel
     */
    public String getCurrentChannel () {
        return channel.getText();
    }

    /**
     * This method sets the current program number.
     *
     * @param currProgramNumber, the current program number.
     */
    public void setCurrentProgramNumber (int currProgramNumber) {

        currentProgramNumber = currProgramNumber;
    }

    /**
     * This method gets the current program number.
     *
     * @return the current program number.
     */
    private Integer getCurrentProgramNumber () {
        return currentProgramNumber;
    }

    /**
     * This method is used to get the P1Button
     *
     * @return the p1Button
     */
    public JButton getP1Button () {
        return p1Button;
    }

    /**
     * This method is used to get the P2Button
     *
     * @return the P2Button
     */
    public JButton getP2Button () {
        return p2Button;
    }

    /**
     * This method is used to get the P3button
     *
     * @return the p3Button
     */
    public JButton getP3Button () {
        return p3Button;
    }

    /**
     * This method is used to get the p4Blekinge button
     *
     * @return the p4Blekinge button
     */
    public JButton getP4BlekingeButton () {
        return p4BlekingeButton;
    }

    /**
     * This method is used to get the P4Dalarna button
     *
     * @return the p4Dalarna button
     */
    public JButton getP4DalarnaButton () {
        return p4DalarnaButton;
    }

    /**
     * This method is used to get the P4Gotland button
     *
     * @return the p4Gotland button
     */
    public JButton getP4GotlandButton () {
        return p4GotlandButton;
    }


    /**
     * This method is used to get the P4Gaevleborg button
     *
     * @return the p4Gaevleborg button
     */
    public JButton getP4GaevleborgButton () {
        return p4GaevleborgButton;
    }

    /**
     * This method is used to get the P4Goeteborg button
     *
     * @return the p4Goeteborg button
     */
    public JButton getP4GoeteborgButton () {
        return p4GoeteborgButton;
    }

    /**
     * This method is used to get the P4Halland button
     *
     * @return the p4Halland button
     */
    public JButton getP4HallandButton () {
        return p4HallandButton;
    }

    /**
     * This method is used to get the P4Jaemtland button
     *
     * @return the p4Jaemtland button
     */
    public JButton getP4JaemtlandButton () {
        return p4JaemtlandButton;
    }

    /**
     * This method is used to get the update button
     *
     * @return the update button
     */
    public JButton getUpdateButton () {
        return updateButton;
    }
	
    /*
     * This is a nested inner class for my table cell renderer.
     */
    public class MyTableCellRenderer extends DefaultTableCellRenderer {

        /**
         * This method is used to color the rows in the table.
         * Rows that are under the current program number (which has already aired) gets the light gray color.
         * The row that has the same value as the current program number and is airing right now gets the cyan color.
         * The rows that are over the current program number and has yet to be aired gets the green color.
         * The column headers all get a white color.
         *
         * @param table, the table.
         * @param value, the value in a cell of the table.
         * @param isSelected, if its selected.
         * @param hasFocus, if it has focus.
         * @param row, the row of the cell.
         * @param column, the column of the cell.
         * @return the component (the cell).
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {

            isSelected = true;
            hasFocus = false;
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Colors the table cells
            if (row == getCurrentProgramNumber()) {
                c.setBackground(Color.CYAN);
            } else if (row < getCurrentProgramNumber()) {
                c.setBackground(Color.LIGHT_GRAY);
            } else if (row > getCurrentProgramNumber()) {
                c.setBackground(Color.GREEN);
            }

            // Colors the column headers
            if ((value == "Image") || (value == "Program") || (value == "Start") ||
                    (value == "End") || (value == "Description")) {
                c.setBackground(Color.WHITE);
            }

            return c;
        }
    }

    /*
     * This is a nested class for my label renderer.
     */
    public static class MyLabelRenderer implements TableCellRenderer {

        /**
         * This method is used to return the component of the table cell.
         *
         * @param table, the table.
         * @param value, the value in a cell of the table.
         * @param isSelected, if its selected.
         * @param hasFocus, if it has focus.
         * @param row, the row of the cell.
         * @param column, the column of the cell.
         * @return the component (the cell).
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            return (Component)value;
        }
    }
}
