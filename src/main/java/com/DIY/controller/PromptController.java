package com.DIY.controller;

import com.DIY.dto.PromptRequest;
import com.DIY.dto.PromptResponse;
import com.DIY.dto.ParseRequest;
import com.DIY.service.LLMService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prompt")
@CrossOrigin(origins = "http://localhost:5174")
public class PromptController {

    private final LLMService llmService;

    public PromptController(LLMService llmService) {
        this.llmService = llmService;
    }


    @PostMapping
    public ResponseEntity<PromptResponse> sendPrompt(@RequestBody PromptRequest request) {
        String llmResponse = llmService.chatWithMemory(request.getPrompt());
        PromptResponse response = new PromptResponse(llmResponse);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/parse-and-zip")
    public void parseAndZip(@RequestBody ParseRequest request, HttpServletResponse response) {
        llmService.parseAndZipToResponse(request.getCode(), response);
    }
}
