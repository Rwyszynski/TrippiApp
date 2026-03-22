package com.example.chat.controller;

import com.example.chat.entity.Message;
import com.example.chat.entity.dto.*;
import com.example.chat.mapper.ChatMapper;
import com.example.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/messages")
@RestController
public class ChatController {

    private final ChatService chatService;
    private final ChatMapper chatMapper;

    @PostMapping("/messages")
    public ResponseEntity<MessageDto> sendMessage(SendMessageRequest request) {
        Message message = chatService.sendMessage(new Message(request.message()));
        return ResponseEntity.ok(new MessageDto(message.getMessageText()));
    }

    @GetMapping("/conversations")
    public ResponseEntity<ConversationDto> getAllConversations() {
        List<Message> messages = chatService.getAllConversations();
        return ResponseEntity.ok(new ConversationDto(chatMapper.mapToMessageListDto(messages)));
    }

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<ConversationDto> getConversationWithUser(String userId) {
        List<Message> messages = chatService.getConversationWithUser(userId);
        return ResponseEntity.ok(new ConversationDto(chatMapper.mapToMessageListDto(messages)));
    }

    @PutMapping("/messages/{id}/read")
    public ResponseEntity<ResponseReadDto> markMessageAsRead(Long id) {
        List<Message> messages = chatService.markMessageAsRead(id);
        return ResponseEntity.ok(new ResponseReadDto("Message marked as read"));
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<ResponseDeleteDto> deleteMessage(String id) {
        Message message = chatService.deleteMessage(id);
        return ResponseEntity.ok(new ResponseDeleteDto("Message deleted"));
    }
}
