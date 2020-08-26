/*
 * Jakob Mukka
 *
 * Version 1,1 Date: 2020-08-26
 *
 * This is the main class of the Radio Info program. The Radio Info program uses the SR-Api
 * to get channels and each channels schedule. It then displays it in a GUI where the user can
 * see all programs from a certain channel that has been aired or will be aired within 12 hours in a table.
 * Programs that have already aired are displayed in grey, the program that is currently airing is
 * displayed in cyan and the programs that will be aired are displayed in the green.
 *
 */

package Program;

import javax.swing.*;

public class RadioInfo {

    /**
     * The main method of the RadioInfo program
     *
     * @param args program arguments (if there are any)
     */
    public static void main (String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            Control control = new Control();
        });
    }
}
