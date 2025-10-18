package game;



import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.function.DoubleSupplier;


/**
 * SugarDash is the class containing the game logic, putting all other classes to use.
 * It extends <a href="#{@link}">{@link Game}</a>. The game plays as follows.
 * <p>
 * The player is placed on the left side of the canvas, and coffee cup obstacles will come from
 * the right side to the left. The player must jump over the coffee cups with W, space, or the up arrow
 * or else the game will be ended.
 * As time progresses, the obstacles move faster and faster.
 * Every time the player clears an obstacle, there is a 10% chance that an invulnerability powerup appears
 * which makes them unable to lose the game to obstacles for five seconds. When the game ends, the player
 * is shown both the amount of time they survived and their time record for survival, and are given the option
 * to reset. The game also has stars that are randomly drawn in the sky, with the stars variably dimming and
 * brightening, and sugar packets moving in the ground from right to left.
 * </p>
 * @author Augniv Mitra
 * @author James Hui
 * @version %I% %G%
 */
class SugarDash extends Game {


	/** Player Object */
    private Player player;
    /** Polygon for Floor */
    private Polygon floor;
    /** Obstacle object */
    private Obstacle obstacle;
    /** Stopwatch for tracking game time */
    private Stopwatch gameTimer;
	/** Invuln powerup Object */
	private Obstacle.InvulnerabilityPowerUp powerUp;

	/** ArrayList for Props */
	private ArrayList<Props> stars;
    private ArrayList<Props> sugarPackets;

	// Game Constants
    private static final int FLOOR_Y = 400;
    private static final int PLAYER_X_OFFSET = 100;
    private static final int PLAYER_SIDE_LENGTH = 40;
    private static final int COFFEE_HEIGHT = 50;
    private static final int COFFEE_BOTTOM = 30;
    private static final int COFFEE_TOP = 50;
	private static final int INVULN_MS = 5000;
	private static final int POWERUP_SIDE_LENGTH = 20;

    // Game State Variables
    private int score = 0;
	private int moveMultiplier = 0;
    private static int highScore = 0;
    private static double bestTimeNano = 0;
    private String finalTimeFormatted;
    private String bestTimeFormatted = "00:00:000";
    private boolean isGameOver = false;
	private boolean isInvulnerable = false;
	private static double invulnStartTime = 0;


	/**
     * Stopwatch is a class defining a stopwatch for keeping track of game time.
     * It is an inner class of <a href="#{@link}">{@link SugarDash}</a>.
	 * It is used to track the time elapsed between its starting and stopping,
	 * and stores previous times.
     * @author Augniv Mitra
     * @author James Hui
     * @version %I% %G%
     */
	public class Stopwatch {

		/** Start time of stopwatch in nanoseconds */
		private double startTime;

		/** Flag to tell the timer is running */
		private boolean isRunning;

		/** Elasped Time */
		private double elapsedTime;

		/** Saved Times */
		private static ArrayList<Double> timeArr; 

		public DoubleSupplier getElapsedTimeNano;

		/**
		 * Initializes ArrayList of times, and uses lambda expression for
		 * getting the amount of time elapsed between start and end.
		 */
		public Stopwatch(){
			startTime = 0;
			isRunning = false;
			timeArr = new ArrayList<Double>();
			getElapsedTimeNano = () -> {
				if (isRunning){
					return System.nanoTime() - startTime;
				}
				return elapsedTime;
		};

		}

		/** Starts the stopwatch tracking time elapsed. */
		public void start(){
			startTime = System.nanoTime();
			isRunning = true;
		}

		/** Stops the stopwatch from counting more time. */
		public void stop(){
			if (isRunning){
				elapsedTime = System.nanoTime() - startTime;
				timeArr.add(elapsedTime);
				isRunning = false;
			}
		}

		/**
		 * Resets the game timer
		 */
		public void reset(){
			elapsedTime = 0;
			startTime = 0;
			isRunning = false;
		}

		/**
		 * Formats the input time into human-readable format
		 * @param totalTimeNano double representation of the system time in nanoseconds
		 * @return String representation of <code>totalTimeNano</code> in format minutes:seconds:milliseconds
		 */
		public String getFormattedTime(double totalTimeNano){
			
			long totalMillis = (long) (totalTimeNano/1000000);

			long minutes = (totalMillis / 1000) / 60;
			long seconds = (totalMillis / 1000) % 60;
			long millis = totalMillis % 1000;

			return String.format("%02d:%02d:%03d", minutes, seconds, millis);
		}

