package com.example.chat.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessageEvent(MessageEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("chat-messages", json);
        } catch (Exception e) {
            throw new RuntimeException("Kafka send failed", e);
        }
    }
}
