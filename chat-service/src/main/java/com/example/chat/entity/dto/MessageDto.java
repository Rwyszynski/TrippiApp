package com.example.chat.entity.dto;

public record MessageDto(String messageText, Long senderId, Long receiverId) {
}
