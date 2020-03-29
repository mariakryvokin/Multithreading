import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
     /*   int capacity = Integer.valueOf(args[0]);
        long eatTime = Long.valueOf(args[1]);
        long talkTime = Long.valueOf(args[2]);*/
        int capacity = 5;
        long eatTime = 2000;
        long talkTime = 1000;
        ForkRepository forkRepository = new SynchronizedForkRepository();
       // ForkRepository forkRepository = new ConcurentHashMapForkRepository();
        //ForkRepository forkRepository = new SemaphoreForkRepository();
        //  ForkRepository forkRepository = new LockForkRepository();
        List<Fork> forks = new ArrayList(capacity);
        for (int i = 0; i < capacity; i++) {
            forks.add(new Fork(i));
        }

        Philosopher first = new Philosopher(1, false, eatTime, talkTime, forks.get(0), forks.get(1), forkRepository);
        Philosopher second = new Philosopher(2, false, eatTime, talkTime, forks.get(1), forks.get(2), forkRepository);
        Philosopher third = new Philosopher(3, false, eatTime, talkTime, forks.get(2), forks.get(3), forkRepository);
        Philosopher forth = new Philosopher(4, false, eatTime, talkTime, forks.get(3), forks.get(4), forkRepository);
        Philosopher fifth = new Philosopher(5, true, eatTime, talkTime, forks.get(4), forks.get(0), forkRepository);

        Thread firstTread = new Thread(first);
        firstTread.start();
        Thread secondTread = new Thread(second);
        secondTread.start();
        Thread thirdTread = new Thread(third);
        thirdTread.start();
        Thread forthTread = new Thread(forth);
        forthTread.start();
        Thread fifthTread = new Thread(fifth);
        fifthTread.start();


    }
}
