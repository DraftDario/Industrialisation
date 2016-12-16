package ch.hearc.ig.odi.epicearc.exception;

/**
 *
 * @author julien.plumez
 */
public class MissingParameterException extends Exception {

    /**
     * Creates a new instance of <code>MissingParameterException</code> without
     * detail message.
     */
    public MissingParameterException() {}

    /**
     * Constructs an instance of <code>MissingParameterException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MissingParameterException(String msg) {
        super(msg);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
