package com.example.chat.mapper;

import com.example.chat.entity.Message;
import com.example.chat.entity.dto.ConversationDto;
import com.example.chat.entity.dto.MessageDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatMapper {

    public MessageDto mapToMessageDto(Message message) {
        return new MessageDto(message.getMessageText());
    }

     public List<MessageDto> mapToMessageListDto(List<Message> messages) {
        List<MessageDto> messageDtos = messages.stream()
                .map(this::mapToMessageDto)
                .toList();
        return messageDtos;
    }




}
