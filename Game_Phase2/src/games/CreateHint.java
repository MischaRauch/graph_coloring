package games;

import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import graph.*;
import ui.*;

public class CreateHint {
    Graph graph;
    int vertices;
    int[] colourArray;
    int[] orderArray;
    int[][] adjacencyMatrix;

    public CreateHint (Graph graph){
        this.graph = graph;
        vertices = graph.vertices;
        colourArray = graph.colourArray;
        adjacencyMatrix = graph.adjacencyMatrix;
        
    }

    public void makeHint(){
        int [] orderArray = new int[vertices];
        int [] result = new int[vertices];
        int orderArrayIndex = 0;

        // fill the order array with the vertices that already have a color
        for (int i = 0; i < colourArray.length; i++){
            if (colourArray[i] != 0){
                orderArray[orderArrayIndex] = i;
                result[orderArrayIndex] = colourArray[i];
                orderArrayIndex++;


            }
        }

        // the number of vertices with a color
        int startRandomOrderHere = orderArrayIndex;

        // fill the rest of the order array with the vertices that don't have a color yet
        for (int i = 0; i < colourArray.length; i++){
            if (colourArray[i] == 0){
                orderArray[orderArrayIndex] = i;
                orderArrayIndex++;
            }
        }

        // order of vertices
        int [] currentOrderArray = new int[vertices];
        // color array
        int [] solutionArray = new int[vertices];
        // order of vertices for best coloring
        int [] bestOrderArray = new int[vertices];
        // order of colors for the best coloring
        int [] bestSolutionArray = new int[vertices];
        int amountOfColors;
        int minimumAmountOfColors = vertices;

        // try a lot of random order to find the best solution
        for (int i = 0; i < 1000; i++){
            currentOrderArray = createRandomOrder(orderArray, startRandomOrderHere);
            solutionArray = greedyColor(currentOrderArray, startRandomOrderHere, result);
            amountOfColors = maxValue(solutionArray);
            if (amountOfColors < minimumAmountOfColors){
                minimumAmountOfColors = amountOfColors;
                System.arraycopy(currentOrderArray, 0, bestOrderArray, 0, bestOrderArray.length);
                System.arraycopy(solutionArray, 0, bestSolutionArray, 0, bestSolutionArray.length);
                printArray(bestOrderArray);
                printArray(bestSolutionArray);
            }
        }


        // print the hint
        JLabel hintLabel = new JLabel("Use color " + bestSolutionArray[startRandomOrderHere] + " for vertex " +  bestOrderArray[startRandomOrderHere]);
        JOptionPane.showMessageDialog(null, hintLabel, "Cheers!", 3);

    }

    
    /** 
     * @param arr
     */
    // print an array
    public void printArray(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            System.out.print(", ");
        }
        System.out.println();
    }

    
    /** 
     * @param orderArray
     * @param startRandomOrderHere
     * @return int[]
     */
    // create an array with the vertices that don't have a color yet in a random order
    public int[] createRandomOrder(int[] orderArray, int startRandomOrderHere){
        int [] newArray = new int[vertices];
        System.arraycopy(orderArray, 0, newArray, 0, newArray.length); 
        for (int i = startRandomOrderHere; i < newArray.length; i++){
            int j = (int) (Math.random() * (vertices - startRandomOrderHere) + startRandomOrderHere);
            // swap i and j
            int temp = newArray[i];
            newArray[i] = newArray[j];
            newArray[j] = temp;
        }

        return newArray;
    }  

    
    /** 
     * @param currentOrderArray
     * @param startRandomOrderHere
     * @param result
     * @return int[]
     */
    //method to color the graph greedily and to obtain the upper bound for the chromatic number that way
    public int[] greedyColor(int[] currentOrderArray, int startRandomOrderHere , int[] result) {
       int[] solution = new int[vertices];
        
        // set the values of solution to the color of the vertices that are already colored
        for (int i = 0; i < startRandomOrderHere; i++){
            solution[i] = result[i];
        }

        // Set all values of solution to -1 if the vertex is not colored yet
        
        for (int i = startRandomOrderHere; i < vertices; i++){
            solution[i] = -1;
        }


        // create a new temporary array that stores which colors are available for a
        // vertex
        boolean safeColor[] = new boolean[vertices];

        

        // Assign colors to remaining V-1 vertices
        for (int u = startRandomOrderHere ; u < vertices; u++) {

            // Set all values of safeColor to true
            for (int i = 0; i < vertices; i++) {
                safeColor[i] = true;
            }
            // check if two vertices are adjacent and if so, set safeColor to false
            for (int j = 0; j < u; j++) {
                if (adjacencyMatrix[currentOrderArray[u]] [currentOrderArray[j]] == 1) {
                    safeColor[solution[j]] = false;  
                }
            }
            
            // Find the first color that is available
            int color;
            for (color = 1; color < vertices; color++) {
                if (safeColor[color])
                    break;
            }

            // set the color for the vertices to the assigned color
            solution[u] = color;

        }
    
        return solution;

    }

    
    /** 
     * @param array[]
     * @return int
     */
    // get the number of colors used
    public static int maxValue(int array[]){
        int max = Arrays.stream(array).max().getAsInt();
        return max;
    }
}
