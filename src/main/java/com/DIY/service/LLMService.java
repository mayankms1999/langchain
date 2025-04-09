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

import java.util.List;

@Service
public class LLMService {

    private final ChatMemory chatMemory;
    private final ChatLanguageModel gemini;
    private final MessageParserService parserService;

    public LLMService(@Value("${google.api.key}") String apiKey, MessageParserService parserService) {
        this.parserService = parserService;
        this.chatMemory = TokenWindowChatMemory.withMaxTokens(1000, new Tokenizer() {
            @Override
            public int estimateTokenCountInText(String s) {
                return 0;
            }

            @Override
            public int estimateTokenCountInMessage(ChatMessage chatMessage) {
                return 0;
            }

            @Override
            public int estimateTokenCountInMessages(Iterable<ChatMessage> iterable) {
                return 0;
            }
        }); // same
        this.gemini = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.0-flash")
                .build();
    }

    public String chatWithMemory(String prompt) {
        chatMemory.add(new UserMessage(prompt));
        List<ChatMessage> messages = chatMemory.messages();
        String response = String.valueOf(gemini.chat(messages));
        chatMemory.add(new AiMessage(response));

        parserService.parseAndGenerate(response); // 🧠 Parse response!

        return response;
    }
}
