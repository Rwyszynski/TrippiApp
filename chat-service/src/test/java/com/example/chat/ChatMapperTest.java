package com.example.chat;

import com.example.chat.entity.Message;
import com.example.chat.entity.dto.MessageDto;
import com.example.chat.mapper.ChatMapper;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class ChatMapperTest {

    private final ChatMapper mapper = new ChatMapper();

    @Test
    void shouldMapMessageToDto() {
        Message message = new Message("hello", 1L, 2L, null, false);

        MessageDto dto = mapper.mapToMessageDto(message);

        assertThat(dto.messageText()).isEqualTo("hello");
        assertThat(dto.senderId()).isEqualTo(1L);
        assertThat(dto.receiverId()).isEqualTo(2L);
    }

    @Test
    void shouldMapEmptyListToEmptyDtoList() {
        List<MessageDto> result = mapper.mapToMessageListDto(List.of());
        assertThat(result).isEmpty();
    }

    @Test
    void shouldMapMessageListToDtoList() {
        List<Message> messages = List.of(
                new Message("hello", 1L, 2L, null, false),
                new Message("world", 2L, 1L, null, false)
        );

        List<MessageDto> result = mapper.mapToMessageListDto(messages);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).messageText()).isEqualTo("hello");
        assertThat(result.get(1).senderId()).isEqualTo(2L);
    }
}
