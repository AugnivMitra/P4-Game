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


class SugarDash extends Game {

	/** Player Object initialized  */
	private Player player;

	/** Polygon for Floor */
	private Polygon floor;

	private Obstacle obstacle;

	/** Floor Height */
	private static final int FLOOR_Y  = 500;
	/** Player X Offset */
	private static final int PLAYER_X_OFFSET = 100;
	/** Player Side Length */
	private static final int PLAYER_SIDE_LENGTH = 40;
	
	/** Coffee Height */
	private static final int COFFEE_HEIGHT = 50;
	/** Coffee Bottom */
	private static final int COFFEE_BOTTOM = 30;
	/** Coffee Height */
	private static final int COFFEE_TOP = 50;

	/** Score */
	private static int score = 0;
	/** Is Game Over */
	private static boolean isGameOver = false;
	public SugarDash() {
		super("Sugar Dash!",800,600);
    	
		player = new Player(new Point(PLAYER_X_OFFSET, FLOOR_Y-PLAYER_SIDE_LENGTH), PLAYER_SIDE_LENGTH);
		obstacle = new Obstacle(new Point(800, FLOOR_Y - COFFEE_HEIGHT), COFFEE_TOP, COFFEE_BOTTOM, COFFEE_HEIGHT);

		Point[] floorShape = { new Point(0,0), new Point(width, 0), new Point(width , height - FLOOR_Y), new Point(0, height - FLOOR_Y)};
		floor = new Polygon(floorShape, new Point(0,FLOOR_Y), 0);

		this.setFocusable(true);
		this.requestFocus();

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyChar() == 'w' || e.getKeyChar() == 'W' || e.getKeyCode() == KeyEvent.VK_SPACE){
					player.jump();
				}
			}
		});

		
  	}
  
	@Override
	public void paint(Graphics brush) {
		if(!isGameOver){
			brush.setColor(new Color (20,20,40));
			brush.fillRect(0,0,width,height);
			
			player.update(FLOOR_Y);
			player.paint(brush);

			obstacle.update(score);
			score += 1;
			obstacle.paint(brush);

			if(player.collides(obstacle)){
				isGameOver = true;
			}

			Paint(brush, floor, new Color(0,0,0));
		}
		else{
			brush.setColor(Color.WHITE);
			brush.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
			brush.drawString("Game Over!", 250, 250);
		}

  	}
	
    public void Paint(Graphics brush, Polygon p, Color c){

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