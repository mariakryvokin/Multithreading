package philosophersproblem;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Fork {

    private int numberOfFork;
    private Semaphore semaphore = new Semaphore(1);
    private Lock lock = new ReentrantLock(true);

    public Fork(int numberOfFork) {
        this.numberOfFork = numberOfFork;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public Lock getLock() {
        return lock;
    }

    @Override
    public String toString() {
        return " Fork{" +
                "numberOfFork=" + numberOfFork +
                '}';
    }

}
