package com.DIY.controller;

import com.DIY.dto.PromptRequest;
import com.DIY.service.LLMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/prompt")
public class PromptController {

    private final LLMService llmService;

    public PromptController(LLMService llmService) {
        this.llmService = llmService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> sendPrompt(@RequestBody PromptRequest request) {
        String response = llmService.chatWithMemory(request.getPrompt());

        Map<String, String> result = new HashMap<>();
        result.put("result", response); // key should match what your frontend expects

        return ResponseEntity.ok(result);
    }
}
