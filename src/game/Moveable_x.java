package game;
/**
 * Moveable_x is an interface which serves as a template for all objects
 * which need to move across the x-axis. It has a maximum speed which acts
 * as a cap for how fast objects can move. It also has a method for translation
 * along the x-axis built in named update.
 * @author Augniv Mitra
 * @author James Hui
 * @version %I% %G%
 */
public interface Moveable_x {
    static int max_move_speed = 3500;

    /**
     * Moves the position of this element along the x-axis, the amount of
     * movement being based on the score parameter.
     * @param score The score used to offset the element
     */
    public void update(int score);
}
