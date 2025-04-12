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
        });
        this.gemini = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.0-flash")
                .build();
    }

    public Map<String, String> chatWithMemory(String prompt) {

        String appName = extractAppNameFromPrompt(prompt);
        chatMemory.add(new UserMessage(prompt));
        List<ChatMessage> messages = chatMemory.messages();
        String response = String.valueOf(gemini.chat(messages));
        chatMemory.add(new AiMessage(response));
        String folderName = parserService.parseAndGenerate(response, appName);
        Map<String, String> result = new HashMap<>();
        result.put("result", response);
        result.put("folderName", folderName);
        result.put("appName", appName);
        return result;
    }

    private String extractAppNameFromPrompt(String prompt) {
        List<Pattern> patterns = Arrays.asList(
                Pattern.compile("<appName>(.*?)</appName>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
                Pattern.compile("(?i)(?:app name is|app name|appName|app_name)[:=\\s\"']*([a-zA-Z0-9_\\-\\s]+)"),
                Pattern.compile("(?i)create (?:an?|the)?\\s*([a-zA-Z0-9_\\-\\s]+)\\s*(?:app|application)"),
                Pattern.compile("(?i)(?:build|design|make|generate).{0,40}app(?:lication)?(?: for| like)? ([a-zA-Z0-9_\\-\\s]+)"),
                Pattern.compile("\"(?:appName|name)\"\\s*:\\s*\"([a-zA-Z0-9_\\-\\s]+)\""),
                Pattern.compile("(?i)(?:appName|app_name|name)\\s*=\\s*([a-zA-Z0-9_\\-\\s]+)"),
                Pattern.compile("\\b([A-Z][a-zA-Z0-9]{2,}App)\\b"),
                Pattern.compile("\\b([a-zA-Z0-9]+[-_][a-zA-Z0-9-_]+)\\b"),
                Pattern.compile("[-_\\s]+([a-zA-Z0-9]+(?:[-_\\s][a-zA-Z0-9]+)+)[-_\\s]+"),
                Pattern.compile("(?i)(?:called|named)\\s*['\"]?([a-zA-Z0-9_\\-\\s]+)['\"]?"),
                Pattern.compile("(?i)^([a-zA-Z]{2,}(?:\\s+[a-zA-Z]{2,}){1,9})\\s+(?:app|application|system)?\\b")
        );
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(prompt);
            if (matcher.find()) {
                String raw = matcher.group(matcher.groupCount());
                return sanitizeAppName(raw);
            }
        }
        return "MyApp";
    }

    private String sanitizeAppName(String rawName) {
        return rawName.trim()
                .replaceAll("[^a-zA-Z0-9 ]", "") // allow space
                .replaceAll("\\s{2,}", " ");     // collapse multiple spaces
    }

}