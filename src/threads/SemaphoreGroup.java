import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

/** Represents a group of semaphores identified by distinct strings
 * Supports basic acquire and release operations. Other operations could be added as necessary
 * @author MPatashnik
 */
public class SemaphoreGroup {

	/** The total number of permits available to this, as it was constructed */
	private final HashMap<String, Integer> permits;
	
	/** The semaphores in this group, by their identifier */
	private final HashMap<String, Semaphore> semaphores;

    /** The semaphore monitoring use of operations in this SemaphoreGroup */
	private final Semaphore operationLock;

    /** A map of threads to permits they currently own */
	private final HashMap<Thread, Map<String, Integer>> threads;

    /** Set to true to see printing output of threads acquiring and releasing */
	private static final boolean DEBUG = true;
	
	/** Creates a SemaphoreGroup. All semaphores are initialized as unfair.
	 * @param permits - the Number of permits for each identifier string
	 */
	public SemaphoreGroup(Map<String, Integer> permits) {
		this.permits = new HashMap<String, Integer>(permits);
		operationLock = new Semaphore(1);
		semaphores = new HashMap<String, Semaphore>();
		threads = new HashMap<Thread, Map<String, Integer>>();
		for(String s : permits.keySet()){
			semaphores.put(s, new Semaphore(permits.get(s)));
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
			operationLock.acquire();
			if(DEBUG) System.out.println("Acquired " + Thread.currentThread().getName());
			for(Map.Entry<String, Integer> e : permits.entrySet()){
				Semaphore s = semaphores.get(e.getKey());
				if(s == null){
					throw new IllegalArgumentException("Illegal Permit Name " + e.getKey() + " Not in " + this);
				}
				if(e.getValue() < 0)
					throw new IllegalArgumentException("Illegal Permit Value " + e.getValue() + " Must be positive");
				if(s.availablePermits() < e.getValue()){
					operationLock.release();
					if(DEBUG) System.out.println("Released " + Thread.currentThread().getName());
					//Not enough permits - wait on semaphore until someone releases, then try again
					synchronized(operationLock){
						operationLock.wait();
					}
					acquire(permits);
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
			operationLock.release();			
			if(DEBUG) System.out.println("Released " + Thread.currentThread().getName());
		}
	}

	/** Attempts to release the given amounts of the given permits.
	 * Won't release more permits for any identifier than this currently owns.
	 * @param permits				- the permits to release.
	 * @throws InterruptedException - see Semaphore.acquire
	 */
	public void release(Map<String, Integer> permits) throws InterruptedException{
		try{
			operationLock.acquire();
			if(DEBUG) System.out.println("Acquired " + Thread.currentThread().getName());
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
			synchronized(operationLock){
				operationLock.notify();
			}
		}finally{
			operationLock.release();
			if(DEBUG) System.out.println("Released " + Thread.currentThread().getName());
		}
	}
	
	/** Releases all permits this currently owns for all identifiers within this Semaphore Group
	 * @throws InterruptedException - see Semaphore.acquire
	 */
	public void releaseAll() throws InterruptedException{
		try{
			operationLock.acquire();
			if(DEBUG) System.out.println("Acquired " + Thread.currentThread().getName());
			Thread t = Thread.currentThread();
			if(! threads.containsKey(t)) return;
			HashMap<String, Integer> permits = new HashMap<String, Integer>(threads.get(t));
			operationLock.release();
			if(DEBUG) System.out.println("Released " + Thread.currentThread().getName());
			release(permits);
		}finally{
			operationLock.release();
			if(DEBUG) System.out.println("Released " + Thread.currentThread().getName());
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
