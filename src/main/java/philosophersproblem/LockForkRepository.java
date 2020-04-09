package philosophersproblem;

public class LockForkRepository implements ForkRepository {

    @Override
    public long eat(Fork leftFork, Fork rightFork, long eatTime) {
        long startTime = 0;
        try {
            getForks(leftFork, rightFork);
            startTime = System.currentTimeMillis();
            LOGGER.info(leftFork + " and " + rightFork + " taken");
            Thread.sleep(eatTime);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            LOGGER.info(leftFork + " and " + rightFork + " released");
            releaseForks(leftFork, rightFork);
        }
        return startTime;
    }

    private void releaseForks(Fork leftFork, Fork rightFork) {
        rightFork.getLock().unlock();
        leftFork.getLock().unlock();
    }

    private void getForks(Fork firstFork, Fork secondFork) {
        firstFork.getLock().lock();
        secondFork.getLock().lock();
    }
}
