package ca.mcgill.ecse211.lab1;

import static ca.mcgill.ecse211.lab1.Resources.*;

/**
 * Samples the US sensor and invokes the selected controller on each cycle.
 * 
 * Control of the wall follower is applied periodically by the UltrasonicPoller thread. The while loop at the bottom
 * executes in a loop. Assuming that the us.fetchSample, and cont.processUSData methods operate in about 20ms, and that
 * the thread sleeps for 50 ms at the end of each loop, then one cycle through the loop is approximately 70 ms. This
 * corresponds to a sampling rate of 1/70ms or about 14 Hz.
 */
public class UltrasonicPoller implements Runnable {

  private UltrasonicController controller;
  private float[] usData;

  public UltrasonicPoller() {
    usData = new float[US_SENSOR.sampleSize()]; // create an array of float of size corresponding to the number of
                                                // elements in a sample. The number of elements does not change
                                                // during
                                                // runtime.
    controller = Main.selectedController; // create a UltraSonic controller
  }

  /*
   * Sensors now return floats using a uniform protocol. Need to convert US result to an integer [0,255] (non-Javadoc).
   * 
   * @see java.lang.Thread#run()
   */

  public void run() {
    int distance;
    while (true) {
      US_SENSOR.getDistanceMode().fetchSample(usData, 0); // acquire distance data in meters and store it in
                                                          // usData (an array of float)
      distance = (int) (usData[0] * 100.0); // extract from buffer (region of a physical memory storage used to
                                            // temporarily store data while it is being moved from one place to
                                            // another), convert to cm, cast to int
      controller.processUSData(distance); // now take action depending on value
      try {
        Thread.sleep(SLEEPINT);
      } catch (Exception e) {
      }
    }
  }
}
