package server;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DispatcherImpl extends DispatcherSkeletonE {	// impl. per ereditarieta'

	private int command = -1;

	private Lock lock;
	private Condition empty;
	private Condition full;

	private final int DIM = 5;

	private int [] buffer;
	private int elements;
	private int head;
	private int tail;
	
	public DispatcherImpl (int p){

		super (p);

		lock = new ReentrantLock();
		empty = lock.newCondition();
		full = lock.newCondition();

		buffer = new int[DIM];
		elements = 0;
		head = 0;
		tail = 0;
	}
	
	public void sendCmd ( int cmd ){

		lock.lock();

		while(elements == DIM){
			try {
				full.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		buffer[head] = cmd;
		head = (head + 1) % DIM;
		elements++;

		System.out.println ("		+ [DispImp] sendCmd: " + cmd );

		empty.signal();

		lock.unlock();

	}
	
	public int getCmd(){

		lock.lock();

		while(elements == 0){
			try {
				empty.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		this.command = buffer[tail];
		tail = (tail + 1) % DIM;
		elements--;

		System.out.println ("		+ [DispImp] getCmd: " + command );

		full.signal();

		lock.unlock();

		return this.command;
	}
	

}
