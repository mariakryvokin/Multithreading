package philosophersproblem;

public class SemaphoreForkRepository implements ForkRepository {

    @Override
    public long eat(Fork leftFork, Fork rightFork, long eatTime) {
        long startTime = 0;
        try {
            lockForks(leftFork, rightFork);
            startTime = System.currentTimeMillis();
            LOGGER.info("{} and {} taken", leftFork, rightFork);
            Thread.sleep(eatTime);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            LOGGER.info("{} and {} released", leftFork, rightFork);
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
