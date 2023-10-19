package ui;

import javax.swing.*;
import graph.*;
import java.awt.Graphics2D;
//import java.awt.event.*;
import java.awt.Graphics;


public class EdgePanel extends JPanel
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static Graph graph;
    public static GraphLayout gl;
    public static int[][] pltP;

    public EdgePanel(Graph G,GraphLayout gL) {
        graph = G;
        gl = gL;
        this.setLayout(null);
        this.setVisible(true);
        setDoubleBuffered(true);
        this.setBorder(UIManager.getBorder("ComboBox.border"));
        this.repaint();


        
    }

    public void drawGraphic()
    {
        repaint();
    }
    /**
     * Draws the edges as lines on the background panel 
     * @param g
     */
    void drawLines(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < graph.vertices; i++) {
            for (int j = 0; j < graph.vertices; j++) {

                if (graph.adjacencyMatrix[i][j] == 1) {

                    int[] start = pltP[i];
                    int[] end = pltP[j];
                    
              
                    // Coordinates are the centre points of the VertexButtons
                    g2d.drawLine(start[0] , start[1] , end[0] , end[1] );
                }
            }
        }

    }

    
    /** 
     * @param g
     */
    public void paint(Graphics g)
    {
        super.paintComponent(g);
        pltP = gl.pltP;
        drawLines(g);

    }
}
