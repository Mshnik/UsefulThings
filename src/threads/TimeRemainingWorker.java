package threads;

/** Monitors the progress of the inputed Monitorable and periodically sends time remaining updates to the Monitorable
 * 
 * 	Every (update) seconds, the worker finds the amount of time that has passed since the
 *  last check and stores that as the new x coordinate, and finds the new progress value
 *  and stores that as the new y coordinate.
 *  
 *  Then, using the new x and y values along with the most recent old x and y values,
 *  the worker calculates the time remaining to completion using a linear regression,
 *  then sends an update
 *  
 * @author MPatashnik
 */
public class TimeRemainingWorker extends Thread{

	public static final int DEFAULT_UPDATE_INTERVAL = 1;	//Time to wait, in seconds, before update.
	public static final double DEFAULT_COMPLETED_VALUE = 100;	//A default y value to be interpreted as "done".
	
	private Monitorable watch;					//The object (a Thread or extension of a thread) to watch for progress
	
	private long startTime = -1;			   //The system time when this worker was started, in milliseconds. -1 until it is started
	private long finishTime = -1;              //The system time when this worker finishes.
	
	private boolean running = false;		   //True while this thread is running. Set this field to false to stop the monitoring
	
	private double x[] = {0, 0};				//Set of 2 most recent x points (time)
	private double y[] = {0, 0};				//Set of 2 most recent y points (progress)
	
	private double completedValue = DEFAULT_COMPLETED_VALUE;
	private int updateInterval = DEFAULT_UPDATE_INTERVAL;
	
	private int timeRemaining = -1;					//Time remaining in this task. -1 until the task has started.
	private double percentComplete = -1;			//Percent completion for this task. -1 until task has started.
	
	/** Constructor - stores the value of the start time of this worker and the Monitorable to watch.
	 * updateInterval is set to the DEFAULT_UPDATE_INTERVAL, and completedValue is set to the DEFUALT_COMPLETED_VALUE
	 * @param toWatch - the Monitorable object to watch for progress and to send updates on completion time
	 */
	public TimeRemainingWorker(Monitorable toWatch){
		this(toWatch, DEFAULT_UPDATE_INTERVAL, DEFAULT_COMPLETED_VALUE);
	}
	
	/** Constructor - stores the value of the start time of this worker and the Monitorable to watch.
	 * @param toWatch - the Monitorable object to watch for progress and to send updates on completion time
	 * @param updateInterval - the update interval which the TimeRemainingWorker will wait before 
	 * 		calculating and sending updates (in seconds). updateInterval > 0.
	 * @param completedValue - A value to be interpreted as completion when received from toWatch. completedValue > 0
	 * @throws IllegalArgumentException - If updateInterval <= 0 or completedValue <= 0
	 */
	public TimeRemainingWorker(Monitorable toWatch, int updateInterval, double completedValue) throws IllegalArgumentException{
		watch = toWatch;
		
		if(updateInterval <= 0 || completedValue <= 0)
			throw new IllegalArgumentException();
		
		this.updateInterval = updateInterval;
		this.completedValue = completedValue;
		
		setName(toWatch.getName() + " - Time Worker");
	}

	/** Returns true if the TimeRemainingWorker is currently monitoring progress, false otherwise */
	public boolean getRunning(){
		return running;
	}
	
	/** Stops this TimeRemainingWorker and causes its thread to die. Task is marked as incomplete */
	public void cancel(){
		running = false;
	}
	
	/** Returns the updateInterval in seconds */
	public int getUpdateInterval(){
		return updateInterval;
	}
	
	/** Sets the updateInterval to n seconds. n > 0. 
	 * New update interval will be used on next time calculation (may not take affect until
	 * up to another old update interval seconds)*/
	public void setUpdateInterval(int n) throws IllegalArgumentException{
		if(n > 0)
			updateInterval = n;
		else
			throw new IllegalArgumentException();
	}
	
	/** Returns the startTime, the system time this timeRemaining worker started working */
	public long getStartTime(){
		return startTime;
	}
	
	/** Returns the finishTime, the system time when this timeRemaining worker finished working, (maybe canceled though) */
	public long getFinishTime(){
		return finishTime;
	}
	
	/** Returns the time remaining (in seconds) for this task. Returns -1 if the task has not yet
	 * started running
	 */
	public int getTimeRemaining(){
		return timeRemaining;
	}
	
	/** Gets completedValue, the value for this task that is interpreted as task completion. Default value 100.
	 */
	public double getCompletedValue(){
		return completedValue;
	}
	
	/** Returns the current completion according to the Monitorable's completion */
	public double getCurrentCompletion(){
		return watch.getCompletionValue();
	}
	
	/** Returns the current percent completion of the Monitorable's task. Returns -1 if the task has not yet started */
	public double getPercentComplete(){
		return percentComplete;
	}
	
	/** Returns the Monitorable that this TimeRemainingWorker is watching */
	public Monitorable getMonitorable(){
		return watch;
	}
	
	@Override
	/** Sets running to true, then:  
	 * While running is true, waits updateInterval seconds. Then, finds the new completion value from the Monitorable.
	 * Uses this value along with the previous completionValue to find a new linear line to the completedValue.
	 * Uses the x coordinate of this intersect to determine the amount of time remaining, and notifies the Monitorable of this amount.
	 */
	public void run(){
		running = true;
		startTime = System.currentTimeMillis();
		while(running){					//While the process is running

			try {
				sleep(updateInterval*1000);						//Sleep for update seconds before calculating
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			//x[1] - x[0] = The time elapsed since last update
			x[0] = x[1];												//Push old x value down
			x[1] = System.currentTimeMillis()/1000 - startTime/1000;	//Calculate new x value,
																		

			y[0] = y[1];										//Push old y value down
			y [1] = watch.getCompletionValue();					//Calculate new y value, the new completion amount

			timeRemaining = timeRemaining();		//Calculate and store the time remaining
			percentComplete = getCurrentCompletion()/getCompletedValue();

			watch.update();//Tell the Monitorable that the time and percentages has updated.
		}
		finishTime = System.currentTimeMillis();
	}

	/** Finds the line that goes through the two points in x and y,
	 * then calculates the intersect of that line with y=compltedValue.
	 * The x value of that intersection is the estimated finishing time.
	 * Subtracting the current time (x[1]) from that time gives the estimated
	 * remaining time.
	 * @return	The time in seconds remaining. >= 0
	 */
	private int timeRemaining(){
		//Find the slope and intersect of the current line defined by x and y.
		double slope = (y[1]-y[0])/(x[1]-x[0]);
		double intersect = y[0] - slope*x[0];

		//Find the intersection of regression with a horizontal line at y = COMPLETED_VALUE
		int finishTime =(int)((completedValue-intersect)/slope);

		return Math.max(finishTime - (int)x[1],0);	
	}
}