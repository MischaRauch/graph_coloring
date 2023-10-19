package ui;

import javax.swing.*;

import games.RandomOrder;
import graph.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.awt.*;

// class VertexRep extends JButton {
//     private static final long serialVersionUID = 1L;

//     public VertexRep(String name) {
//         super(name);
//     }
// }

public class GraphPanel extends JPanel implements MouseWheelListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static int[][] pltP;
    public Graph graph;
    public GraphLayout gl;
    public VertexButton[] buttons;
    public JLayeredPane lp;
    public Color color;
    public int colorIdx;
    private VertexButton selectedVertex;
    private static int vertexRadius = 20;
    EdgePanel ep;
    VertexListener vertexListener;

    private double zoomFactor = 1;
    private double prevZoomFactor = 1;
    private boolean zoomer;
    private double xOffset = 0;
    private double yOffset = 0;



    public GraphPanel(Graph G,boolean enforceOrder) {
        graph = G;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        int width = getWidth();
        int height = getHeight();
        this.setSize(width, height);

        colorIdx = -1;
        lp = new JLayeredPane();
        gl = new GraphLayout(graph);
        ep = new EdgePanel(graph, gl);

        ep.setBounds(0, 0, width, height);
        ep.setVisible(true);
        lp.add(ep, JLayeredPane.DEFAULT_LAYER);
        
        buttons = new VertexButton[graph.vertices];
        gl.move(20000);
        pltP = gl.pltP;
        if (enforceOrder) {
            initButtonsRANDOMORDER();
        } else {
            initButtons();
        }
        
        updateButtons();
        this.add(lp);
        lp.setBounds(0, 0, width, height);

        this.setVisible(true);

    }

    /**
     * Initalises buttons at the right positions with action liusteners for the randomorder game mode
     */
    void initButtonsRANDOMORDER() {
        RandomOrder RandomOrderObj = new RandomOrder();

        for (int i = 0; i < graph.vertices; i++) {
            VertexButton b = new VertexButton(String.valueOf(i), vertexRadius);
          
            buttons[i] = b;
        
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
  
                    String text = b.getText(); 

                    int x = Integer.parseInt(text); 
                    int counter = RandomOrderObj.getNumber();
     
                    if(x == counter){
                        if (color != null && graph.isAllowed(Integer.parseInt(b.getText()), colorIdx + 1)) {
                            b.setVertexColor(color);
                            int vertex = Integer.parseInt(b.getText());
                            graph.colourArray[vertex] = colorIdx + 1;

                            if((RandomOrderObj.counter)+1 >= RandomOrderObj.order.size()) {
                                graph.computeChromaticNumber();
                                //Calculate User CN
                                int userCN = 0;
                                int [] copyColors = new int[graph.colourArray.length];
                                for (int i = 0; i < graph.vertices; i++) {
                                    boolean foo = true;
                                    copyColors[i] = graph.colourArray[i];

                                    for (int j =0; j < graph.vertices; j++) {
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
                                }
                                JOptionPane.showMessageDialog(null, "You finished the Game\n The Chromatic number is: "+graph.chromaticNumber+ "\n You needed: "+ userCN+" colors", "WIN", 3);
                            }
                            else {
                            RandomOrderObj.generateNumber();
                            RandomOrder.nextVertice.setText("Color next vertice: "+RandomOrderObj.getNumber());
                            }
                        } 
                        else if (color == null) {
                            JOptionPane.showMessageDialog(null, "Please choose first a color", "ERROR", 3);
                        } 
                        else {
                            JOptionPane.showMessageDialog(null, "You cant color this vertex in this color as one of it's neighbours already has its color", "ERROR", 3);
                        }
                        selectedVertex = b;
                        selectedVertex.setSelected(true);
                        
                        graph.colourArray[Integer.parseInt(b.getText())] = colorIdx+1;                        
                     }
                }
            });
            lp.add(b, JLayeredPane.POPUP_LAYER);
        }
    }
    
    /**
     * Initalises buttons at the right positions with bound action listeners
     */
    
    void initButtons() {
        for (int i = 0; i < graph.vertices; i++) {
            VertexButton b = new VertexButton(String.valueOf(i), vertexRadius);

            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    if (color != null && graph.isAllowed(Integer.parseInt(b.getText()), colorIdx + 1)) {
                        b.setVertexColor(color);
                        int vertex = Integer.parseInt(b.getText());
                        graph.colourArray[vertex] = colorIdx + 1;
                    } 
                    else if (color == null) {
                        JOptionPane.showMessageDialog(null, "Please choose first a color", "ERROR", 3);
                    } 
                    else {
                        JOptionPane.showMessageDialog(null, "You cant color this vertex in this color as one of it's neighbours already has its color", "ERROR", 3);
                    }
                    if (selectedVertex != null) {
                        selectedVertex.setSelected(false); // previous selected vertex
                    }
                    selectedVertex = b;
                    selectedVertex.setSelected(true);

                    
                    int vertex = Integer.parseInt(b.getText());
                    if (vertexListener != null) {
                        vertexListener.vertexSelected(vertex);
                    }

                }
            });
            buttons[i] = b;
            lp.add(b, JLayeredPane.POPUP_LAYER);

        }

    }
    void updateButtons() {
        for (int i = 0; i < graph.vertices; i++) {
            buttons[i].setCenter(pltP[i][0], pltP[i][1]);
        }
    }

    
    /** 
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        AffineTransform at = new AffineTransform();
        Graphics2D g2 = (Graphics2D) g;
        

        if (zoomer) {
            double avgX=0;
            double avgY=0;
            for (int i = 0; i < gl.pts.length; i+=2) {
                if(gl.pts[i]<avgX)
                    avgX += gl.pts[i];
                if(gl.pts[i+1]<avgY)
                    avgY += gl.pts[i+1];
            }
            avgX = avgX / gl.g.vertices;
            avgY = avgY / gl.g.vertices;
            double zoomDiv = zoomFactor / prevZoomFactor;
            
            xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * avgX;
            yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * avgY;

            at.translate(xOffset, yOffset);
            at.scale(zoomFactor, zoomFactor);
            prevZoomFactor = zoomFactor;
            zoomer = false;
        }





        int width = getWidth();
        int height = getHeight();
        pltP = gl.getPlotPoints(at);
        ep.setBounds(0, 0, width, height);
        updateButtons();
        addMouseWheelListener(this);

    }

    
    /** 
     * @param vertexListener
     */
    public void setVertexListener(VertexListener vertexListener) {
        this.vertexListener = vertexListener;
    }

    
    /** 
     * @param e
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //System.out.println("MW event");
        zoomer = true;

        // Zoom in
        if (e.getWheelRotation() < 0) {
            zoomFactor *= 1.001;
            repaint();
        }
        // Zoom out
        if (e.getWheelRotation() > 0) {
            zoomFactor *= 0.999;
            repaint();
        }
    }

    


}
