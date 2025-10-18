package game;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Obstacle is the class which defines the obstacles present in the SugarDash game.
 * It extends <a href="#{@link}">{@link Polygon}</a> and implements
 * <a href="#{@link}">{@link Moveable_x}</a>. It is a white, upside-down isosceles trapezoid.
 * If the player contacts it and is not invincible, the game is ended.
 * It loops across the canvas from right-to-left.
 * @author Augniv Mitra
 * @author James Hui
 * @version %I% %G%
 */
public class Obstacle extends Polygon implements Moveable_x {

    /**
     * InvulnerabilityPowerUp is the class which defines the power-ups present in the SugarDash game.
     * It is an inner class of <a href="#{@link}">{@link Obstacle}</a>
     * It extends <a href="#{@link}">{@link Polygon}</a> and implements
     * <a href="#{@link}">{@link Moveable_x}</a>. It is a yellow square.
     * When the player contacts it, they are granted 5 seconds of invulnerability.
     * It has a 10% chance of appearing for every obstacle passed.
     * @author Augniv Mitra
     * @author James Hui
     * @version %I% %G%
     */
    public class InvulnerabilityPowerUp extends Polygon implements Moveable_x{

        private boolean visibility = false;
        private int side_length;

        /**
         * InvulnerabilityPowerUp 
         * inherits Polygon and Moveable_X Constructor, and is an 
         * inner class of Obstacle. 
         * Takes in a starting point and side length, and creates a square polygon
         * at that starting point with that side length.
         * @param startPosition where the power-up is at first (before updates)
         * @param side_length specifies length of sides of powerup, in pixels
         */
        public InvulnerabilityPowerUp(Point startPosition, int side_length){
            super(new Point[]{
                new Point(0, 0),
                new Point(side_length,0),
                new Point(side_length, side_length),
                new Point(0, side_length),
            }, startPosition, 0);
            this.side_length = side_length;
        }

        
        @Override
        public void update(int score){
            if(visibility){
                if(score < max_move_speed){
                    position.x -= baseSpeed + (score / 500.0);
                }
                else{
                    position.x -= baseSpeed + (max_move_speed / 500.0);
                }
                if(position.x <= -side_length){
                    setVisibility(false);
                }
            }
        }

        /** 
         * Draws powerup on the canvas. 
         * @param brush the brush that is being used to draw on the game's canvas
         * */
        public void paint(Graphics brush){
            if(visibility){
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
                brush.setColor(Color.YELLOW);
                // Draw Shape 
                brush.fillPolygon(xs, ys, points.length);
            }

        }

        /** Returns visibility of the powerup. 
         * @return true if the powerup is visible, false otherwise
        */
        public boolean getVisibility(){
            return visibility;
        }

        /** 
         * Sets the visibility of the powerup to the parameter. 
         * @param vis true or false; visible or not
         * */
        public void setVisibility(boolean vis){
            this.visibility = vis;
        }

        /** 
         * Moves the x-position of the powerup back to the front of the canvas,
         * which is 800 pixels to the right. 
         */
        public void moveToStart(){
            position.x = 800;
        }

        
    }

    static int top_size;

    private double baseSpeed = 3.0;

    private boolean hasBeenScored;

    /**
     * Obstacle constructor, inheriting Polygon and Moveable_X Constructor. 
     * Takes in a starting point and size parameters, and creates an
     * isosceles trapezoidal polygon based on these paremeters at the specified
     * starting point.
     * @param startPosition: Starting position of the Obstacle
     * @param top_size: Size of the top base of the Obstacle, in pixels
     * @param bottom_size: Size of the bottom base of the Obstacle, in pixels
     * @param height: Height of the Obstacle, in pixels
     */
    public Obstacle(Point startPosition, int top_size, int bottom_size, int height){
        super(new Point[]{
            new Point(0, 0),
            new Point(top_size, 0),
            new Point(top_size - (top_size - bottom_size) / 2.0, height),
            new Point((top_size - bottom_size) / 2.0, height)
        }, startPosition, 0.0);
        this.top_size = top_size;
        this.hasBeenScored = false;
    }
     /** 
     * Draws powerup on the canvas. 
     * @param brush the brush that is being used to draw on the game's canvas
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
        brush.setColor(new Color(230, 230, 230));
        // Draw Shape 
        brush.fillPolygon(xs, ys, points.length);

    }
    
    /** Returns the x-position of the obstacle 
     * @return The x-position of the obstacle, in pixels
    */
    public double getXPos(){
        return position.x;
    }
    
    @Override
    public void update(int score){

        if(score < max_move_speed){
            position.x -= baseSpeed + (score / 500.0);
        }
        else{
            position.x -= baseSpeed + (max_move_speed / 500.0);
        }
        if(position.x <= -top_size){
            position.x = 800;
            this.hasBeenScored = false;
        }
    }

    /** Returns if the obstacle has been passed by the player or not. 
     * @return true if it has been scored, false if not
    */
    public boolean getHasBeenScored (){
        return this.hasBeenScored;
    }

    /** Sets the status of the obstacle being passed to the parameter. 
     * @param status the scoring status of the obstacle - true if scored, false if not
    */
    public void setHasBeenScored(boolean status) {
        this.hasBeenScored = status;
    }

    
}
