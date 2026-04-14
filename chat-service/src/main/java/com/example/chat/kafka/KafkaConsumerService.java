package com.example.chat.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "chat-messages", groupId = "chat-group")
    public void listen(String message) {
        try {
            MessageEvent event = objectMapper.readValue(message, MessageEvent.class);

            // wysyłka do konkretnego usera
            messagingTemplate.convertAndSendToUser(
                    event.receiverId().toString(),
                    "/queue/messages",
                    event
            );
            System.out.println("WS SEND → user=" + event.receiverId());

        } catch (Exception e) {
            throw new RuntimeException("Kafka consume failed", e);
        }
    }
}
