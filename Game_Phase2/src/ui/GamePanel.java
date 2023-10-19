package ui;

import graph.*;
import java.awt.*;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
        import java.util.List;

import java.util.Random;
import java.awt.event.*;
import javax.swing.*;

import games.BestUpperBound;
import games.ToTheBitterEnd;

import java.awt.Color;

public class GamePanel extends JPanel {
    
    
    
    /**
     *
     */

    private static final long serialVersionUID = -5973075508835915991L;
    private ArrayList<JButton> Buttons = new ArrayList<JButton>();
    private ArrayList<Color> Colors =new ArrayList<Color>();
    private final Random rand = new Random();
    private JPanel buttonPanel;
    JButton addColorButtonB;
    JButton hintButton;
    Graph g;
    public GraphPanel graphView;
    public JPanel hintPanel;
    public boolean randomorder;
    
    public GamePanel(){}
    public GamePanel(Graph G) {
        g = G;
        randomorder = false;
        //g.colourArray[0] = 1;
        graphView = new GraphPanel(g,false);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        this.setLayout(new BorderLayout());
        
        hintPanel = new JPanel();
        hintButton = new JButton("Hint");
        hintPanel.add(hintButton);

        hintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateHint();
            }
        });

        this.add(hintPanel, BorderLayout.SOUTH);
        this.add(graphView, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.NORTH);

        JButton printB = new JButton("Print Graph");

        printB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < g.vertices; i++) {
                    System.out.print(graphView.graph.colourArray[i]);
                    System.out.print(", ");
                }
                System.out.println("");
                System.out.println("");
            }
        });
        buttonPanel.add(printB);

        JButton addColorButtonB = new JButton("+");
        addColorButtonB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addColorButton();
            }
        });
        buttonPanel.add(addColorButtonB);

        for (int i = 0; i < 2; i++) {
            addColorButton();
        }
        this.setVisible(true);
    }
    public GamePanel(Graph G,boolean enforceOrder) {
        g = G;
        randomorder = true;
        //g.colourArray[0] = 1;
        
        graphView = new GraphPanel(g,enforceOrder);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        this.setLayout(new BorderLayout());
        hintPanel = new JPanel();
        hintButton = new JButton("Hint");
        hintPanel.add(hintButton);

        hintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateHint();
            }
        });

        this.add(hintPanel, BorderLayout.SOUTH);
        this.add(graphView, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.NORTH);

        JButton printB = new JButton("Print Graph");

        printB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < g.vertices; i++) {
                    System.out.print(graphView.graph.colourArray[i]);
                    System.out.print(", ");
                }
                System.out.println("");
                System.out.println("");
            }
        });
        buttonPanel.add(printB);



        JButton addColorButtonB = new JButton("+");
        addColorButtonB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addColorButton();
            }
        });
        buttonPanel.add(addColorButtonB);
        

        for (int i = 0; i < 2; i++) {
            addColorButton();
        }
        this.setVisible(true);
    }

    public void addColorButton(){
        Color color = new Color(rand.nextInt(0xFFFFFF));
        Colors.add(color);
        JButton b = new JButton();
       
        b.setBackground(color);
        b.setOpaque(true);
        Buttons.add(b);
        Integer index = Buttons.indexOf(b) + 1;
        b.setText(index.toString());
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphView.color = color;
                graphView.colorIdx = Buttons.indexOf(b);
                
            }
        });
        
        buttonPanel.add(b);
        buttonPanel.updateUI();
    }

    
    /** 
     * @param vertexListener
     */
    public void setVertexListener(VertexListener vertexListener) {
        graphView.setVertexListener(vertexListener);
    }
    
    /** 
     * @param arr
     * @param toCheckValue
     * @return boolean
     */
    private static boolean checkIn(int[] arr, int toCheckValue) 
    { 
        // check if the specified element 
        // is present in the array or not 
        // using Linear Search method 
        boolean res = false; 
        for (int element : arr) { 
            if (element == toCheckValue) { 
                res = true; 
                break; 
            } 
        } 
        return res;
    }

    
    /** 
     * @param g
     * @param colorArray
     * @return List<List<Integer>>
     * returns a list of colors which are represented as a list of vertices colored with this color
     */
    public List<List<Integer>> getColorPos(Graph g, int[] colorArray,boolean player) {
        List<Integer> colors = new ArrayList<Integer>(2);
        int numColors = 0;
        for (int i = 0; i < g.vertices; i++) {
            if (!colors.contains(colorArray[i])&& (colorArray[i]!=0|| !player)){
                colors.add(colorArray[i]);
                numColors++;
            }
        }
        List<List<Integer>> colorPos = new ArrayList<List<Integer>>(numColors);
        for (int i = 0; i < numColors; i++) {
            ArrayList<Integer> inner = new ArrayList<Integer>();
            colorPos.add(inner);
        }
        for (int i = 0; i < numColors; i++) {
            for (int j = 0; j < g.vertices; j++) {
                if (colors.get(i) == colorArray[j]) {
                    colorPos.get(i).add(j);
                }
            }
        }
        return colorPos;
    }

    
    /** 
     * @param goodColors
     * @param playerColors
     * @return List<Integer>
     * This method compares the Coloring from the backtracking approach with the coloring the user has entered and finds vertices 
     * that might be colored in a way that prevents the player from finding a solution with the best possible chromatic number 
     */
    public List<Integer> compareColorings(List<List<Integer>> goodColors, List<List<Integer>> playerColors) {
        List<List<Integer>> expectedColors = new ArrayList<List<Integer>>(playerColors.size());
        for (int i = 0; i < playerColors.size(); i++) {
            expectedColors.add(null);
        }
        for (List<Integer> color : goodColors) {
            int bestOverlap = 0;
            int target = -1;
            for (int i = 0; i < playerColors.size(); i++) {
                int overlap = 0;
                for (Integer vertex : color) {
                    if (playerColors.get(i).contains(vertex))
                        overlap++;
                }
                if (overlap > bestOverlap) {
                    bestOverlap = overlap;
                    target = i;
                }

            }
            
            if (target != -1) {
                expectedColors.set(target, color);
            }
        }
        List<Integer> misses = new ArrayList<Integer>(2);
        int i = 0;
        for (List<Integer> color : playerColors) {
            for (Integer vertex : color) {
                if(expectedColors.get(i)!= null&&!expectedColors.get(i).contains(vertex))
                    misses.add(vertex);
            }
            i++;
        }
        return misses;
    }
    //generates randomly a hint and prints it
    public void generateHint() {
        List<Integer> misses;
        if (!randomorder) {
            
            Graph data = graphView.gl.g;
            data.computeChromaticNumber();
            List<List<Integer>> playerColors = getColorPos(data, data.colourArray,true);
            List<List<Integer>> goodColors = getColorPos(data, data.finalColourArray,false);
            misses = compareColorings(goodColors, playerColors);
        } else {
            misses=new ArrayList<Integer>();
        }
        int hintType;

        if(misses.size() > 0) {
            hintType = 0;
            String vertexString = "";
            for (Integer miss : misses) {
                vertexString += ("" + miss + ", ");
            }
            vertexString = vertexString.substring(0, vertexString.length() - 2);
            JLabel hintLabel = new JLabel("Take a closer look at the vertices: "+vertexString+" since they might be colored incorrectly");
            JOptionPane.showMessageDialog(null, hintLabel, "Cheers!", 3);
        }else{
            hintType = (int) (Math.random()*2 +1);
        }
        //prints out the vertices with the most edges
        if (hintType == 1) {
            int mostEdges = 0;
            String verticeNumber = "";
            for (int i =0; i < g.adjacencyMatrix.length; i++ ) {
                int connectionCounter = 0;
                for (int j = 0; j<g.adjacencyMatrix[0].length; j++) {
                    if (g.adjacencyMatrix[i][j] == 1) {
                        connectionCounter++;
                    }
                }
                if (connectionCounter >= mostEdges) {
                    mostEdges = connectionCounter;
                    verticeNumber += i;
                    verticeNumber += " ";
                }
            }
            JLabel hintLabel = new JLabel("Take a closer look at the vertices: "+verticeNumber+" since they have the most edges");
            JOptionPane.showMessageDialog(null, hintLabel, "Cheers!", 3);
        }
        
        //prints out the exact chromatic number
        if (hintType == 2) {
            GUI gui = new GUI(1);
            if (gui.bitterEnd.isSelected()) {
                JOptionPane.showMessageDialog(null, "The Chromatic Number is: "+g.chromaticNumber, "Cheers!", 3);
            }
            else {
                g.computeChromaticNumber();
                JOptionPane.showMessageDialog(null, "The Chromatic Number is: "+g.chromaticNumber, "Cheers!", 3);
            }
        }

    }
}
