package game;
import java.awt.Color;
import java.awt.Graphics;


public class Obstacle extends Polygon implements Moveable_x {

    public class InvulnerabilityPowerUp extends Polygon implements Moveable_x{

        private boolean visibility = false;
        private int side_length;

        /**
         * InvulnerabilityPowerUp Constructor:
         * Inherits Polygon and Moveable_X Constructor, is an 
         * inner class of Obstacle. 
         * Takes in a starting point & side length
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

        /** Draws powerup on the canvas. */
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

        /** Returns true if the powerup is visible, false otherwise. */
        public boolean getVisibility(){
            return visibility;
        }

        /** Sets the visibility of the powerup to the parameter. */
        public void setVisibility(boolean vis){
            this.visibility = vis;
        }

        /** Moves the x-position of the powerup back to the front. */
        public void moveToStart(){
            position.x = 800;
        }

        
    }

    static int top_size;

    private double baseSpeed = 3.0;

    private boolean hasBeenScored;

    /**
     * Obstacle Constructor:
     * Inherits Polygon and Moveable_X Constructor. 
     * Creates an upside-down isosceles trapezoidal polygon
     * based on the parameters.
     * @param startPosition: Point
     * @param top_size: int
     * @param bottom_size: int
     * @param height: int
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
     * Draw obstacle on the canvas.
     */
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
    
    /** Returns the x-position of the obstacle */
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

    /** Returns if the obstacle has been passed by the player or not. */
    public boolean getHasBeenScored (){
        return this.hasBeenScored;
    }

    /** Sets the status of the obstacle being passed to the parameter. */
    public void setHasBeenScored(boolean status) {
        this.hasBeenScored = status;
    }

    
}
