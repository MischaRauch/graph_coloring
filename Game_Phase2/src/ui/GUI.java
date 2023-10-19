package ui;
import javax.swing.*;

import games.BestUpperBound;
import games.ToTheBitterEnd;
import games.RandomOrder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import graph.*;

class ColEdge{
	int u;
	int v;
}



public class GUI implements ActionListener {
  JPanel panel = new JPanel();
  JFrame frame = new JFrame("GUI");
  private File finalfile;
  //static variables to identify with other objects which game mode is choosen
  public static JRadioButton bitterEnd;
  public static JRadioButton bestUpperBound;
  public static JRadioButton randomOrder;

  JTextField inputVertices;
  JTextField inputEdges;
  int vertices;
  int edges;

  public GUI(int number){}
  public GUI() {
    title();
    //creates the left side of the user interface
    graphConfig();
    readIn();
    generateRandomGraph();

    //creates the right side of the user interface
    gameStyle();
    letsPlay();

    frame.setSize(900,500);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(panel);
    panel.setLayout(null);
    frame.setVisible(true);

  }

  public void title() {
    JLabel header = new JLabel("Graph Coloring Game");
    Font myFont = new Font("Verdana", Font.BOLD,50);
    header.setFont(myFont);
    header.setBounds(100,0,1000,100);
    panel.add(header);
  }

  public void graphConfig() {
    JLabel tVertices = new JLabel("Number of Vertices:");
    tVertices.setBounds(50,150,130,25);
    panel.add(tVertices);

    inputVertices = new JTextField(10);
    inputVertices.setBounds(190,150,130,25);
    panel.add(inputVertices);

    JLabel tEdges = new JLabel("Number of Edges:");
    tEdges.setBounds(50,180,130,25);
    panel.add(tEdges);

    inputEdges = new JTextField(10);
    inputEdges.setBounds(190,180,130,25);
    panel.add(inputEdges);

    JLabel background = new JLabel();
    background.setOpaque(true);
    background.setBackground(Color.lightGray);
    background.setBounds(50,150,270,60);
    panel.add(background);
  }
  
  public void readIn() {
    JButton file;
	  //the read JLabel describes the button
    JLabel read = new JLabel("Read in Graph from file:");
    read.setBounds(50,240,150,25);
    panel.add(read);

    //constructing the open file button
    file = new JButton("Open File");
    file.setBounds(200,240,120,25);
    panel.add(file); // adding the button    
    
    class readFile implements ActionListener { //actionListener to read in a file
      public void actionPerformed(ActionEvent e) {
    	  
    	if(e.getSource() == file) {
    		JFileChooser fileChooser = new JFileChooser();
    		
    		fileChooser.setCurrentDirectory(new File("."));
    		int response = fileChooser.showOpenDialog(null); //select file to open
    		
    		if(response == JFileChooser.APPROVE_OPTION) { // if a file is chosen it is approved (saved)
    			File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
          System.out.println("This is the path to the choosen file: " + file);
          finalfile=file;
    		}
    	}  
      }
    }
    
    file.addActionListener(new readFile());
    //Background styling, grey box for the Open file text block
    JLabel background = new JLabel();
    background.setOpaque(true);
    background.setBackground(Color.lightGray);
    background.setBounds(50,240,270,25);
    panel.add(background);
  }
    
    
  public void generateRandomGraph() {
    JButton randomGraphB = new JButton("Genereate Random Graph");
    randomGraphB.setBounds(50,300,260,50);
    randomGraphB.addActionListener(this);
    panel.add(randomGraphB);

    //ActionListener to generate a random Graph based on user input
    class generateRandomGraph implements ActionListener {
      public void actionPerformed(ActionEvent e) {  
        vertices = 0;
        edges = 0;
        String tmp = inputVertices.getText();
        vertices = Integer.parseInt(tmp);
          
        String tmp2 = inputEdges.getText();
        edges = Integer.parseInt(tmp2);
        if (edges > (vertices * (vertices - 1)) / 2) {
          JOptionPane.showMessageDialog(null, "There cant be more than n(n-1)/2 edges for a graph with n vertices",
              "ERROR", 3);
          edges = 0;
          vertices = 0;
        }
      }
    }
    randomGraphB.addActionListener(new generateRandomGraph());

  }
  
  //Generates radioButton to choose a gameStyle 
  public void gameStyle() {
    JLabel header = new JLabel("Choose your Game Style!");
    Font headerFont = new Font("Verdana",Font.BOLD,15);
    header.setFont(headerFont);
    header.setBounds(410,150,220,25);
    panel.add(header);

    bitterEnd = new JRadioButton("Bitter end");
    bitterEnd.setBounds(410,180,100,15);
    panel.add(bitterEnd);

    bestUpperBound = new JRadioButton("Best Upper Bound");
    bestUpperBound.setBounds(410,210,150,15);
    panel.add(bestUpperBound);

    randomOrder = new JRadioButton("Random Order");
    randomOrder.setBounds(410,240,150,15);
    panel.add(randomOrder);

    ButtonGroup group = new ButtonGroup();
    group.add(bitterEnd);
    group.add(bestUpperBound);
    group.add(randomOrder);
    

    JLabel background = new JLabel();
    background.setOpaque(true);
    background.setBackground(Color.lightGray);
    background.setBounds(410,150,270,120);
    panel.add(background);
  }

  //the play button which starts the game
  public void letsPlay() {
    JButton play = new JButton("Let's Play!");
    play.setBounds(410,300,260,50);
    play.addActionListener(this);
    panel.add(play);

    //check first if a graph can be generated
    class createGraph implements ActionListener  {
      public void actionPerformed(ActionEvent e) {
        Graph graph=new Graph(1,0);
        if (vertices != 0 && edges != 0) {
          
            graph = new Graph(vertices, edges);
        }
        else {
          try {
            graph = new Graph(finalfile.getAbsolutePath());
          }
          catch(Exception f) {
            JOptionPane.showMessageDialog(null, "Please insert a graph file or set Vertices and Edges", "ERROR", 3);
            }
          graph = new Graph(finalfile.getAbsolutePath());        
        }
      

        //if a graph can be generated start the corresponding game mode
        if (bitterEnd.isSelected()) {
          ToTheBitterEnd game = new ToTheBitterEnd(graph);
        }
        else if (bestUpperBound.isSelected()) {
          BestUpperBound game = new BestUpperBound(graph);
        }
        else if (randomOrder.isSelected()) {
          
          RandomOrder game = new RandomOrder(graph);
        }
        else {
          JOptionPane.showMessageDialog(null, "Please choose a Game Mode", "ERROR", 3);
        }

      }
    }
    play.addActionListener(new createGraph());
  }

  public void actionPerformed(ActionEvent e) {
  }
}