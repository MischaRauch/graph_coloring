package ui;

//https://github.com/svishrut93/Force-Directed-Graph-Drawing/blob/master/Graph%202.py
import graph.*;
import java.awt.geom.AffineTransform;

public class GraphLayout {
    // mass
    public final double alpha = 1.0;
    public final double beta = .0001;
    public final double k = 1.0;
    // damping
    public final double eta = .99;
    public final double delta_t = .01;
    public final Graph g;
    // positions
    public double[][] x;
    // velocities
    private double[][] v;
    // working points that are transformed by zoom and drag
    public double[] pts;
    // scaled and Integer popsitions for plotting
    public int[][] pltP;

    public GraphLayout(Graph G) {
        //initializing some arrays 
        g = G;
        x = new double[g.vertices][];
        v = new double[g.vertices][];
        pltP = new int[g.vertices][];
        pts = new double[g.vertices*2];
        for (int j = 0; j < g.vertices; j++) {
            x[j] = new double[] { Math.random(), Math.random() };
            v[j] = new double[] { 0, 0 };
        }
    }

    /**
     * @param xi
     * @param xj
     * @param dij
     * @return double[]
     * Vector version of the Hooke force
     */
    double[] Hooke_force(double[] xi, double[] xj, double dij) {
        double dx = xj[0] - xi[0];
        double dy = xj[1] - xi[1];
        double ds = Math.sqrt(dx * dx + dy * dy);
        double dl = ds - dij;
        double cc = k * dl / ds;
        return new double[] { cc * dx, cc * dy };
    }

    /**
     * @param xi
     * @param xj
     * @return double[]
     * Vector version of the columb force
     */
    double[] Coulomb_force(double[] xi, double[] xj) {
        double dx = xj[0] - xi[0];
        double dy = xj[1] - xi[1];
        double ds2 = dx * dx + dy * dy;
        double ds = Math.sqrt(ds2);
        double ds3 = ds2 * ds;
        double cc;
        if (ds3 == 0.0) {
            cc = 0;
        } else
            cc = beta / (ds2 * ds);
        return new double[] { -cc * dx, -cc * dy };
    }

    /**
     * @param n
     * Calculates the forces and velocities for n Timesteps 
     */
    void move(int n) {
        for (int m = 0; m < n; m++) {

            for (int i = 0; i < g.vertices; i++) {
                //Slightly attract all vertices to the point (0.5,0.5) so that they are more resonably placed in the frame 
                //Especially useful when the graph is made up of several not connected subgraphs
                double[] Fatr = Hooke_force(x[i],new double[] {0.5,0.5},1);
                double Fx = 0.2*Fatr[0];
                double Fy = 0.2*Fatr[1];
                
                
                
                for (int j = 0; j < g.vertices; j++) {
                    //Interaction between vertecies i and j 
                    double dij = g.adjacencyMatrix[i][j];
                    double[] Fij;
                    if (dij == 0.0)
                        Fij = Coulomb_force(x[i], x[j]);
                    else
                        Fij = Hooke_force(x[i], x[j], dij / 10);
                    Fx += Fij[0];
                    Fy += Fij[1];
                }

                v[i][0] = (v[i][0] + alpha * Fx * delta_t) * eta;
                v[i][1] = (v[i][1] + alpha * Fy * delta_t) * eta;

            }
            for (int i = 0; i < g.vertices; i++) {
                x[i][0] += v[i][0] * delta_t;
                x[i][1] += v[i][1] * delta_t;
            }
        }
        getPositiveZeroAlignedScaledPos(300);
    }

    
    /** 
     * @param defaultScale
     * Scales the point to make sense in the swing window context 
     */
    private void getPositiveZeroAlignedScaledPos(int defaultScale) {
        double maxX=-Double.MAX_VALUE;
        double maxY=-Double.MAX_VALUE;
        double minX=Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        int padding = 50;
        
        
        for (int i = 0; i < g.vertices; i++) {
            if (x[i][0] > maxX) {
                maxX = x[i][0];
            } else if (x[i][0] < minX) {
                minX = x[i][0];
            }
            if (x[i][1] > maxY) {
                maxY = x[i][1];
            } else if (x[i][1] < minY) {
                minY = x[i][1];
            }
        }
        double xRange = maxX - minX;
        double yRange = maxY - minY;
        double scaling = (1/Math.max(xRange, yRange))*defaultScale;

        
        for (int i = 0; i < g.vertices; i++) {
            pltP[i] = new int[] { (int) (padding+(x[i][0] - minX) * scaling), (int) (padding+(x[i][1] - minY) * scaling) };
            pts[(i * 2)] = (x[i][0] - minX) * scaling;
            pts[(i * 2) + 1] = ((x[i][1] - minY) * scaling);
        }
    }

    /**
     * @param AfffineTransform
     * @return int[][]
     * Transforms and returns teh points position after zooming
     */
    int[][] getPlotPoints(AffineTransform af) {
        //System.out.println("redraw with af");
        for (int i = 0; i < g.vertices; i++) {
            
            pts[(i * 2)] = pltP[i][0];
            pts[(i * 2) + 1] = pltP[i][1];
        }
        af.transform(pts, 0, pts, 0, g.vertices);
        for (int i = 0; i < g.vertices; i++) {
            pltP[i] = new int[] { (int)(pts[(i*2)]),(int) pts[(i*2)+1]};
        }
        return pltP;
    }
}
