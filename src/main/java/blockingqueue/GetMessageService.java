package blockingqueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class GetMessageService {

    private BlockingQueue<Message> messagePool;
    private static final Logger LOGGER = LogManager.getLogger();

    public GetMessageService(BlockingQueue<Message> messagePool) {
        this.messagePool = messagePool;
    }

    public Optional<Message> getMessage(){
        try {
            return Optional.ofNullable(messagePool.poll(30,TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(),e);
            return Optional.empty();
        }
    }
}
