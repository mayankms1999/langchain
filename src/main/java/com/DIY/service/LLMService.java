package com.DIY.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class LLMService {
    ChatLanguageModel gemini = GoogleAiGeminiChatModel.builder()
            .apiKey("AIzaSyDiDoYLVgE6XyqMS6GBoWaTgvVc7tzlTg4")
            .modelName("gemini-2.0-flash")
            .build();


    public String chatLlm(String prompt) {
        String response = gemini.chat(prompt);
        return response;
    }
}

//    private final RestClient restClient;
//
//    @Value("${llm.mock-mode:false}")
//    private boolean mockMode;
//
//    public LLMService(RestClient restClient) {
//        this.restClient = restClient;
//    }
//
//    public String getLLMResponse(String prompt) {
//        if (mockMode) {
//            return getMockResponse(prompt);
//        }
//
//        try {
//            return restClient.post()
//                    .uri("/chat/completions")
//                    .body("""
//                        {
//                          "model": "deepseek-chat",
//                          "messages": [
//                            {"role": "user", "content": "%s"}
//                          ]
//                        }
//                        """.formatted(prompt))
//                    .retrieve()
//                    .body(String.class);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to call DeepSeek API", e);
//        }
//    }
//
//    private String getMockResponse(String prompt) {
//        return """
//        {
//          "prompt": "%s",
//          "structured_response": {
//            "type": "mock",
//            "data": "This is a mock response for testing"
//          }
//        }
//        """.formatted(prompt);
//    }

