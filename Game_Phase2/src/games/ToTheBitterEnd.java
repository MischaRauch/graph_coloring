package games;

import graph.*;
import ui.*;
import java.awt.event.*;
import java.util.Arrays;
import java.awt.BorderLayout;

import javax.swing.*;


public class ToTheBitterEnd extends GamePanel {

    JFrame frame;
    JPanel panel;
    JLabel labelTimer;
    Timer timer;
    JButton check;
    int seconds;
    int userCN;
    Graph g;
    boolean notDone;



    public ToTheBitterEnd(Graph graph) {
        //creates an object from the inherited class GamePanel
        super(graph);
        g = graph;
        g.computeChromaticNumber();
        frame = new JFrame("Graph sample ");
        panel = new JPanel();

        labelTimer = new JLabel("TIMER:\t\t");
        check = new JButton("DONE?");

        panel.add(labelTimer);
        panel.add(check);

        //add the specific elements for the game mode to the south of the BorderLayout
        hintPanel.add(panel, BorderLayout.SOUTH);

        frame.add(this);
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 400);
        frame.setVisible(true);

        timer();
        checkDone();
    }

    //starts the timer
    public void timer() {
        int delay = 1000;
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    seconds++;
                    labelTimer.setText("TIMER: "+seconds+"\t\t");
                }
            };
        timer = new Timer(delay, taskPerformer);
        timer.start();
    }

    //checks if the used colors matches with the chromatic number if so the game is finished
    public void checkDone() {
        ActionListener checkDone = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateUserCN();
                if(userCN == g.chromaticNumber) {
                    timer.stop();
                    JOptionPane.showMessageDialog(null, "Congrats - you finished with the lowest Chromatic Number possible", "Cheers!", 3);
                    notDone = false;
                }
                if(notDone) {
                    JOptionPane.showMessageDialog(null, "There is a lower Chromatic Number - try it one more time!", "Not yet done :(", 3);
                }

            }
        };
        check.addActionListener(checkDone);
    }

    //calculates the used colors from the user ie. chromatic number
    public void calculateUserCN() {
        userCN = 0;
        int [] copyColors = new int[graphView.graph.colourArray.length];
        for (int i = 0; i < g.vertices; i++) {
            if (graphView.graph.colourArray[i] == 0) {
                JOptionPane.showMessageDialog(null, "You are not finished, color ALL vertices", "ERROR", 3);
                notDone = false;
                break;
            }
            boolean foo = true;
            copyColors[i] = graphView.graph.colourArray[i];

            for (int j =0; j < g.vertices; j++) {
                if (i == j) {
                    break;
                }
                if (copyColors[i] == copyColors[j]) {
                    foo = false;
                }
            }
            if(foo) {
                userCN++;
            }
            notDone = true;
        }
    }
}
