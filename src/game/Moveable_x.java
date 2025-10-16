package game;
/**
 * Interface for all elements which move on the x-axis
 * Has a maximum speed for movement along and 
 * an update method
 */
public interface Moveable_x {
    static int max_move_speed = 3500;
    /**
     * Updates the element's x-position, offset based on score.
     * @param score
     */
    public void update(int score);
}
