
package test;


import org.junit.Before;
import org.junit.Test;

import sfad.tp3android.MovementException;
import sfad.tp3android.Particle;
import sfad.tp3android.ParticleException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test class of the class model Particle
 * 
 * @author Sébastien Fillion,  Alexis Demers
 */
public class ParticleTest{
    
    private double x;
    private double y;
    private double radius;
    private double speed;
    private double angle;
    private Particle p1, p2, p3 ,p4, p5, p6, p7, p8, p9, p10, p11, p12;
   
    /**
     * Method to build Particle object to make different test
     * 
     * @throws MovementException - Class fail to build
     */
    @Before
    public void setUp()throws ParticleException, MovementException {
    
        //position x at is min limit
        p1 = new Particle(7, 50, 160, 3, 7,0);
        
        p2 = new Particle(1194, 50, 198, 6, 5,0);
        
        //position y at is min limit
        p3 = new Particle(67, 6, 280, 8, 6,0);
        
        p4 = new Particle(67, 673, 299, 9, 5,0);
        
        //for the test of colliding
        //same position
        p5 = new Particle(100, 100, 99, 6, 6,0);
        p6 = new Particle(100, 100, 99, 6, 6,0);
        
        //no collision different x, same y
        p7 = new Particle(50, 8, 99, 6, 5,0);
        p8 = new Particle(50, 40, 99, 6, 5,0);
        
        //no collision different y, same x
        p9 = new Particle(89, 200, 99, 6, 8,0);
        p10 = new Particle(220, 200, 99, 6, 9,0);
        
        //the position == sum of all radius (collision)
        p11 = new Particle(100, 200, 99, 6, 5,0);
        p12 = new Particle(110, 200, 99, 6, 5,0);
                
    }
   
    
    /**
     * method testing class validate x, who check if the x is gretter than 0
     */
     @Test
    public void testValidateX() {
       System.out.println("validate X");
       assertTrue(p1.validateX(p1.getX()));
       assertTrue(p2.validateX(p2.getX()));
       assertFalse(p1.validateX(-5000.0));
      
    }
    
     /**
     * method testing class validate x, who check if the y is gretter than 0
     */
       @Test
    public void testValidateY() {
       System.out.println("validate Y");
       assertTrue(p3.validateY(p3.getY()));
       assertTrue(p4.validateY(p4.getY()));
       assertFalse(p3.validateY(-5000.0));
    }
    
    /**
     * method testing class validate spedd, who check if the speed is between 0 and 10
     */
       @Test
    public void testValidateSpeed() {
       System.out.println("validate speed");
       assertFalse(p1.validateSpeed(-5.0));
       assertTrue(p1.validateSpeed(0.0));
       assertTrue(p1.validateSpeed(6.0));
       assertTrue(p1.validateSpeed(10.0));
       assertFalse(p1.validateSpeed(1000.0));
       assertTrue(p1.validateSpeed(Particle.MAX_SPEED_VALUE));
       assertTrue(p1.validateSpeed(Particle.MIN_SPEED_VALUE));
       assertTrue(p1.validateSpeed(Particle.DEFAULT_SPEED));
    }
    
    /**
     * method testing class validate angle, who check if the angle is between 0 and 360
     */
       @Test
    public void testValidateAngle() {
       System.out.println("validate angle");
       assertFalse(p1.validateAngle(-5.0));
       assertTrue(p1.validateAngle(0.0));
       assertTrue(p1.validateAngle(180.0));
       assertTrue(p1.validateAngle(360.0));
       assertFalse(p1.validateAngle(1000.0));
       assertTrue(p1.validateAngle(Particle.MAX_ANGLE_VALUE));
       assertTrue(p1.validateAngle(Particle.MIN_ANGLE_VALUE));
       assertTrue(p1.validateAngle(Particle.DEFAULT_ANGLE));
    }
    
     /**
     * method testing class validate radius, who check if the radius is between 4 and 10
     */
       @Test
    public void testValidateRadius() {
       System.out.println("validate radius");
       assertFalse(p1.validateRadius(-5.0));
       assertFalse(p1.validateRadius(0.0));
       assertTrue(p1.validateRadius(4.0));
       assertTrue(p1.validateRadius(6.0));
       assertTrue(p1.validateRadius(10.0));
       assertFalse(p1.validateRadius(1000.0));
       assertTrue(p1.validateRadius(Particle.MAX_RADIUS_VALUE));
       assertTrue(p1.validateRadius(Particle.MIN_RADIUS_VALUE));
       assertTrue(p1.validateRadius(Particle.DEFAULT_RADIUS));
    }

    
     
