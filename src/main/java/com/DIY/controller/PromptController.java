package com.DIY.controller;

import com.DIY.dto.PromptRequest;
import com.DIY.service.LLMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Map<String, String> result = llmService.chatWithMemory(request.getPrompt());
        return ResponseEntity.ok(result);
    }
}
