package philosophersproblem;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurentHashMapForkRepository implements ForkRepository {

    private ConcurrentMap<Fork, Boolean> takenForks = new ConcurrentHashMap<>();


    @Override
    public long eat(Fork leftFork, Fork rightFork, long eatTime) {
        long startTime = 0;
        try {
            if (!getForks(leftFork, rightFork, eatTime / 2)) {
                getForks(leftFork, rightFork, eatTime / 2);
            }
            startTime = System.currentTimeMillis();
            LOGGER.info(leftFork + " and " + rightFork + " taken");
            Thread.sleep(eatTime);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            releaseForks(leftFork, rightFork);
        }
        return startTime;
    }

    private void releaseForks(Fork leftFork, Fork rightFork) {
        LOGGER.info(leftFork + " and " + rightFork + " released");
        takenForks.remove(rightFork, true);
        takenForks.remove(leftFork, true);
    }


    public boolean getForks(Fork firstFork, Fork secondFork, long waitingTime) throws InterruptedException {
        if (takenForks.putIfAbsent(firstFork, true) == null) {
            while (takenForks.putIfAbsent(secondFork, true) != null) {
                Thread.sleep(waitingTime);
            }
            return true;
        } else {
            return false;
        }
    }
}
