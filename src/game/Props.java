package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Props is the class which defines the obstacles present in the SugarDash game.
 * It extends <a href="#{@link}">{@link Polygon}</a> and implements
 * <a href="#{@link}">{@link Moveable_x}</a>. Props can either be sugar packets drawn
 * in the floor or stars drawn in the upper side of the canvas, effectively the sky.
 * These props' positions are stored as arrays of coordinates which are used to make prop 
 * objects on the fly. Props loop moving from right to left. 
 * @author Augniv Mitra
 * @author James Hui
 * @version %I% %G%
 */
public class Props extends Polygon implements Moveable_x{
    
    static final double speed = 1;

    static final int[][] STAR_COORDS = {{10, 12, 19, 13, 15, 10, 5, 7, 1, 8, 10},
                                        {20, 13, 13, 9, 2, 6, 2, 9, 13, 13, 20}};

    static final int[][] SUGAR_COORDS = {{0,0,80,80},{0,50,50,0}};

    private String type;
    private Color color;
    private int colorCycleCounter = 0;

    /**
     * The prop constructor creates props with a specified string type determining
     * their shape and where they are drawn. 
     * They will be placed initially at <code>startPosition</code>.
     * @param type a String indicating whether the prop is a star or a sugar packet
     * @param startPosition a point where <code>Props</code> are placed initially.
     */
    public Props(String type, Point startPosition){
        super((type.equals("star") ? createShapeFromCoords(STAR_COORDS) : createShapeFromCoords(SUGAR_COORDS)), startPosition, 0.0);
        this.type = type;

        if (type.equals("star")){
            this.color = Color.YELLOW;
        } else {
            this.color = new Color (255, 179, 222);
        }
    }

    /**
     * Generates an array of Points which represent the 2d integer array of individual
     * coordinates passed into the parameter.
     * @param coords 2d array consisting of two arrays, x and y
     * @return an array containing the point objects representing
     * the input x- and y- coordinates. 
     */
    private static Point[] createShapeFromCoords(int[][] coords) {
        Point[] points = new Point[coords[0].length];
        for (int i = 0; i < coords[0].length; i++) {
            points[i] = new Point(coords[0][i], coords[1][i]);
        }
        return points;
    }

    @Override
    public void update(int score){

        if (this.type.equals("star")){
            this.position.x -=speed;
        } else {
            this.position.x -=speed/4;
        }

        if (this.position.x < -80){
            this.position.x = 800;
        }

        if(type.equals("star")){
            updateStarColor();
        }
    }
    /**
     * Changes stars' color in a looping manner,
     * dimming and brightening them.
     */
    private void updateStarColor() {

        colorCycleCounter++;

        if (colorCycleCounter % 5 == 0) {
            int brightness = 155 + (colorCycleCounter % 20) * 5; 
            this.color = new Color(255, 255, brightness);
        }

    }

     /** 
     * Draws all props on the canvas, being either a star or a sugar packet based
     * on the <code>type</code> String. 
     * @param brush the brush that is being used to draw props on the game's canvas
     * */
    public void paint(Graphics brush){

        // Get current verticies
        Point[] points = this.getPoints();
        int[] xs = new int[points.length];
        int[] ys = new int[points.length];

        // Update points
        for (int i = 0; i < points.length; i++) {
            xs[i] = (int) points[i].x;
            ys[i] = (int) points[i].y;
        }

        // Set Brush Color
        brush.setColor(this.color);
        // Draw Shape 
        brush.fillPolygon(xs, ys, points.length);

        // Draw Text Sugar on Packet
        if (type.equals("sugar")) {
            brush.setColor(Color.RED);
            brush.setFont(new Font("Arial", Font.BOLD, 18));
            int textX = (int) (this.position.x + 8);
            int textY = (int) (this.position.y + 32);
            brush.drawString("SUGAR", textX, textY);
        }

    }    
}
