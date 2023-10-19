package games;

import graph.*;
import ui.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;


public class BestUpperBound extends GamePanel implements VertexListener {
    Timer countdownTimer;
    int timeRemaining;
    JLabel timeLabel;
    JLabel colorsLabel;
    JButton hintButton2;
    JButton finishedButton;
    JFrame frame;
    public Graph graph;
    public BestUpperBound(){}

    public BestUpperBound(Graph graph) {
        super(graph);
        this.graph = graph;

        // calculate how much time a player gets to finish the game
        timeRemaining = (int) (20 + graph.vertices * graph.edges /3);
        
        frame = new JFrame("Graph sample ");
    
        
        super.setVertexListener(this);
        
        JPanel timerView = new JPanel();

        Font font = new Font("Courier", Font.BOLD,20);
        

        timeLabel = new JLabel(String.valueOf(timeRemaining) + " seconds left", JLabel.CENTER);
        timeLabel.setFont(font);
        colorsLabel = new JLabel("You used "  + graph.getNumberOfColoursUsed() +  " colours");
        finishedButton = new JButton("Done?");

        // When the player is finished this button can be pressed
        // It checks if every vertex has a colour and it shows a message if this is not the case
        // When every vertex is coloured it prints the number of colours used and stops the timer
        finishedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < graph.vertices; i++) {
                    if (graph.colourArray[i] == 0) {
                        JOptionPane.showMessageDialog(null, "You are not finished, color ALL vertices", "ERROR", 3);
                        return;
                    }     
                }
                countdownTimer.stop();
                graph.computeChromaticNumber();
                //JOptionPane.showMessageDialog(frame, "Congratulations, you used "  + graph.getNumberOfColoursUsed() +  " colours");
                JOptionPane.showMessageDialog(null, "You finished the Game\n The Chromatic number is: "+graph.chromaticNumber+ "\n You needed: "+ graph.getNumberOfColoursUsed()+" colors", "WIN", 3);

            }
        });
        
        // button for the hint that uses the greedy algorithm to suggest a color for a vertex
        hintButton2 = new JButton("Another hint");
        hintButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateHint hint = new CreateHint(graph);
                hint.makeHint();

            }
        });

        countdownTimer = new Timer(1000, new CountdownTimerListener());
        countdownTimer.start();
       
        timerView.add(timeLabel);
        timerView.add(colorsLabel);
        timerView.add(finishedButton);
        timerView.add(hintButton2);




        hintPanel.add(timerView, BorderLayout.SOUTH);
        frame.add(this);
        
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 400);
        frame.setVisible(true);

    }

    // method that checks if all the vertices have a color when the time is up
    // when all the vertices are coloured, it prints that the game is finished and how many colours are used
    // when not all the vertices are coloured, it prints that the game isn't finished
    void gameFinished(){
        if (frame.isVisible()) {
            for (int i = 0; i < graph.vertices; i++) {
                if (graph.colourArray[i] == 0) {
                    JOptionPane.showMessageDialog(frame, "You did not finish the game in time,  you used "  + graph.getNumberOfColoursUsed() +  " colours");
                    return;
                }     
            }
            //JOptionPane.showMessageDialog(frame, "Congratulations, you finished the game and used "  +  +  " colours");
            graph.computeChromaticNumber();
            JOptionPane.showMessageDialog(null, "You finished the Game\n The Chromatic number is: "+graph.chromaticNumber+ "\n You needed: "+ graph.getNumberOfColoursUsed()+" colors", "WIN", 3);
        }
    }

    // this class if for the timer
    // it prints how many seconds are left every second and when the time is up is prints this
    class CountdownTimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (--timeRemaining > 0) {
                timeLabel.setText(String.valueOf(timeRemaining) + " seconds left");
            } else {
                timeLabel.setText("Time's up!");
                countdownTimer.stop();
                gameFinished();
                
                
            }
        }
    }

    
    /** 
     * @param vertex
     */
    // This method is from the interface VertexListener
    // It updates the colorsLabel every time a button is pressed and prints the number of colours used
    public void vertexSelected(int vertex){
        colorsLabel.setText("You used "  + graph.getNumberOfColoursUsed() +  " colours");
    }
    
}
