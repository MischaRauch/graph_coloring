package ui;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.FontMetrics;

public class VertexButton extends JButton {

    private static final long serialVersionUID = 1L;
    private boolean selected = false;
    public Color color = Color.WHITE;
    private int borderSize = 1;
    private int radius;
    private int diameter;

    public VertexButton(String name, int radius) {
        super(name);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        this.radius = radius;
        diameter = 2 * radius;
    }

    
    /** 
     * @param centerX
     * @param centerY
     */
    public void setCenter(int centerX, int centerY) {
        setBounds(centerX - radius, centerY - radius, diameter, diameter);
    }

    
    /** 
     * @param selected
     */
    // change the appearence to indicate a vertex is selected
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    
    /** 
     * @param color
     */
    public void setVertexColor(Color color) {
        this.color = color;
        repaint();
    }
    
    /** 
     * @return int
     */
    public int getRadius(){
        //me
        return radius;
    }
    
    /** 
     * @param x
     * @param y
     * @return boolean
     */
    @Override
    public boolean contains(int x, int y) {
        return Point2D.distance(x, y, radius, radius) < radius;
    }

    @Override
    public void paintComponent(Graphics g) {

        if (selected) {
            g.setColor(Color.BLUE);
            borderSize = 3;
        } else {
            g.setColor(Color.BLACK);
            borderSize = 1;
        }

        g.fillOval(0, 0, diameter, diameter);

        g.setColor(color);
        g.fillOval(borderSize, borderSize, diameter - borderSize * 2, diameter - borderSize * 2);

        g.setColor(Color.BLACK);
        g.setFont(getFont());
        FontMetrics metrics = g.getFontMetrics(getFont());
        int stringWidth = metrics.stringWidth(getText());
        int stringHeight = metrics.getHeight();
        g.drawString(getText(), radius - stringWidth / 2, radius + stringHeight / 4);
    }
}
