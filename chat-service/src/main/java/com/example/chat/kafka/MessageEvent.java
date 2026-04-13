package com.example.chat.kafka;

import java.time.LocalDateTime;

public record MessageEvent(
        Long senderId,
        Long receiverId,
        String messageText,
        LocalDateTime timestamp
) {}
