package blockingqueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class Main {
    private static final int CAPACITY = 3;
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        BlockingQueue<Message> messagePool = new ArrayBlockingQueue<>(CAPACITY);
        PutMessageService putMessageService = new PutMessageService(messagePool);
        GetMessageService getMessageService = new GetMessageService(messagePool);

        List<Message> messages = new ArrayList<>(Arrays.asList(
                new Message("First message"),new Message("Second message"), new Message("Third message"),
                new Message("Fourth message")));
        LOGGER.info("messagePool isEmpty {}",messagePool.isEmpty());
        CompletableFuture.runAsync(() -> {
                            for (Message message: messages ) {
                                LOGGER.info("put {}", message);
                                LOGGER.info(putMessageService.putMessage(message));
                            }
                });

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < messages.size(); i++){
               Optional<Message> message = getMessageService.getMessage();
               if(message.isPresent()){
                   LOGGER.info("get {}", message.get());
               }
           }
        }, executorService).thenRun(() -> executorService.shutdown());
    }
}
