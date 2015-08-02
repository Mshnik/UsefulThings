package concurrent;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/** Represents a group of semaphores identified by distinct strings
 * Supports basic acquire and release operations. Other operations could be added as necessary
 * @author MPatashnik
 */
public class SemaphoreGroup {

	/** True iff this is fair - wakes threads in the order they wait */
	private final boolean fair;

	/** The total number of permits available to this, as it was constructed */
	private final HashMap<String, Integer> permits;

	/** The semaphores in this group, by their identifier */
	private final HashMap<String, Semaphore> semaphores;

	/** The semaphore monitoring use of operations in this SemaphoreGroup */
	private final ReentrantLock operationLock;

	/** A map of threads to permits they currently own */
	private final HashMap<Thread, Map<String, Integer>> threads;

	/** The threads waiting to acquire this */
	private final List<Thread> waitingThreads;

	/** Set to true to see printing output of threads acquiring and releasing */
	private static final boolean DEBUG = false;

	/** Creates a SemaphoreGroup. All semaphores are initialized as unfair.
	 * @param permits - the Number of permits for each identifier string
	 * @param fair - true iff this Semaphore is fair (wakes threads in the order they wait
	 */
	public SemaphoreGroup(Map<String, Integer> permits, boolean fair) {
		this.permits = new HashMap<String, Integer>(permits);
		this.fair = fair;
		operationLock = new ReentrantLock();
		semaphores = new HashMap<String, Semaphore>();
		threads = new HashMap<Thread, Map<String, Integer>>();
		waitingThreads = Collections.synchronizedList(new LinkedList<Thread>());
		for(String s : permits.keySet()){
			semaphores.put(s, new Semaphore(permits.get(s)));
		}
	}

	/** Locks the operationLock if it isn't already locked by this thread
	 * Use at the start of any complex operation
	 */
	private void startOperation(){
		if(! operationLock.isHeldByCurrentThread()) operationLock.lock();
		if(DEBUG) System.out.println("Acquired " + Thread.currentThread().getName());
	}

	/** Unlocks the operationLock
	 * Use at the end of any complex operation in a finally block
	 */
	private void endOperation(){
		if(operationLock.isHeldByCurrentThread()) operationLock.unlock();
		if(DEBUG) System.out.println("Released " + Thread.currentThread().getName());
	}
	
	/** Pauses the current thread because it can't access the semaphore at this time */
	private void pauseThread() throws InterruptedException{
		if(fair){
			Thread t = Thread.currentThread();
			synchronized(t){
				t.wait();
			}
		} else{
			synchronized(operationLock){
				operationLock.wait();
			}
		}
	}
	
	/** Resumes a thread to try to access the semaphore again */
	private void resumeThread(){
		if(fair){
			synchronized(waitingThreads){
				Thread t = waitingThreads.get(0);
				synchronized(t){
					t.notify();
				}
			}
		} else{
			synchronized(operationLock){
				operationLock.notify();
			}
		}
	}

	/** Attempts to acquire the given permits
	 * @param permits					- the permits to acquire
	 * @throws InterruptedException		- see Semaphore.acquire()
	 * @throws IllegalArgumentException - If one of the permits this wants to 
	 * 										acquire is an unrecognized string, or any of the
	 * 										permit acquisition counts is negative
	 */
	public void acquire(Map<String, Integer> permits) 
			throws InterruptedException, IllegalArgumentException{
		try{
			startOperation();
			for(Map.Entry<String, Integer> e : permits.entrySet()){
				Semaphore s = semaphores.get(e.getKey());
				if(s == null){
					throw new IllegalArgumentException("Illegal Permit Name " + e.getKey() + " Not in " + this);
				}
				if(e.getValue() < 0)
					throw new IllegalArgumentException("Illegal Permit Value " + e.getValue() + " Must be positive");
				if(s.availablePermits() < e.getValue()){
					endOperation();
					//Not enough permits - wait on semaphore until someone releases, then try again
					synchronized(waitingThreads){ 
						waitingThreads.remove(Thread.currentThread()); //Make sure this thread goes to the back
						waitingThreads.add(Thread.currentThread());
					}
					pauseThread();
					acquire(permits);
					synchronized(waitingThreads){
						waitingThreads.remove(Thread.currentThread());
					}
					return;
				}
			}
			//All semaphores ok. Do acquiring and exit
			for(Map.Entry<String, Integer> e : permits.entrySet()){
				semaphores.get(e.getKey()).acquire(e.getValue());
			}
			Thread t = Thread.currentThread();
			//Update information of this thread owning permits
			Map<String, Integer> currentlyOwned = threads.get(t);
			if(currentlyOwned == null){
				threads.put(t, new HashMap<String, Integer>(permits));
			}
			else{
				HashMap<String, Integer> totalOwned = new HashMap<String, Integer>(permits);
				for(Map.Entry<String, Integer> e : permits.entrySet()){
					totalOwned.put(e.getKey(), 
							e.getValue() 
							+ (totalOwned.get(e.getKey()) == null ? 0 : currentlyOwned.get(e.getKey())));
				}
				threads.put(t, totalOwned);
			}
		}
		finally{
			endOperation();
		}
	}