		/**
		 * gets the longest time the player has survived
		 * @return double representation of the longest time that the player has survived
		 */
		public static double getLongestTime(){
			if (timeArr.isEmpty()){
				return 0.0;
			}
			return Collections.max(timeArr);
		}

	}

	/**
	 * Initializes all objects necessary for the gameplay. This includes the player as Player,
	 * the obstacles as Obstacle, the game's timer as Stopwatch, and the obstacle as 
	 * InvulnerabilityPowerup. It also creates the polygon representation of the floor to
	 * be drawn in paint. It also calls <code>{initializeProps}<code> to
	 * create all props which will be drawn in paint.
	 */
	public SugarDash() {
		super("Sugar Dash!",800,600);
    	
		player = new Player(new Point(PLAYER_X_OFFSET, FLOOR_Y-PLAYER_SIDE_LENGTH), PLAYER_SIDE_LENGTH);
		obstacle = new Obstacle(new Point(800, FLOOR_Y - COFFEE_HEIGHT), COFFEE_TOP, COFFEE_BOTTOM, COFFEE_HEIGHT);
		gameTimer = new Stopwatch();
		powerUp = obstacle.new InvulnerabilityPowerUp(new Point(800, FLOOR_Y - POWERUP_SIDE_LENGTH), POWERUP_SIDE_LENGTH);

		Point[] floorShape = { new Point(0,0), new Point(width, 0), new Point(width , height - FLOOR_Y), new Point(0, height - FLOOR_Y)};
		floor = new Polygon(floorShape, new Point(0,FLOOR_Y), 0);

		initializeProps();

		gameTimer.start();

		this.setFocusable(true);
		this.requestFocus();

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyChar() == 'w' || e.getKeyChar() == 'W' || e.getKeyCode() == KeyEvent.VK_SPACE){
					player.jump();
				} else if ((e.getKeyCode() == 'r' || e.getKeyCode() == 'R') && isGameOver){
					resetGame();
				}
			}
		});

		
  	}

	/**
	 * Creates & stores 20 stars and 5 props in their respective <Code>ArrayLists</code>
	 * with random x-positions and bounded random y-positions.
	 */
	private void initializeProps() {
        stars = new ArrayList<>();
        sugarPackets = new ArrayList<>();
        Random rand = new Random();

        // Create 10 stars with random positions in the sky
        for (int i = 0; i < 20; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(300);
            stars.add(new Props("star", new Point(x, y)));
        }

        // Create 5 sugar packets with random positions on the floor
        for (int i = 0; i < 5; i++) {
            int x = rand.nextInt(width);
            int y = FLOOR_Y + rand.nextInt(height - FLOOR_Y - 50);
            sugarPackets.add(new Props("sugar", new Point(x, y)));
        }
    }

	/** Resets Game to initial state, overwriting gamestate variables.*/
	private void resetGame() {
		player.position.x = PLAYER_X_OFFSET;
        player.position.y = FLOOR_Y - PLAYER_SIDE_LENGTH;
		player.setRotation(0);
		obstacle.setHasBeenScored(false);
		obstacle.position.x = 800;
		powerUp.setVisibility(false);
		isInvulnerable = false;

		score = 0;
		moveMultiplier = 0;

		gameTimer.reset();
		gameTimer.start();

		isGameOver = false;

		initializeProps();

	}

	/** Handles logic for when the game ends.
	 * Displays the user's current run time, their best time, and prompts
	 * them to restart the game.
	  */
	private void gameOver(){
		isGameOver = true;
		gameTimer.stop();

		bestTimeNano = Stopwatch.getLongestTime();
		bestTimeFormatted = gameTimer.getFormattedTime(bestTimeNano);

		if (score > highScore){
			highScore = score;
		}


		finalTimeFormatted = gameTimer.getFormattedTime(gameTimer.getElapsedTimeNano.getAsDouble());
	}
  
	
	@Override
	/**
	 * Handles game logic for drawing graphics onto canvas, painting the floor,
	 * player, both prop types, obstacles, powerups, and game-over text. It also
	 * checks for collision between the player and obstacles or powerups, and handles
	 * their respective logic by modifying game state variables.
	 * @param brush Used to paint on the game canvas
	 */
	public void paint(Graphics brush) {
		if(!isGameOver){
			brush.setColor(new Color (20,20,40));
			brush.fillRect(0,0,width,height);
			
			paintFloor(brush, floor, new Color(118,92,72));

			for (Props star : stars) {
                star.update(0);
                star.paint(brush);
            }

			for (Props packet : sugarPackets) {
                packet.update(0);
                packet.paint(brush);
            }
			
			player.update(FLOOR_Y);
			player.paint(brush);

			obstacle.update(moveMultiplier);
			moveMultiplier += 1;
			obstacle.paint(brush);

			powerUp.update(moveMultiplier);
			powerUp.paint(brush);

			if(!isInvulnerable && player.collides(obstacle)){
				gameOver();
			}

            if (obstacle.getXPos() < PLAYER_X_OFFSET && !obstacle.getHasBeenScored()) {
                score++;
                obstacle.setHasBeenScored(true);
				if(Math.random() < 0.1){
					powerUp.moveToStart();
					powerUp.setVisibility(true);
					powerUp.paint(brush);
					// System.out.println("Random check");
				}
            }

			

			// powerup collision | getVisibility returns true if visible
			if(player.collides(powerUp) && powerUp.getVisibility()){
				isInvulnerable = true;
				invulnStartTime = System.currentTimeMillis();
				powerUp.setVisibility(false);
				powerUp.paint(brush);
			}

			// display to user that they are invulnerable
			if(isInvulnerable){
				brush.setColor(Color.WHITE);
				brush.setFont(new Font("Monospaced", Font.PLAIN, 20));
				brush.drawString("Invulnerable!", width / 2 - 50, height / 2 - 100);
			}

			// when time runs out, change state back
			if(isInvulnerable && System.currentTimeMillis() > invulnStartTime + INVULN_MS){
				isInvulnerable = false;

			}

			
			brush.setColor(Color.WHITE);
            brush.setFont(new Font("Monospaced", Font.BOLD, 20));
            brush.drawString("Score: " + score, width - 250, 30);
            brush.drawString("Time: " + gameTimer.getFormattedTime(gameTimer.getElapsedTimeNano.getAsDouble()), width - 250, 55);

		}
		else{
            brush.setColor(new Color(20, 20, 40));
            brush.fillRect(0, 0, width, height);
            
            brush.setColor(Color.WHITE);
            brush.setFont(new Font("Monospaced", Font.BOLD, 50));
            brush.drawString("Game Over!", width / 2 - 150, height / 2 - 100);

            brush.setFont(new Font("Monospaced", Font.PLAIN, 24));
            brush.drawString("Final Score: " + score, width / 2 - 150, height / 2 - 20);
            brush.drawString("High Score:  " + highScore, width / 2 - 150, height / 2 + 10);
            brush.drawString("Final Time:  " + finalTimeFormatted, width / 2 - 150, height / 2 + 50);
            // Get best time directly from the timer's static methods
            brush.drawString("Best Time:   " + bestTimeFormatted, width / 2 - 150, height / 2 + 80);

            brush.setFont(new Font("Monospaced", Font.BOLD, 30));
            brush.drawString("Press 'R' to Restart", width / 2 - 180, height / 2 + 150);
		}

  	}
	
	/**
	 * Paints the game's ground onto the canvas, given a brush,
	 * a Polygon to paint, and the color of the polygon
	 * @param brush used to paint on the game's canvas
	 * @param p the polygon to be put as the floor, should be a rectangle
	 * @param c the color that the floor should be
	 */
    public void paintFloor(Graphics brush, Polygon p, Color c){
        // Get current verticies
        Point[] points = p.getPoints();
        int[] xs = new int[points.length];
        int[] ys = new int[points.length];
        // Update points
        for (int i = 0; i < points.length; i++) {
            xs[i] = (int) points[i].x;
            ys[i] = (int) points[i].y;
        }
        // Set Brush Color
        brush.setColor(c);
        // Draw Shape 
        brush.fillPolygon(xs, ys, points.length);

    }

  
	public static void main (String[] args) {
   		SugarDash a = new SugarDash();
		a.repaint();
  }
}