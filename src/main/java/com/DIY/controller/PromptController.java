package com.DIY.controller;

import com.DIY.dto.PromptRequest;
import com.DIY.service.LLMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prompt")
public class PromptController {

    private final LLMService llmService;

    public PromptController(LLMService llmService) {
        this.llmService = llmService;
    }

    @PostMapping
    public ResponseEntity<String> sendPrompt(@RequestBody PromptRequest request) {
        String structuredResponse = llmService.chatLlm(request.getPrompt());
        return ResponseEntity.ok(structuredResponse);
    }
}

