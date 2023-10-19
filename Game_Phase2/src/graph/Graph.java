package graph;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class Graph {
    public ColEdge[] e = null;
    public int vertices; // n
    public int edges; // m
    public int[][] adjacencyMatrix;
    // used for the game logic
    public int[] colourArray;
    // usd for our computed graph coloring
    public int[] finalColourArray;
    public final boolean DEBUG = false;
    public final String COMMENT = "//";
    public int chromaticNumber;



    public Graph(String filename) {

        String inputfile = filename;

        boolean seen[] = null;

        // ! n is the number of vertices in the graph
        vertices = -1;

        // ! m is the number of edges in the graph
        edges = -1;

        // ! e will contain the edges of the graph
        ColEdge e[] = null;

        try {
            FileReader fr = new FileReader(inputfile);
            BufferedReader br = new BufferedReader(fr);

            String record = new String();

            // ! THe first few lines of the file are allowed to be comments, staring with a
            // // symbol.
            // ! These comments are only allowed at the top of the file.

            // ! -----------------------------------------
            while ((record = br.readLine()) != null) {
                if (record.startsWith("//"))
                    continue;
                break; // Saw a line that did not start with a comment -- time to start reading the
                       // data in!
            }

            if (record.startsWith("VERTICES = ")) {
                vertices = Integer.parseInt(record.substring(11));
                if (DEBUG)
                    System.out.println(COMMENT + " Number of vertices = " + vertices);
            }

            seen = new boolean[vertices + 1];

            record = br.readLine();

            if (record.startsWith("EDGES = ")) {
                edges = Integer.parseInt(record.substring(8));
                if (DEBUG)
                    System.out.println(COMMENT + " Expected number of edges = " + edges);
            }

            e = new ColEdge[edges];

            for (int d = 0; d < edges; d++) {
                if (DEBUG)
                    System.out.println(COMMENT + " Reading edge " + (d + 1));
                record = br.readLine();
                String data[] = record.split(" ");
                if (data.length != 2) {
                    System.out.println("Error! Malformed edge line: " + record);
                    System.exit(0);
                }
                e[d] = new ColEdge();

                e[d].u = Integer.parseInt(data[0]);
                e[d].v = Integer.parseInt(data[1]);

                seen[e[d].u] = true;
                seen[e[d].v] = true;

                if (DEBUG)
                    System.out.println(COMMENT + " Edge: " + e[d].u + " " + e[d].v);

            }

            String surplus = br.readLine();
            if (surplus != null) {
                if (surplus.length() >= 2)
                    if (DEBUG)
                        System.out.println(
                                COMMENT + " Warning: there appeared to be data in your file after the last edge: '"
                                        + surplus + "'");
            }
            br.close();
        } catch (IOException ex) {
            // catch possible io errors from readLine()
            System.out.println("Error! Problem reading file " + inputfile);
            System.exit(0);
        }

        for (int x = 1; x <= vertices; x++) {
            if (seen[x] == false) {
                if (DEBUG)
                    System.out.println(COMMENT + " Warning: vertex " + x
                            + " didn't appear in any edge : it will be considered a disconnected vertex on its own.");
            }
        }
        adjacencyMatrix = createAdjacencyMatrix(e, vertices);
        colourArray = new int[vertices];
    }

    /**
     * @param vertices
     * @param edges
     */
    // creating a random graph
    public Graph(int numVertices, int numEdges) {
        vertices = numVertices;
        edges = numEdges;
        Random rand = new Random();
        adjacencyMatrix = new int[vertices][vertices];
        e = new ColEdge[edges];
        colourArray = new int[vertices];
        int count = 0;

        // setting all fields of the adjacency matrix to zero
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                adjacencyMatrix[i][j] = 0;
            }
        }

        // randomly setting fields of the upper triangle to 1 according to the number of
        // edges

        do {
            int i = rand.nextInt(vertices);
            int j = rand.nextInt(vertices);
            if (!(adjacencyMatrix[i][j] == 1 || i == j)) {
                count++;
                adjacencyMatrix[i][j] = 1;
                adjacencyMatrix[j][i] = 1;
            }
        } while (!(count == edges)); // as long as the amount of edges is not right it repeats the process

        // printing the adjacency matrix
        if (DEBUG) {
            System.out.println("adjacency matrix:");
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    System.out.print(adjacencyMatrix[i][j]);
                }
                System.out.println();
            }
        }

        // creating the 2d array of the graph
        int countRow = 0;
        for (int j = 0; j < vertices - 1; j++) {
            for (int k = j + 1; k < vertices; k++) {
                if (adjacencyMatrix[j][k] == 1) {
                    ColEdge eh = new ColEdge();
                    eh.v = j;
                    eh.u = k;
                    e[countRow] = eh;
                    countRow++;
                }
            }
        }
    }

    /**
     * @param e
     * @param n
     * @return int[][]
     */

    public static int[][] createAdjacencyMatrix(ColEdge[] e, int n) {
        int[][] adjacent = new int[n][n];
        for (int i = 0; i < e.length; i++) {
            adjacent[e[i].u - 1][e[i].v - 1] = 1;
            adjacent[e[i].v - 1][e[i].u - 1] = 1;
        }
        return adjacent;
    }

    
    /** 
     * @return int
     */
    public int getNumberOfColoursUsed() {
        int count = 0;
        int maxColour = vertices + 1;
        boolean[] used = new boolean[maxColour];
        for (int colour = 1; colour < maxColour; colour++) {
            used[colour] = false;
        }

        for (int i = 0; i < vertices; i++) {
            used[colourArray[i]] = true;
        }
        for (int colour = 1; colour < maxColour; colour++) {
            if (used[colour]) {
                count++;
            }
        }
        return count;
    }

    //Computes the exact Chromatic Number for the Graph (algorithm from phase 1)
    public void computeChromaticNumber() {
        chromaticNumber = 2;
        finalColourArray = new int[vertices];

        while (!graphColour(0)) {
            chromaticNumber++;
        }
        if(DEBUG)
        {
            System.out.println("There is a solution for Chromatic number = " + chromaticNumber
                    + " This solution colors the Vertices as follows:");
            for (int i = 0; i < finalColourArray.length; i++) {
                System.out.print("" + finalColourArray[i] + ", ");
            }
        }
        
    }

    
    /** 
     * @param vertex
     * @return boolean
     */
    //Part of the exact CN algorithm from phase 1
    public boolean graphColour(int vertex) {
        boolean res;

        for (int color = 1; color <= chromaticNumber; color++) {
            // Iterate over the available colors and check wether or not that color can
            // color the k'th vertex
            if (isSafe(vertex, color)) {
                // If we can use this color for this vertex assign it to be that way
                finalColourArray[vertex] = color;
                if (vertex + 1 == vertices) {
                    // If we are at the last vertex return True
                    return true;
                } else {
                    // If we arent at the last vertex return a new call of graphColor to color the
                    // next vertex
                    res = graphColour(vertex + 1);
                    if (res) {
                        return res;
                    }
                }
            }
        }
        // If we run out of colors return False
        return false;
    }

    
    /** 
     * @param vertex
     * @param color
     * @return boolean
     */
    //Part of the exact CN algorithm from phase 1
    public boolean isSafe(int vertex, int color) {
        // Tests if we can use this color to assign to this vertex
        for (int i = 0; i < vertex; i++) {
            if (adjacencyMatrix[vertex][i] == 1 && color == finalColourArray[i]) {
                return false;
                // Return false if we find any adjacent edge of the same color
            }
        }
        return true;
    }

    
    /** 
     * @param vertex
     * @param colorIdx
     * @return boolean
     */
    public boolean isAllowed(int vertex, int colorIdx){
        boolean colorCorrect=true;
        for (int j = 0; j < vertices; j++) {
            if(vertex!=j){
                if(adjacencyMatrix[vertex][j]==1 && colorIdx==colourArray[j]){
                    colorCorrect=false;
                    break;
                }

            }
        }
        return colorCorrect;
    }

    
    /** 
     * @return boolean
     */
    public boolean checkColor() {
        boolean colorCorrect=true;

            // Goes through all the vertices on the right side of the graph
        for (int i = 0 ; i < vertices ; i++){
            for (int j = 0; j < vertices; j++) {
                if(i!=j){
                    if(adjacencyMatrix[i][j]==1 && colourArray[i]==colourArray[j]){
                        colorCorrect=false;
                        break;
                    }

                }
            }
        }
            // If the color of the buttons is the same, then the color is false
        return colorCorrect;
    }

}

class ColEdge {
    int u;
    int v;
}
