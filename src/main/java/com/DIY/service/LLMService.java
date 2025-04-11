package com.DIY.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LLMService {

    private final ChatMemory chatMemory;
    private final ChatLanguageModel gemini;
    private final MessageParserService parserService;

    public LLMService(@Value("${google.api.key}") String apiKey, MessageParserService parserService) {
        this.parserService = parserService;
        this.chatMemory = TokenWindowChatMemory.withMaxTokens(1000, new Tokenizer() {
            @Override public int estimateTokenCountInText(String s) { return 0; }
            @Override public int estimateTokenCountInMessage(ChatMessage chatMessage) { return 0; }
            @Override public int estimateTokenCountInMessages(Iterable<ChatMessage> iterable) { return 0; }
        });
        this.gemini = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.0-flash")
                .build();
    }

    public Map<String, String> chatWithMemory(String prompt) {
        // Step 1: Extract app name from the prompt
        String appName = extractAppNameFromPrompt(prompt);

        // Step 2: Interact with Gemini
        chatMemory.add(new UserMessage(prompt));
        List<ChatMessage> messages = chatMemory.messages();
        String response = String.valueOf(gemini.chat(messages));
        chatMemory.add(new AiMessage(response));

        // Step 3: Generate files/folders and get the folder name
        String folderName = parserService.parseAndGenerate(response, appName);

        // Step 4: Return results
        Map<String, String> result = new HashMap<>();
        result.put("result", response);
        result.put("folderName", folderName);
        result.put("appName", appName);
        return result;
    }

    private String extractAppNameFromPrompt(String prompt) {
        String defaultName = "MyApp";
        try {
            Pattern pattern = Pattern.compile("<appName>(.*?)</appName>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(prompt);
            if (matcher.find()) {
                return matcher.group(1).trim().toLowerCase().replaceAll("[^a-z0-9]", "");
            }
        } catch (Exception ignored) {}
        return defaultName;
    }
}
