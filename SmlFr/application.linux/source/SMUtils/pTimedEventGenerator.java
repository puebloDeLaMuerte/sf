package SMUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import processing.core.*;

public class pTimedEventGenerator {
  private class MethodInvoker implements Runnable {
    @Override
    public void run() {
      try {
        timerEvent.invoke(parentApplet);
      } catch (Exception e) {
        System.err.println("Disabling " + timerEventName + "() for TimedEventGenerator " +
            "because of an error.");
        e.printStackTrace();
        cancelTask();
      }
    }
  }

  public final static String VERSION = "1.0";
  private static final String defaultEventName = "onTimerEvent";
  private static final int defaultIntervalMs = 500;

  private PApplet parentApplet;
  private ScheduledExecutorService executor;
  private String timerEventName;
  private Method timerEvent;
  private int intervalMs;
  private boolean isEnabled;
  private ScheduledFuture<?> currentTask;

  /**
   * Construct an enabled TimedEventGenerator with the default event handler name of
   * <code>onTimerEvent</code> and the default interval of 500ms.
   *
   * @param parentApplet the parent Applet that this library runs inside (this in your sketch).
   */
  public pTimedEventGenerator(PApplet parentApplet) {
    this(parentApplet, defaultEventName, true);
  }

  /**
   * Construct an enabled TimedEventGenerator with a custom event handler name and the default
   * interval of 500ms.
   *
   * @param parentApplet the parent Applet that this library runs inside (this in your sketch).
   * @param timerEventName the name of the method to invoke when the timer fires.
   */
  public pTimedEventGenerator(PApplet parentApplet, String timerEventName) {
    this(parentApplet, timerEventName, true);
  }
 
  /**
   * Construct a TimedEventGenerator with a specific event handler name and specific enabled
   * status.
   *
   * @param parentApplet the parent Applet that this library runs inside (this in your sketch).
   * @param timerEventName the name of the method to invoke when the timer fires.
   * @param isEnabled determines if the TimedEventGenerator starts off active or not.
   */
  public pTimedEventGenerator(PApplet parentApplet, String timerEventName, boolean isEnabled) {
    this(parentApplet, timerEventName, isEnabled, defaultIntervalMs);
  }
 
  /**
   * Construct a TimedEventGenerator with a specific event handler name, a specific enabled
   * status and a specific interval.
   *
   * @param parentApplet the parent Applet that this library runs inside (this in your sketch).
   * @param timerEventName the name of the method to invoke when the timer fires.
   * @param isEnabled determines if the TimedEventGenerator starts off enabled or not.
   */
  public pTimedEventGenerator(PApplet parentApplet, String timerEventName, boolean isEnabled,
      int intervalMs) {
    this.parentApplet = parentApplet;
    this.timerEventName = timerEventName;
    this.isEnabled = isEnabled;
    this.intervalMs = intervalMs;
    try {
      timerEvent = parentApplet.getClass().getMethod(timerEventName, (Class<?>[]) null);
    } catch (Exception e) {
      this.isEnabled = false;
      throw new RuntimeException("Your sketch is using the TimedEventGenerator without defining " +
         "the target method " + timerEventName + "()!");
    }
    executor = Executors.newSingleThreadScheduledExecutor();
    if (isEnabled) {
      scheduleTask();
    }
  }

  /**
   * @return if the timer events will fire in the parent sketch.
   */
  public boolean isEnabled() {
    return isEnabled;
  }

  /**
   * Turns the timer on or off according to the <code>isEnabled</code> argument.
   *
   * @param isEnabled whether or not the timer event will fire in the parent sketch.
   */
  public void setEnabled(boolean isEnabled) {
    if (this.isEnabled) {
      cancelTask();
    } else {
      scheduleTask();
    }
    this.isEnabled = isEnabled;
  }

  /**
   * Returns the currently configured number of milliseconds between timer events firing.
   *
   * @return the interval time, in milliseconds.
   */
  public int getIntervalMs() {
    return intervalMs;
  }

  /**
   * Set the number of milliseconds between timer events firing.  Changing this will cancel an
   * enabled timer and immediately schedule a new one.
   *
   * @param intervalMs the interval time, in milliseconds.
   */
  public void setIntervalMs(int intervalMs) {
    this.intervalMs = intervalMs;
    if (isEnabled) {
      cancelTask();
      scheduleTask();
    }
  }

  /**
   * return the version of the library.
   *
   * @return String
   */
  public static String version() {
    return VERSION;
  }

  private void scheduleTask() {
    currentTask = executor.scheduleAtFixedRate(new MethodInvoker(), intervalMs, intervalMs,
        TimeUnit.MILLISECONDS);
  }

  private void cancelTask() {
    currentTask.cancel(true);
  }
  
  public void dispose() {
	  executor.shutdownNow();
	  
  }
}
