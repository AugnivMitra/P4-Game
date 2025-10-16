package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

*/
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;




class SugarDash extends Game {


	/** Player Object */
    private Player player;
    /** Polygon for Floor */
    private Polygon floor;
    /** Obstacle object */
    private Obstacle obstacle;
    /** Stopwatch for tracking game time */
    private Stopwatch gameTimer;

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

    // Game State Variables
    private int score = 0;
	private int moveMultiplier = 0;
    private static int highScore = 0;
    private static double bestTimeNano = 0;
    private String finalTimeFormatted;
    private String bestTimeFormatted = "00:00:000";
    private boolean isGameOver = false;



	public class Stopwatch {

		/** Start time of stopwatch in nanoseconds */
		private double startTime;

		/** Flag to tell the timer is running */
		private boolean isRunning;

		/** Elasped Time */
		private double elapsedTime;

		/** Saved Times */
		private static ArrayList<Double> timeArr; 


		/** Constructor */
		public Stopwatch(){
			startTime = 0;
			isRunning = false;
			timeArr = new ArrayList<Double>();

		}

		/** Start Stopwatch */
		public void start(){
			startTime = System.nanoTime();
			isRunning = true;
		}

		/** Stop Stopwatch */
		public void stop(){
			if (isRunning){
				elapsedTime = System.nanoTime() - startTime;
				timeArr.add(elapsedTime);
				isRunning = false;
			}
		}

		/** Reset Time - stop timer, save time, reset time*/
		public void reset(){
			elapsedTime = 0;
			startTime = 0;
			isRunning = false;
		}

		/** Return ElapsedTime in Nanoseconds*/
		public double getElapsedTimeNano(){
			if (isRunning){
				return System.nanoTime() - startTime;
			}
			return elapsedTime;
		}

		/** Get Formatted Time for String */
		public String getFormattedTime(double totalTimeNano){
			
			long totalMillis = (long) (totalTimeNano/1000000);

			long minutes = (totalMillis / 1000) / 60;
			long seconds = (totalMillis / 1000) % 60;
			long millis = totalMillis % 1000;

			return String.format("%02d:%02d:%03d", minutes, seconds, millis);
		}

		/** Get Longest Time */
		public static double getLongestTime(){
			if (timeArr.isEmpty()){
				return 0.0;
			}
			return Collections.max(timeArr);
		}

	}

	public SugarDash() {
		super("Sugar Dash!",800,600);
    	
		player = new Player(new Point(PLAYER_X_OFFSET, FLOOR_Y-PLAYER_SIDE_LENGTH), PLAYER_SIDE_LENGTH);
		obstacle = new Obstacle(new Point(800, FLOOR_Y - COFFEE_HEIGHT), COFFEE_TOP, COFFEE_BOTTOM, COFFEE_HEIGHT);
		gameTimer = new Stopwatch();

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

	/** Reset Game to initial state*/
	private void resetGame() {
		player.position.x = PLAYER_X_OFFSET;
        player.position.y = FLOOR_Y - PLAYER_SIDE_LENGTH;
		player.setRotation(0);
		obstacle.setHasBeenScored(false);
		obstacle.position.x = 800;

		score = 0;
		moveMultiplier = 0;

		gameTimer.reset();
		gameTimer.start();

		isGameOver = false;

		initializeProps();

	}

	private void gameOver(){
		isGameOver = true;
		gameTimer.stop();

		bestTimeNano = Stopwatch.getLongestTime();
		bestTimeFormatted = gameTimer.getFormattedTime(bestTimeNano);

		if (score > highScore){
			highScore = score;
		}


		finalTimeFormatted = gameTimer.getFormattedTime(gameTimer.getElapsedTimeNano());
	}
  
	@Override
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


			if(player.collides(obstacle)){
				gameOver();
			}

            if (obstacle.getXPos() < PLAYER_X_OFFSET && !obstacle.getHasBeenScored()) {
                score++;
                obstacle.setHasBeenScored(true);
            }
			
			brush.setColor(Color.WHITE);
            brush.setFont(new Font("Monospaced", Font.BOLD, 20));
            brush.drawString("Score: " + score, width - 250, 30);
            brush.drawString("Time: " + gameTimer.getFormattedTime(gameTimer.getElapsedTimeNano()), width - 250, 55);

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