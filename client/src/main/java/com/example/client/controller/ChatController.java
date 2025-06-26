package com.example.client.controller;

import com.example.client.config.HeaderContext;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ToolCallbackProvider toolCallbackProvider;
    private final ChatClient.Builder chatClientBuilder;

    @Autowired
    public ChatController(ChatClient.Builder chatClientBuilder,
                          ToolCallbackProvider toolCallbackProvider) {
        this.chatClientBuilder = chatClientBuilder;
        this.toolCallbackProvider = toolCallbackProvider;
    }

    @PostConstruct
    public void init() {

        List<String> toolInfoList = Arrays.stream(toolCallbackProvider.getToolCallbacks())
                .map(tool -> String.format("%s - %s",
                        tool.getToolDefinition().name(),
                        tool.getToolDefinition().description()))
                .toList();
        logger.info("\nRegistered Tools:\n{}", String.join("\n", toolInfoList));

    }

    @PostMapping("/chat")
    public String chat(@RequestBody String userMessage,
                       @RequestHeader Map<String, String> headers) {

        try {
            // Set dynamic headers for current thread
            HeaderContext.set(headers);
            String response = chatClientBuilder.build()
                    .prompt(userMessage)
                    .system("Provide response in tabular form")
                    .toolCallbacks(toolCallbackProvider)
                    .call().content();

            logger.info("Proper response : " + response);

            return response;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            HeaderContext.clear(); // Cleanup to avoid header leaks across requests
        }
    }
}
