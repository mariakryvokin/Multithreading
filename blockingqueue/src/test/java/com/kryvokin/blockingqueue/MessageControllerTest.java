package com.kryvokin.blockingqueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static final Logger LOGGER = LogManager.getLogger();


    private List<String> messages = new ArrayList<>(Arrays.asList("fist", "second", "third", "fourth"));

    @Test
    void put() throws Exception {
        for (int i = 0; i < MessageController.CAPACITY; i++) {
            LOGGER.info(mockMvc.perform(MockMvcRequestBuilders.put("/message")
                    .param("message", messages.get(i))).andReturn().getResponse().getContentAsString());
        }

    }

/*    @Test
    void get() throws Exception {
        List<String> retrievedMessages = new ArrayList<>();
        for (int i = 0; i < MessageController.CAPACITY; i++) {
            retrievedMessages.add(
                    mockMvc.perform(MockMvcRequestBuilders.get("/message"))
                            .andReturn().getResponse().getContentAsString());
        }
        LOGGER.info(retrievedMessages);
    }*/


}