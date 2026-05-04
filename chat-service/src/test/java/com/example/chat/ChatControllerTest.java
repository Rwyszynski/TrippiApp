package com.example.chat;

import com.example.chat.config.TestConfig;
import com.example.chat.controller.ChatController;
import com.example.chat.entity.Message;
import com.example.chat.entity.dto.ConversationDto;
import com.example.chat.entity.dto.MessageDto;
import com.example.chat.mapper.ChatMapper;
import com.example.chat.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
@Import(TestConfig.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @MockBean
    private ChatMapper chatMapper;

    @Test
    void shouldSendMessage() throws Exception {
        Message saved = new Message("hello", 1L, 2L, null, false);
        saved.setId(1L);
        when(chatService.sendMessage(any(Jwt.class), any(Message.class))).thenReturn(saved);

        mockMvc.perform(post("/v1/messages/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"messageText":"hello","receiverId":2}
                                """)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(j -> j.subject("1"))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetConversationWithUser() throws Exception {
        List<Message> messages = List.of(new Message("hi", 1L, 2L, null, false));
        List<MessageDto> dtos = List.of(new MessageDto("hi", 1L, 2L));

        when(chatService.getConversationWithUser(eq(2L), any(Jwt.class))).thenReturn(messages);
        when(chatMapper.mapToMessageListDto(messages)).thenReturn(dtos);

        mockMvc.perform(get("/v1/messages/conversations/get/2/")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(j -> j.subject("1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages[0].messageText").value("hi"))
                .andExpect(jsonPath("$.messages[0].senderId").value(1))
                .andExpect(jsonPath("$.messages[0].receiverId").value(2));
    }

    @Test
    void shouldGetAllConversations() throws Exception {
        List<Message> messages = List.of(new Message("test", 1L, 2L, null, false));
        List<MessageDto> dtos = List.of(new MessageDto("test", 1L, 2L));

        when(chatService.getAllConversations()).thenReturn(messages);
        when(chatMapper.mapToMessageListDto(messages)).thenReturn(dtos);

        mockMvc.perform(get("/v1/messages/conversations")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(j -> j.subject("1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages[0].messageText").value("test"));
    }

    @Test
    void shouldDeleteMessage() throws Exception {
        Message message = new Message("bye", 1L, 2L, null, false);
        message.setId(1L);
        when(chatService.deleteMessage(1L)).thenReturn(message);

        mockMvc.perform(delete("/v1/messages/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(j -> j.subject("1"))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldMarkConversationAsRead() throws Exception {
        mockMvc.perform(put("/v1/messages/conversations/read/2")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(j -> j.subject("1"))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/v1/messages/conversations"))
                .andExpect(status().isUnauthorized());
    }
}
