package philosophersproblem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Philosopher implements Runnable {

    private int id;
    private int amountOfEatTalkCircles = 4;
    private boolean deadlockBreaker;
    private long eatTime;
    private long talkTime;
    private Fork leftFork;
    private Fork rightFork;
    private ForkRepository forkRepository;

    private long hungryTime = 0;

    private static final Logger LOGGER = LogManager.getLogger();

    public Philosopher(int id, boolean deadlockBreaker, long eatTime, long talkTime, Fork leftFork, Fork rightFork, ForkRepository forkRepository) {
        this.id = id;
        this.deadlockBreaker = deadlockBreaker;
        this.eatTime = eatTime;
        this.talkTime = talkTime;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.forkRepository = forkRepository;
    }

    public void eat(){
        LOGGER.info("{} going to eat", this);
        long startOfGrabingTime = System.currentTimeMillis();
        if (deadlockBreaker) {
            Fork swap = leftFork;
            leftFork = rightFork;
            rightFork = swap;
        }
        long startOfEatingTime = forkRepository.eat(leftFork, rightFork, eatTime);
        hungryTime += startOfEatingTime - startOfGrabingTime;
        LOGGER.info("{} ends eat", this);
    }

    public void talk(){
        LOGGER.info("{} starts talking", this);
        try {
            Thread.sleep(talkTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("{} ends talking",this);
    }

    public void setAmountOfEatTalkCircles(int amountOfEatTalkCircles) {
        this.amountOfEatTalkCircles = amountOfEatTalkCircles;
    }

    @Override
    public String toString() {
        return "Philosopher{" +
                "id=" + id +
                '}';
    }

    public void run() {
        for (int i = 0; i < amountOfEatTalkCircles;  i++){
            eat();
            talk();
        }
        LOGGER.info("Total hungry milliseconds {}",  hungryTime);
    }
}
