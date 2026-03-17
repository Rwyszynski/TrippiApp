package com.example.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/messages")
@RestController
public class ChatController {

    @PostMapping("/messages")
    public ResponseEntity<MessageDto> sendMessage(SendMessageRequest request) {
        // Logic to send a message
        return ResponseEntity.ok(new MessageDto());
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDto>> getConversations() {
        // Logic to get conversations for the authenticated user
        return ResponseEntity.ok(List.of(new ConversationDto()));
    }

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<ConversationDto> getConversationWithUser(String userId) {
        // Logic to get conversation with a specific user
        return ResponseEntity.ok(new ConversationDto());
    }

    @PutMapping("/messages/{id}/read")
    public ResponseEntity<ResponseDto> markMessageAsRead(String id) {
        // Logic to mark a message as read
        return ResponseEntity.ok(new ResponseDto("Message marked as read"));
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<ResponseDto> deleteMessage(String id) {
        // Logic to delete a message
        return ResponseEntity.ok(new ResponseDto("Message deleted"));
    }
}
