import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurentHashMapForkRepository implements ForkRepository {

    private ConcurrentMap<Fork, Boolean> takenForks = new ConcurrentHashMap<>();


    @Override
    public long eat(Fork leftFork, Fork rightFork, long time, boolean takeRightForkFirst) {
        long startTime = 0;
        try {
            if (takeRightForkFirst) {
                getForks(rightFork, leftFork, time / 2);
            } else {
                getForks(leftFork, rightFork, time / 2);
            }
            startTime = System.currentTimeMillis();
            LOGGER.info(leftFork + " and " + rightFork + " taken");
            Thread.sleep(time);
        } catch (InterruptedException e) {
           LOGGER.error(e.getMessage(),e);
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


    public void getForks(Fork firstFork, Fork secondFork, long time) throws InterruptedException {
        if (takenForks.putIfAbsent(firstFork, true) == null) {
            while (takenForks.putIfAbsent(secondFork, true) == null) {
            }
        } else {
            Thread.sleep(time);
            getForks(firstFork, secondFork, time);
        }
    }
}
