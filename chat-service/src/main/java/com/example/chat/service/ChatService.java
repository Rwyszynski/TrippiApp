package com.example.chat.service;

import com.example.chat.entity.Message;
import com.example.chat.repository.ChatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;

    public Message sendMessage(Message message) {

        message.setSenderId(1L);
        message.setReceiverId(2L);
        message.setTimestamp(LocalDateTime.now());
        message.setIsRead(false);

        userService.getUser(message.getSenderId());
        userService.getUser(message.getReceiverId());

        return chatRepository.save(message);
    }

    public Message readMessages(Long id) {
        Message readMessage = chatRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));
        return readMessage;
    }

    public List<Message> getAllConversations() {
        List<Message> messages = (List<Message>) chatRepository.findAll();
        return messages;
    }

    public List<Message> getConversationWithUser(Long userId, Long currentUserId) {
        return chatRepository.findConversation(currentUserId, userId);
    }

    public Message deleteMessage(Long id) {
        Message message = chatRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("Message not found"));
        chatRepository.delete(message);
        return message;
    }

    @Transactional
    public void markConversationAsRead(Long currentUserId, Long userId) {
        chatRepository.markMessagesAsRead(currentUserId, userId);
    }

}
