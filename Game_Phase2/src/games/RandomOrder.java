package games;

import graph.*;
import ui.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.util.Random;
import java.util.ArrayList;



public class RandomOrder extends GamePanel {
    JFrame frame;
    JPanel panel;
    VertexButton currentVertex;
    GraphPanel graphPanel;
    public static JLabel nextVertice;

    int userCN;
    int chromaticNumber;
    Graph g;
    public static int counter = 0;
    public static ArrayList < Integer > order = new ArrayList<Integer>();


    public RandomOrder(Graph graph) {
        super(graph,true);
        g = graph;

        frame = new JFrame("Graph sample ");
        panel = new JPanel();

        //reset counter and arrayList for next graph
        counter = 0;
        order.clear();
        generateOrder();
        
        nextVertice = new JLabel("Color next vertice: "+order.get(counter));
        System.out.println("ORDER: "+order);

        System.out.println("Current: "+order.get(counter));

        panel.add(nextVertice);

        hintPanel.add(panel, BorderLayout.SOUTH);

        frame.add(this);
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
    
    public RandomOrder() {
        super();
    }

    public void generateNumber() {
         counter++;
    }
    
    /** 
     * @return int
     */
    public int getNumber() {
        return order.get(counter);
    }

    public void generateOrder() {
        while(order.size() != g.vertices){
            int x = new Random().nextInt(g.vertices);
             if(!order.contains(x))
                   order.add(x);
         }
    }
}