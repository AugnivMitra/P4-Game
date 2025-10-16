package game;
import java.awt.Color;
import java.awt.Graphics;

public class Obstacle extends Polygon implements Moveable_x {


    static int top_size;

    private double baseSpeed = 3.0;

    private boolean hasBeenScored;

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
     * Paint: draw the player on the canvas
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

    public double getXPos(){
        return position.x;
    }
    
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

    public boolean getHasBeenScored (){
        return this.hasBeenScored;
    }

    public void setHasBeenScored(boolean status) {
        this.hasBeenScored = status;
    }
}
