package philosophersproblem;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurentHashMapForkRepository implements ForkRepository {

    private ConcurrentMap<Fork, Boolean> takenForks = new ConcurrentHashMap<>();


    @Override
    public long eat(Fork leftFork, Fork rightFork, long eatTime) {
        long startTime = 0;
        try {
            boolean needForks = true;
            while (needForks) {
                needForks = !getForks(leftFork, rightFork, eatTime / 2);
                if (needForks) {
                    Fork swap = leftFork;
                    leftFork = rightFork;
                    rightFork = swap;
                }
            }
            startTime = System.currentTimeMillis();
            LOGGER.info("{} and {} taken", leftFork, rightFork);
            Thread.sleep(eatTime);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            releaseForks(leftFork, rightFork);
        }
        return startTime;
    }

    private void releaseForks(Fork leftFork, Fork rightFork) {
        LOGGER.info("{} and {} released", leftFork, rightFork);
        takenForks.remove(rightFork, true);
        takenForks.remove(leftFork, true);
    }


    private boolean getForks(Fork firstFork, Fork secondFork, long waitingTime) throws InterruptedException {
        if (takenForks.putIfAbsent(firstFork, true) == null) {
            while (takenForks.putIfAbsent(secondFork, true) != null) {
                Thread.sleep(waitingTime);
            }
            return true;
        } else {
            Thread.sleep(waitingTime);
            return false;
        }
    }
}
