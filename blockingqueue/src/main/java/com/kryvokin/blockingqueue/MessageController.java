package com.kryvokin.blockingqueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@RestController
public class MessageController {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final int CAPACITY = 3;
    BlockingQueue<String> messagePool = new ArrayBlockingQueue<>(CAPACITY);

    @GetMapping("/message")
    public String get() {
        try {
            Optional<String> message = Optional.ofNullable(messagePool.poll(30, TimeUnit.SECONDS));
            if (message.isPresent()) {
                String resultMessage = message.get();
                LOGGER.info("get message {}", resultMessage);
                return resultMessage;
            }
        } catch (InterruptedException e) {
            LOGGER.info(e.getMessage());
        }
        return "Empty queue";
    }

    @PutMapping("/message")
    public boolean put(@RequestParam("message") String message) {
        try {
            LOGGER.info("put message {}", message);
            return messagePool.offer(message, 30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

}
