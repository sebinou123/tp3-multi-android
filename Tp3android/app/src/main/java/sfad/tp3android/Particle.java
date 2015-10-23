package sfad.tp3android;

/**
 *
 */
public class Particle{
    /**
     * Constant variables representing default values, min values and max values
     * of most the Particle's attributes
     */
    public static final double DEFAULT_SPEED = 0;
    public static final double DEFAULT_ANGLE = 0;
    public static final double DEFAULT_RADIUS = 4;
    public static final double DEFAULT_X_MOVEMENT = 1;
    public static final double DEFAULT_Y_MOVEMENT = 0;

    public static final double MAX_ANGLE_VALUE = 360;
    public static final double MIN_ANGLE_VALUE = 0;
    public static final double MAX_RADIUS_VALUE = 10;
    public static final double MIN_RADIUS_VALUE = 4;
    public static final double MAX_SPEED_VALUE = 10;
    public static final double MIN_SPEED_VALUE = 0;
    public static final double MIN_X_VALUE = 0;
    public static final double MIN_Y_VALUE = 0;

    private double x;
    private double y;
    private double radius;
    private Movement movement;
    private double speed;
    private double angle;
    /**
     * Constructor using default values
     */
    public Particle(){
        this.setX(Particle.MIN_X_VALUE + MIN_RADIUS_VALUE);
        this.setY(Particle.MIN_Y_VALUE + MIN_RADIUS_VALUE);
        this.setSpeed(Particle.DEFAULT_SPEED);
        this.setAngle(Particle.DEFAULT_ANGLE);
        this.setMovement(new Movement(Particle.DEFAULT_SPEED));
        this.setRadius(Particle.DEFAULT_RADIUS);
    }