    /**
     * method testing class validate position, who check if the x and y of the particle are in the pane width and height limit
     */
    @Test
    public void testValidatePosition() {
        System.out.println("validate Position");
        double c;
        double r;
        double maxPaneWith = 1200.0;
        double maxPaneHeight = 679.0;
        double expResult;
        double result;
        
        //when the particle is stick to the left side of the pane
        c = 3.0;
        r = 2.0;
        expResult = 3.0;
        result = Particle.validatePosition(c, r, maxPaneWith);
        assertEquals(expResult, result, 0.0);
        
        //when the particle is exceeding to the left side of the pane, the position need to be change
        c = 1.0;
        r = 3.0;
        expResult = 4.0;
        result = Particle.validatePosition(c, r, maxPaneWith);
        assertEquals(expResult, result, 0.0);
        
        //when the particle is almost stick to the left right side of the pane
        c = 1195.0;
        r = 3.0;
        expResult = 1195.0;
        result = Particle.validatePosition(c, r, maxPaneWith);
        assertEquals(expResult, result, 0.0);
        
        //when the particle is exceeding to the right side of the pane, the position need to be change
        c = 1200.0;
        r = 3.0;
        expResult = 1197.0;
        result = Particle.validatePosition(c, r, maxPaneWith);
        assertEquals(expResult, result, 0.0);
        
        //when the particle is stick to the left side of the pane, but the radius is >
        c = 15.0;
        r = 14.0;
        expResult = 15.0;
        result = Particle.validatePosition(c, r, maxPaneHeight);
        assertEquals(expResult, result, 0.0);
        
        //when the particle exceeding to the left side of the pane, but the radius is >
        c = 20.0;
        r = 50.0;
        expResult = 70.0;
        result = Particle.validatePosition(c, r, maxPaneHeight);
        assertEquals(expResult, result, 0.0);
        
         //when the particle is stick to the bottom side of the pane
        c = 659.0;
        r = 18.0;
        expResult = 659.0;
        result = Particle.validatePosition(c, r, maxPaneHeight);
        assertEquals(expResult, result, 0.0);
        
        //when the particle is exceeding to the bottom side of the pane, the position need to be change
        c = 700.0;
        r = 10.0;
        expResult = 690.0;
        result = Particle.validatePosition(c, r, maxPaneHeight);
        assertEquals(expResult, result, 0.0);
    }
    
    /**
     * method testing class move, who check if the variable movement x and movement y result the good movement divide by the rate of the application
     */
     @Test
    public void testMove() {
        System.out.println("Move");
        
        double xP1 = p1.getX();
        double xP2 = p2.getX();
        double xP3 = p3.getX();
        double yP1 = p1.getY();
        double yP2 = p2.getY();
        double yP3 = p3.getY();
        
        //with positif value
        p1.move(15);
        p2.move(10);
        
        //with negative value
        p3.move(-7);

        assertTrue( p1.getX() == (xP1 + p1.getMovement().getXMovement()/15));
        assertTrue( p1.getY() == (yP1 + p1.getMovement().getYMovement()/15));
        assertTrue( p2.getX() == (xP2 + p2.getMovement().getXMovement()/10));
        assertTrue( p2.getY() == (yP2 + p2.getMovement().getYMovement()/10));
        assertTrue( p3.getX() == (xP3 + p3.getMovement().getXMovement()/-7));
        assertTrue( p3.getY() == (yP3 + p3.getMovement().getYMovement()/-7));
    }
    
    /**
     * method testing is colliding, check for each particle theire position and radius to evaluate if theu are colliding or not
     * return true if colliding or false if is not
     */
     @Test
    public void testIsColliding() {
       System.out.println("isColliding");
        
     assertTrue(p5.isColliding(p6));
     assertFalse(p7.isColliding(p8));
     assertFalse(p9.isColliding(p10));
     assertTrue(p11.isColliding(p12));
        
    }
}