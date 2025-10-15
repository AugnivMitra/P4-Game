package game;
import java.awt.Color;
import java.awt.Graphics;

public class Obstacle extends Polygon implements Moveable_x {
    static int top_size;
    public Obstacle(Point startPosition, int top_size, int bottom_size, int height){
        super(new Point[]{
            new Point(0, 0),
            new Point(top_size, 0),
            new Point(top_size - (top_size - bottom_size) / 2.0, height),
            new Point((top_size - bottom_size) / 2.0, height)
        }, startPosition, 0.0);
        this.top_size = top_size;
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
        brush.setColor(new Color(255, 255, 255));
        // Draw Shape 
        brush.fillPolygon(xs, ys, points.length);

    }
    
    public void update(int score){
        if(score < max_score_speed){
            position.x -= (3 + score/500);
        }
        else{
            position.x -= (3 + max_score_speed/500);
        }
        if(position.x <= -top_size){
            position.x = 800;
        }
    }
}
