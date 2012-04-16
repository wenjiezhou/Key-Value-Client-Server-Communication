package edu.berkeley.cs162;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class ThreadPool {
	
	/**
	 * Set of threads in the thread pool
	 */
	protected Thread threads[] = null;
	protected Queue<Runnable> taskQueue = new LinkedList<Runnable>();
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	Date date = null;
	
	/**
	 * Initialize the number of threads required in the threadpool.
	 * 
	 * @param size How many threads in the thread pool.
	 */
	public ThreadPool(int size) {
		threads = new WorkerThread[size];
		for (int i = 0; i < size; i++) {
			threads[i] = new WorkerThread(this);
			threads[i].start();
			date = new Date();
			System.out.println("[" + dateFormat.format(date) + "][thread ID: " + i + "]    ====>> starting");
		}
	}
	
	/**
	 * Add a job to the queue of tasks that has to be executed. As soon as a thread is available,
	 * it will retrieve tasks from this queue and start processing.
	 * 
	 * @param r job that has to be executed asynchronously
	 * @throws InterruptedException
	 */
	public void addToQueue(Runnable r) throws InterruptedException {
		synchronized (taskQueue) {
			taskQueue.add(r);
			taskQueue.notifyAll();
		}
	}
}


/**
 * The worker threads that make up the thread pool.
 */
class WorkerThread extends Thread {
	
	protected ThreadPool threadpool;
	protected static int jobCount = 0;
	
	/**
	 * @param o the thread pool
	 */
	WorkerThread(ThreadPool o) {
		this.threadpool = o;
	}
	
	/**
	 * Scan and execute tasks.
	 */
	public void run() {
		Runnable task;
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = null;
		while (true) {
			synchronized (threadpool.taskQueue) {
				while (threadpool.taskQueue.isEmpty()) {
					try {
						threadpool.taskQueue.wait();
					} catch (InterruptedException e) {}
				}
				
				task = threadpool.taskQueue.poll();
				
				try {
					int jobID = jobCount++;
					date = new Date();
					System.out.println("[" + dateFormat.format(date) + "][thread " + "]    ====>> job[" + jobID
							+ "] starting");
					task.run();
					date = new Date();
					System.out.println("[" + dateFormat.format(date) + "][thread " + "]    <<==== job[" + jobID
							+ "] finishing");
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		}
	}
	
}
