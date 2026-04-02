package com.example.user.service;

import com.example.user.config.ChatClient;
import com.example.user.entity.dto.ChatResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public ChatResponseDto getChat(Long id) {
        return chatClient.getChat(id);
    }
}
