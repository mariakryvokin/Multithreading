package blockingqueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class PutMessageService {

    private BlockingQueue<Message> messagePool;
    private static final Logger LOGGER = LogManager.getLogger();

    public PutMessageService(BlockingQueue<Message> messagePool) {
        this.messagePool = messagePool;
    }

    public boolean putMessage(Message message){
        try {
            return messagePool.offer(message,30,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(),e);
            return false;
        }
    }

}
