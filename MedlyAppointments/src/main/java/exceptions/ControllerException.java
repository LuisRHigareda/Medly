package exceptions;

/**
 *
 * @author Leonardo Flores Leyva
 */
public class ControllerException extends Exception {

    /**
     * Creates a new instance of <code>ControllerException</code> without detail
     * message.
     */
    public ControllerException() {}

    /**
     * Constructs an instance of <code>ControllerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ControllerException(String msg) {
        super(msg);
    }
}