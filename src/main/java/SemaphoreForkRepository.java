public class SemaphoreForkRepository implements ForkRepository {

    @Override
    public long eat(Fork leftFork, Fork rightFork, long time, boolean takeRightForkFirst) {
        long startTime = 0;
        try {
            if (takeRightForkFirst) {
                lockForks(rightFork, leftFork);
            } else {
                lockForks(leftFork, rightFork);
            }
            startTime = System.currentTimeMillis();
            LOGGER.info(leftFork + " and " + rightFork + " taken");
            Thread.sleep(time);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            LOGGER.info(leftFork + " and " + rightFork + " released");
            releaseForks(leftFork, rightFork);
        }
        return startTime;
    }

    private void releaseForks(Fork leftFork, Fork rightFork) {
        leftFork.getSemaphore().release();
        rightFork.getSemaphore().release();
    }

    private void lockForks(Fork firstFork, Fork secondFork) throws InterruptedException {
        firstFork.getSemaphore().acquire(1);
        secondFork.getSemaphore().acquire(1);
    }
}
