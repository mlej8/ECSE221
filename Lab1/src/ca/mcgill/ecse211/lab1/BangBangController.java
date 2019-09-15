package ca.mcgill.ecse211.lab1;

import static ca.mcgill.ecse211.lab1.Resources.*;

public class BangBangController extends UltrasonicController {

  private static final int DELTASPD = 25;    // Bang-bang constant
  private static final int SHARP_TURN = MOTOR_HIGH;
  
  public BangBangController() {
    LEFT_MOTOR.setSpeed(MOTOR_SPEED); // Start robot moving forward
    RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
    LEFT_MOTOR.forward(); 
    RIGHT_MOTOR.forward();
  }

  /**  
   * Perform an action based on the US data input in (BANG-BANG style).
   * 
   * @param distance -- the distance between the US sensor and an obstacle in cm.
   */
  @Override
  public void processUSData(int distance) {
    // Rudimentary filter - toss out invalid samples corresponding to null signal and assigns distance value for current controller.
    filter(distance);  
   
    // compute the error
    int error = BAND_CENTER - this.distance; // (distance between the US sensor and an obstacle in cm) - (Standard offset from the wall cm). We need to tweak BAND_CENTER and BAND_WIDTH in order to make the robot smooth
    
    // log information 
    System.out.println("BANG_CENTER: " + BAND_CENTER + " US Distance: " + readUSDistance() + " Error: " + error); 
    
    // put the robot 20 cm from the wall is it possible to have two constant turns  
    
    if (Math.abs(error) <= BAND_WIDTH) { // if the error is smaller than threshold, continue forward. 
      LEFT_MOTOR.setSpeed(MOTOR_SPEED);
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
    }
       
    if (error > 6) { // SHARP TURN RIGHT 
      LEFT_MOTOR.setSpeed(MOTOR_SPEED + SHARP_TURN);
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED + SHARP_TURN);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.backward();
      } else if (error < -30) { // SHARP TURN LEFT 
//         get better at turning right
      LEFT_MOTOR.setSpeed(MOTOR_SPEED);
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED + SHARP_TURN);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
      } else if (error > 0){ // if error is bigger than 0, it means that current distance is too close from the obstacle, so we need to turn right 
      LEFT_MOTOR.setSpeed(MOTOR_SPEED + DELTASPD);
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED - DELTASPD);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
      } else if (error < 0) { // if error is smaller than 0, means that current distance is too far from the obstacle, so we need to turn left
      LEFT_MOTOR.setSpeed(MOTOR_SPEED - DELTASPD); // reduce speed on left motor, in order to turn right 
      RIGHT_MOTOR.setSpeed(MOTOR_SPEED + DELTASPD);
      LEFT_MOTOR.forward();
      RIGHT_MOTOR.forward();
      }      
  }
     
  /**
   * Returns the distance between the US sensor and an obstacle in cm.
   * 
   * @return the distance between the US sensor and an obstacle in cm
   */
  @Override
  public int readUSDistance() {
    return this.distance; 
  }
}
