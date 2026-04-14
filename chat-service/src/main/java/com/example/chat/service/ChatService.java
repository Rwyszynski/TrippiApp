package com.example.chat.service;

import com.example.chat.entity.Message;
import com.example.chat.kafka.MessageEvent;
import com.example.chat.kafka.MessageProducer;
import com.example.chat.repository.ChatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final MessageProducer messageProducer;

    public Message sendMessage(Jwt jwt, Message message) {

        Long senderId = Long.valueOf(jwt.getSubject());

        message.setSenderId(senderId);
        message.setTimestamp(LocalDateTime.now());
        message.setIsRead(false);

        userService.getUser(message.getSenderId());
        userService.getUser(message.getReceiverId());
        Message saved = chatRepository.save(message);

        // EVENT
        MessageEvent event = new MessageEvent(
                saved.getSenderId(),
                saved.getReceiverId(),
                saved.getMessageText(),
                saved.getTimestamp()
        );
        messageProducer.sendMessageEvent(event);
        return saved;
    }

    public Message readMessages(Long id) {
        Message readMessage = chatRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));
        return readMessage;
    }

    public List<Message> getAllConversations() {
        List<Message> messages = (List<Message>) chatRepository.findAll();
        return messages;
    }

    public List<Message> getConversationWithUser(Long userId, Jwt jwt) {
        Long senderId = Long.valueOf(jwt.getSubject());
        return chatRepository.findConversation(userId, senderId);
    }

    public Message deleteMessage(Long id) {
        Message message = chatRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("Message not found"));
        chatRepository.delete(message);
        return message;
    }

    @Transactional
    public void markConversationAsRead(Long UserId, Jwt jwt) {
        Long senderId = Long.valueOf(jwt.getSubject());
        chatRepository.markMessagesAsRead(UserId, senderId);
    }
}
