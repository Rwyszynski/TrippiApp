package com.example.chat.service;

import com.example.chat.entity.Message;
import com.example.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public Message sendMessage(Message message) {
        Message saveMessage =  chatRepository.save(
                new Message(message.getMessageText(),
                            message.getSenderId(),
                            message.getReceiverId(),
                            LocalDateTime.now(),
                            false));
        return saveMessage;
    }

    public Message readMessages(Long id) {
        Message readMessage = chatRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));
        return readMessage;
    }

    public List<Message> getAllConversations() {
        List<Message> messages = (List<Message>) chatRepository.findAll();
        return messages;
    }

    public List<Message> getConversationWithUser(String userId) {
        List<Message> messages = chatRepository.findByUserId(userId);
        return messages;
    }

    public List<Message> markMessageAsRead(Long id) {
        List<Message> messages = chatRepository.findById(id)
                .stream().peek(message -> message.setIsRead(true)).toList();
        chatRepository.saveAll(messages);
        return messages;
    }

    public Message deleteMessage(String id) {
        Message message = chatRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("Message not found"));
        chatRepository.delete(message);
        return message;
    }
}
