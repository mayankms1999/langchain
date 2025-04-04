//package com.DIY.controller;
//
//import com.DIY.service.ChatService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/chat")
//public class ChatController {
//
////    private final ChatService chatService;
//
//    public ChatController(ChatService chatService) {
//        this.chatService = chatService;
//    }
//
//    @PostMapping
//    public ResponseEntity<String> chat(@RequestBody String message) {
//        String response = chatService.chat(message);
//        return ResponseEntity.ok(response);
//    }
//}
//
