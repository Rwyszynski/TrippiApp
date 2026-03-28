package com.example.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "messages")
public class Message {

    public Message(String messageText) {
        this.messageText = messageText;
    }

    public Message(String messageText, Long senderId, Long receiverId, LocalDateTime timestamp, boolean b) {
        this.messageText = messageText;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
        this.isRead = b;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    private Long id;

    private Long senderId;

    private Long receiverId;

    private String messageText;

    private LocalDateTime timestamp;

    private Boolean isRead;

}
