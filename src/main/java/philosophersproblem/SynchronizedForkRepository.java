package philosophersproblem;

public class SynchronizedForkRepository implements ForkRepository {

    @Override
    public long eat(Fork leftFork, Fork rightFork, long eatTime) {
        return takeForks(leftFork, rightFork, eatTime);
    }

    private long takeForks(Fork firstFork, Fork secondFork, long time) {
        synchronized (firstFork) {
            synchronized (secondFork) {
                long startTime = System.currentTimeMillis();
                try {
                    LOGGER.info("{} and {} taken", firstFork, secondFork);
                    Thread.sleep(time);
                    LOGGER.info("{} and {} released", firstFork, secondFork);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                return startTime;
            }
        }
    }
}


