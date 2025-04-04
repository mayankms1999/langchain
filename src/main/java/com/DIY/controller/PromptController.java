package com.DIY.controller;

import com.DIY.dto.PromptRequest;
import com.DIY.service.LLMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prompt")
public class PromptController {

    private final LLMService llmService;

    public PromptController(LLMService llmService) {
        this.llmService = llmService;
    }

    @PostMapping
    public ResponseEntity<String> sendPrompt(@RequestBody PromptRequest request) {
        String response = llmService.chatWithMemory(request.getPrompt());
        return ResponseEntity.ok(response);
    }
}