	/** Attempts to acquire all permits available. Returns
	 * the acquired permits. May return an empty map if no permits were available.
	 * @return - The acquired permits. Will never be null, but may be empty.
	 * @throws InterruptedException - see Semaphore.acquire
	 */
	public Map<String, Integer> drain() throws InterruptedException{
		try{
			startOperation();
			Map<String, Integer> m = getAvailablePermits();
			acquire(m);
			return m;
		}
		finally{
			endOperation();
		}
	}

	/** Attempts to release the given amounts of the given permits.
	 * Won't release more permits for any identifier than this currently owns.
	 * @param permits				- the permits to release.
	 * @throws InterruptedException - see Semaphore.acquire
	 */
	public void release(Map<String, Integer> permits) throws InterruptedException{
		try{
			startOperation();
			Thread t = Thread.currentThread();

			//Check to see if this thread has any permits at all
			if(! threads.containsKey(t))
				return;

			for(Map.Entry<String, Integer> e : permits.entrySet()){
				Semaphore s = semaphores.get(e.getKey());
				if(s == null){
					throw new IllegalArgumentException("Illegal Permit Name " + e.getKey() + " Not in " + this);
				}
				int has = threads.get(t).containsKey(e.getKey()) ? threads.get(t).get(e.getKey()) : 0;
				int toRemove = Math.min(e.getValue(), has);
				s.release(toRemove);
				threads.get(t).put(e.getKey(), has - toRemove);
			}
			if(DEBUG){
				System.out.println("\nReleasing " + t);
				System.out.println(threads.toString().replaceAll("},", "}\n"));
			}

			//Ok, notify a thread wanting to acquire
			resumeThread();
		}finally{
			endOperation();
		}
	}

	/** Releases all permits this currently owns for all identifiers within this Semaphore Group
	 * @throws InterruptedException - see Semaphore.acquire
	 */
	public void releaseAll() throws InterruptedException{
		try{
			startOperation();
			Thread t = Thread.currentThread();
			if(! threads.containsKey(t)) return;
			HashMap<String, Integer> permits = new HashMap<String, Integer>(threads.get(t));
			release(permits);
		}finally{
			endOperation();
		}

	}

	/** Returns the permits (by identifier) this SemaphoreGroup still has available. */
	public Map<String, Integer> getAvailablePermits(){
		HashMap<String, Integer> available = new HashMap<>();
		for(Entry<String, Semaphore> e : semaphores.entrySet()){
			available.put(e.getKey(), e.getValue().availablePermits());
		}
		return available;
	}

	/** Returns the set of valid identifying strings for this semaphore group */
	public Set<String> getIdentifyingStrings(){
		return semaphores.keySet();
	}

	/** Returns a collection containing threads that may be waiting to acquire */
	public Collection<Thread> getQueuedThreads(){
		synchronized(waitingThreads){
			return new LinkedList<Thread>(waitingThreads);
		}
	}

	/** Returns an estimate of the number of threads waiting to acquire this SemaphoreGroup */
	public int getQueueLength(){
		return waitingThreads.size();
	}

	/** Returns iff this is fair */
	public boolean isFair(){
		return fair;
	}

	/** Returns the available permits out of the total as the toString */
	@Override
	public String toString(){
		Map<String, Integer> available = getAvailablePermits();
		String s = "{";
		for(Entry<String, Integer> e : permits.entrySet()){
			s += e.getKey() + "=" + available.get(e.getKey()) + "/" + e.getValue() + ", ";
		}
		return s.substring(0, s.length() - 2) + "}";
	}

}
