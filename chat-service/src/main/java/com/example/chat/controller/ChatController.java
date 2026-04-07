package com.example.chat.controller;

import com.example.chat.entity.Message;
import com.example.chat.entity.dto.*;
import com.example.chat.mapper.ChatMapper;
import com.example.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/messages")
@RestController
public class ChatController {

    private final ChatService chatService;
    private final ChatMapper chatMapper;

    @PostMapping("/")
    public ResponseEntity<SendMessageRequest> sendMessage(@RequestBody MessageDto request, @AuthenticationPrincipal Jwt jwt) {
        Message message = chatService.sendMessage(jwt, new Message(request.messageText(), request.receiverId()));
        return ResponseEntity.ok(
                new SendMessageRequest("Message " + message.getMessageText() + " has been sent")
        );
    }

    @GetMapping("/conversations")
    public ResponseEntity<ConversationDto> getAllConversations() {
        List<Message> messages = chatService.getAllConversations();
        return ResponseEntity.ok(new ConversationDto(chatMapper.mapToMessageListDto(messages)));
    }

    @GetMapping("/conversations/{userId}/{userId2}")
    public ResponseEntity<ConversationDto> getConversationWithUser(@PathVariable Long userId, Long userId2) {
        List<Message> messages = chatService.getConversationWithUser(userId, userId2);
        return ResponseEntity.ok(new ConversationDto(chatMapper.mapToMessageListDto(messages)));
    }

    @PutMapping("/conversations/{userId}/read")
    public ResponseEntity<?> markConversationAsRead(@PathVariable Long userId) {
        chatService.markConversationAsRead(1L, userId); // później JWT
        return ResponseEntity.ok("Conversation marked as read");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDeleteDto> deleteMessage(@PathVariable Long id) {
        chatService.deleteMessage(id);
        return ResponseEntity.ok(new ResponseDeleteDto("Message deleted"));
    }
}
