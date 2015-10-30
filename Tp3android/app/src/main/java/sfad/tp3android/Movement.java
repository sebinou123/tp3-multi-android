package sfad.tp3android;

/**
 *  Class movement who work with the movement change for the particle
 */
public class Movement {
    /**
     * Constant variables representing default values
     * of most the Particle's attributes
     */
    public static final double DEFAULT_X_MOVEMENT_VALUE = 1;
    public static final double DEFAULT_Y_MOVEMENT_VALUE = 0;

    private double xMovement;
    private double yMovement;

    /**
     * Default constructor for movement using default values
     */
    public Movement(double speed) {
        setXMovement(Movement.DEFAULT_X_MOVEMENT_VALUE*speed);
        setYMovement(Movement.DEFAULT_Y_MOVEMENT_VALUE*speed);
    }

    /**
     * Constructor using a degree angle value
     * @param xMovement movement of x
     * @param yMovement movement of y
     * @param speed speed value in degrees
     * @throws MovementException
     */
    public Movement(double xMovement, double yMovement, double speed) {
        setXMovement(xMovement*speed);
        setYMovement(yMovement*speed);
    }

    /**
     * Sets the x value for the movement
     * @param value x value
     */
    public void setXMovement(double value){
        this.xMovement = value;
    }

    /**
     * Sets the y value for the movement
     * @param value y value
     */
    public void setYMovement(double value){
        this.yMovement = value;
    }

    /**
     * Gets the x value for the movement
     * @return x value
     */
    public double getXMovement(){
        return this.xMovement;
    }

    /**
     * Gets the y value for the movement
     * @return y value
     */
    public double getYMovement(){
        return this.yMovement;
    }
}

