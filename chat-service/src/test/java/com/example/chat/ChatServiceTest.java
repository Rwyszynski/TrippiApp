package com.example.chat;

import com.example.chat.entity.Message;
import com.example.chat.kafka.MessageEvent;
import com.example.chat.kafka.MessageProducer;
import com.example.chat.repository.ChatRepository;
import com.example.chat.service.ChatService;
import com.example.chat.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserService userService;

    @Mock
    private MessageProducer messageProducer;

    @InjectMocks
    private ChatService chatService;

    private Jwt jwt;

    @BeforeEach
    void setUp() {
        jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "1")
                .build();
    }

    @Test
    void shouldSendMessage() {
        Message message = new Message("hello", 2L);
        Message saved = new Message("hello", 1L, 2L, null, false);
        saved.setId(1L);

        when(chatRepository.save(any())).thenReturn(saved);

        Message result = chatService.sendMessage(jwt, message);

        assertThat(result.getMessageText()).isEqualTo("hello");
        assertThat(result.getSenderId()).isEqualTo(1L);
        verify(messageProducer).sendMessageEvent(any(MessageEvent.class));
    }

    @Test
    void shouldGetConversationWithUser() {
        List<Message> messages = List.of(new Message("hi", 1L, 2L, null, false));
        when(chatRepository.findConversation(1L, 2L)).thenReturn(messages);

        List<Message> result = chatService.getConversationWithUser(2L, jwt);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessageText()).isEqualTo("hi");
    }

    @Test
    void shouldThrowWhenMessageNotFound() {
        when(chatRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chatService.readMessages(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Message not found");
    }

    @Test
    void shouldDeleteMessage() {
        Message message = new Message("bye", 1L, 2L, null, false);
        message.setId(1L);
        when(chatRepository.findById(1L)).thenReturn(Optional.of(message));

        Message result = chatService.deleteMessage(1L);

        assertThat(result.getMessageText()).isEqualTo("bye");
        verify(chatRepository).delete(message);
    }

    @Test
    void shouldGetAllConversations() {
        List<Message> messages = List.of(
                new Message("a", 1L, 2L, null, false),
                new Message("b", 2L, 1L, null, false)
        );
        when(chatRepository.findAll()).thenReturn(messages);

        List<Message> result = chatService.getAllConversations();

        assertThat(result).hasSize(2);
    }
}
