/**
 * A simple custom lock that allows simultaneously read operations, but
 * disallows simultaneously write and read/write operations.
 * 
 * You do not need to implement any form or priority to read or write
 * operations. The first thread that acquires the appropriate lock should be
 * allowed to continue.
 * 
 * @author CS 212 Software Development
 * @author Diana Ly
 * @author University of San Francisco
 */
public class MultiReaderLock {

	private int writer;
	private int reader;

	/**
	 * Initializes a multi-reader (single-writer) lock.
	 */
	public MultiReaderLock() {
		writer = 0;
		reader = 0;
	}

	/**
	 * Will wait until there are no active writers in the system, and then will
	 * increase the number of active readers.
	 */
	public synchronized void lockRead() {
		while (writer > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("Write error.");
			}
		}
		reader++;
	}

	/**
	 * Will decrease the number of active readers, and notify any waiting
	 * threads if necessary.
	 */
	public synchronized void unlockRead() {
		reader--;

		if (reader == 0) {
			this.notifyAll();
		}

	}

	/**
	 * Will wait until there are no active readers or writers in the system, and
	 * then will increase the number of active writers.
	 */
	public synchronized void lockWrite() {
		while (reader > 0 || writer > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("Error.");
			}
		}
		writer++;
	}

	/**
	 * Will decrease the number of active writers, and notify any waiting
	 * threads if necessary.
	 */
	public synchronized void unlockWrite() {
		writer--;
		this.notifyAll();
	}
}