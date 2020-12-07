package com.progmatic.labyrinthproject.exceptions;

/**
 *
 * @author pappgergely
 */
public class InvalidMoveException extends Exception {

    @Override
    public String getMessage(){
        return "You can't go that way.";
    }

}
