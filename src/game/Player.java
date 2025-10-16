package game;
import java.awt.Color;
import java.awt.Graphics;

public class Player extends Polygon{
    
    /** Falling Gravity. */
    private static final double GRAVITY = 0.4;

    /** Initial Jump Velocity */
    private static final double JUMPVEL = -14.0;

    /** Number of Frames during Jump */
    private static final double JUMPFRAMES = (-2 * JUMPVEL) / GRAVITY;

    /** Theta per frame for a jump */
    private static final double THETAPERFRAME = 90.0 / JUMPFRAMES;

    /** Save Theta for rotation*/
    private double initial_theta = 0;

    /** Player Velocity */
    private double playerVel= 0;

    /** Determine if Player is on the ground */
    private boolean onGround = true;

    private int side_length;

    /**
     * Player Constructor:
     * Inherits Polygon Constructor, which includes the polygon vertics
     * The polygon has a point as well which sets the left bottom corner
     * Side Length can also be set
     */

    public Player(Point startPosition, int side_length){
        super( new Point[]{ new Point(0,0), new Point(side_length, 0), new Point(side_length, side_length), new Point(0, side_length)}, startPosition, 0.0);
        this.side_length = side_length;
    }

    public void update(int floorY){
        // Apply gravity to velocity;
        playerVel += GRAVITY;

        // Update Player's y value
        position.y += playerVel;

        if (!onGround){
            this.rotation += THETAPERFRAME;
        }

        // If touches the ground, make sure velocity is 0
        if(position.y >= floorY - side_length){
            position.y = floorY - side_length;
            playerVel = 0;
            
            if (!onGround) {

                onGround = true;

                //
                this.rotation = (initial_theta + 90) % 360;
            }
        }
    }

    public void setRotation (double theta){
        this.rotation = theta;
    }

    /**
     * Activates Player Jumping only when the player is on the ground
     * Player also rotates
     */
    public void jump(){
        if(onGround){
            playerVel = JUMPVEL;
            onGround = false;

            // Reassign theta to keep track of rotation
            initial_theta = this.rotation;
        }

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

    
}


