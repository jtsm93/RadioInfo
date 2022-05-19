# RadioInfo
This is a Java-application that uses SR's (Sveriges Radio) open API where the user can, via a GUI, see schedules of the different SR channels.

# Installing and running the program

This program requires a jdk installed.

## Compiling the program in linux:

Go to the "program" folder and enter the following command

- javac Control.java RadioInfo.java View.java  Model/Channel.java Model/ChannelsCreator.java Model/Episode.java Model/Schedule.java Model/ScheduleCreator.java

## Running the program in linux:

Go to the "src" folder and enter the following command

- java Program/RadioInfo

## Compiling the program in windows:

Go to the "program" folder and enter the following command

- javac Control.java RadioInfo.java View.java  Model/Channel.java Model/ChannelsCreator.java Model/Episode.java Model/Schedule.java Model/ScheduleCreator.java

## Running the program in windows:

Stay in the "program" folder and enter the following command

- java -classpath ../ Program.RadioInfo

# How to use the program

When starting the program it will take a few minutes to load each channels schedule. When the loading window has dissapeared you can start using the program. The default channel, which is the first channel displayed, is P1. 

To switch channels go to the menubar on the lower side of the program window. To update the schedules for all channels click on the "Update" button in the upper right corner.
