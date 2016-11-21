/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.ig.odi.epicearc.exception;

/**
 *
 * @author julien.plumez
 */
public class IncorrectStateException extends Exception {

    /**
     * Creates a new instance of <code>IncorrectStateException</code> without
     * detail message.
     */
    public IncorrectStateException() {
        
    }

    /**
     * Constructs an instance of <code>IncorrectStateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public IncorrectStateException(String msg) {
        super(msg);
    }
}
