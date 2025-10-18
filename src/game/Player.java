package game;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Player is the class which defines the controllable player present in the SugarDash game.
 * It extends <a href="#{@link}">{@link Polygon}</a>. It is a white square which upon the pressing
 * of w, space, or the up arrow, jumps and rotates 90 degrees clockwise in the air
 * in the same manner as the geometry dash cube.
 * @author Augniv Mitra
 * @author James Hui
 * @version %I% %G%
 */
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
     * Player is a <a href="#{@link}">{@link Polygon}</a> square with a side length
     * of <code>side_length</code> placed initially at <code>startPosition</code>.
     * @param startPosition a point where <code>Player</code> is placed initially.
     * @param side_length the length of the side of the square polygon, in pixels.
     */
    public Player(Point startPosition, int side_length){
        super( new Point[]{ new Point(0,0), new Point(side_length, 0), new Point(side_length, side_length), new Point(0, side_length)}, startPosition, 0.0);
        this.side_length = side_length;
    }

    /**
     * Moves the position of this element along the y-axis, the amount of
     * movement being based on a gravity constant and vertical velocity to 
     * simulate the acceleration of gravity. While the player is jumping,
     * rotates the cube. Once the cube falls back down to <code>floorY</code>,
     * set rotation to 90 degrees clockwise and set vertical velocity to zero.
     * 
     * @param floorY the y-value of the floor where the player must land,
     * in terms of pixels from the top of the canvas
     */
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

    /**
     * sets the rotation of the Player object to the parameter
     * @param theta the new degree of rotation
     */
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
     * Draws player on the canvas. 
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
        brush.setColor(new Color(255, 255, 255));

        // Draw Shape 
        brush.fillPolygon(xs, ys, points.length);

    }

    
}