    public Particle(double x, double y, double a, double s, double r){
        this.x = x;
        this.y = y;
        this.radius = r;
        this.speed = s;
        this.angle = a;
        this.movement = new Movement(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)), s);
    }

    /**
     * Moves the particle given the timeframe the game is running on
     * @param rate Time frame the game is running on
     */
    public void move(double rate){
        this.x += this.movement.getXMovement() / rate;
        this.y += this.movement.getYMovement() / rate;
    }

    /**
     * Validates an x or y position given a radius and a plane bound
     * @param c x or y value to validate on the board
     * @param r radius of the particle
     * @param max max value of the bound to check for validity
     * @return
     */
    public static double validatePosition(double c, double r, double max){
        c = (c - r < 1 ) ? (c + r) : c;
        c = (c + r > max - 1) ? (c - r) : c;

        return c;
    }

    /**
     * Checks for collision between this particle and the one received in the paramters
     * @param p Particle to check for collision
     * @return True if colliding
     */
    public boolean isColliding(Particle p){
        return((Math.sqrt(Math.pow(this.getX()-p.getX(), 2) + Math.pow(this.getY()-p.getY(), 2))/*Hypothenuse*/
                <= (this.getRadius() + p.getRadius())/*Distance between centers*/));
    }

    /**
     * Collide this particle with the given particle
     * @param p Particle that is colliding
     */
    public void collide(Particle p){
        // NOTE: La collision ne prends pas en comtpe la masse des deux balles, nous avons essayÃ©
        // mais nos connaissances en mathÃ©matiques et physique sont limitÃ©es et nous ne trouvions
        // pas une bonne faÃ§on de le faire, la comprendre et l'appliquer. @alexisd
        double tempx = p.getMovement().getXMovement();
        double tempy = p.getMovement().getYMovement();
        p.getMovement().setXMovement(this.getMovement().getXMovement());
        p.getMovement().setYMovement(this.getMovement().getYMovement());
        this.getMovement().setXMovement(tempx);
        this.getMovement().setYMovement(tempy);

        /*
        this.getMovement().setXMovement((this.getMovement().getXMovement()*(this.getRadius()-p.getRadius())+(2*p.getRadius()*p.getMovement().getXMovement()))/(this.getRadius()+p.getRadius()));
        p.getMovement().setXMovement((p.getMovement().getXMovement()*(p.getRadius()-this.getRadius())+(2*this.getRadius()*this.getMovement().getXMovement()))/(p.getRadius()+this.getRadius()));
        this.getMovement().setYMovement((this.getMovement().getYMovement()*(this.getRadius()-p.getRadius())+(2*p.getRadius()*p.getMovement().getYMovement()))/(this.getRadius()+p.getRadius()));
        p.getMovement().setYMovement((p.getMovement().getYMovement()*(p.getRadius()-this.getRadius())+(2*this.getRadius()*this.getMovement().getYMovement()))/(p.getRadius()+this.getRadius()));
    */
    }

    /**
     * Sets a speed value for the particle if given parameters are valid
     * @param speed Speed value
     * @return True if valid
     */
    private boolean setSpeed(double speed){
        boolean returnValue;
        if(returnValue = validateSpeed(speed)){
            this.speed = speed;
        } else {
            this.speed = DEFAULT_SPEED;
        }
        return returnValue;
    }

    /**
     * Sets an angle value for the particle if given parameters are valid
     * @param angle Angle value
     * @return True if valid
     */
    private boolean setAngle(double angle){
        boolean returnValue;
        if(returnValue = this.validateAngle(angle)){
            this.angle = angle;
        }
        return returnValue;
    }

    /**
     * Sets a speed value for the particle if given parameters are valid
     * @param radius radius value
     * @return True if valid
     */
    private boolean setRadius(double radius){
        boolean returnValue;
        if(returnValue = validateRadius(radius)){
            this.radius = radius;
        } else {
            this.radius = DEFAULT_RADIUS;
        }
        return returnValue;
    }

    /**
     * Sets an x value for the particle if given parameters are valid
     * @param x x value
     * @return True if valid
     */
    public boolean setX(double x){
        boolean returnValue;
        if(returnValue = this.validateX(x)){
            this.x = x;
        }
        return returnValue;
    }

    /**
     * Sets an y value for the particle if given parameters are valid
     * @param y y value
     * @return True if valid
     */
    public boolean setY(double y){
        boolean returnValue;
        if(returnValue = this.validateY(y)){
            this.y = y;
        }
        return returnValue;
    }

    /**
     * Sets a Movement class for the particle
     */
    private void setMovement(Movement movement){
        this.movement = movement;
    }

    /**
     * Gets the x value for the particle
     * @return x value
     */
    public double getX(){
        return this.x;
    }

    /**
     * Gets the y value for the particle
     * @return y value
     */
    public double getY(){
        return this.y;
    }

    /**
     * Gets the angle value for the particle
     * @return angle value
     */
    public double getAngle(){
        return this.angle;
    }

    /**
     * Gets the Movement class for the particle
     * @return movement value
     */
    public Movement getMovement(){
        return this.movement;
    }

    /**
     * Gets the speed value for the particle
     * @return speed value
     */
    public double getSpeed(){
        return this.speed;
    }

    /**
     * Gets the radius value for the particle
     * @return radius value
     */
    public double getRadius(){
        return this.radius;
    }

    /**
     * Validates a given x value
     * @param x x value to validate
     * @return True if valid
     */
    public boolean validateX(double x) {
        return (x - this.getRadius() >= Particle.MIN_X_VALUE);
    }

    /**
     * Validates a given y value
     * @param y y value to validate
     * @return True if valid
     */
    public boolean validateY(double y) {
        return (y - this.getRadius() >= Particle.MIN_Y_VALUE);
    }

    /**
     * Validates a given angle value
     * @param angle angle value to validate
     * @return True if valid
     */
    public boolean validateAngle(double angle) {
        return (angle  >= Particle.MIN_ANGLE_VALUE && angle <= Particle.MAX_ANGLE_VALUE);
    }

    /**
     * Validates a given speed value
     * @param speed speed value to validate
     * @return True if valid
     */
    public boolean validateSpeed(double speed) {
        return (speed  >= Particle.MIN_SPEED_VALUE && speed <= Particle.MAX_SPEED_VALUE);
    }

    /**
     * Validates a given radius value
     * @param radius radius value to validate
     * @return True if valid
     */
    public boolean validateRadius(double radius) {
        return (radius  >= Particle.MIN_RADIUS_VALUE && radius <= Particle.MAX_RADIUS_VALUE);
    }
}

