package sfad.tp3android;

/**
 *  class EXECPTION for the class movement
 */
public class MovementException extends Exception {

    public MovementException(){
        super("MouvementException");
    }

    public MovementException(String message){
        super(message);
    }
}

