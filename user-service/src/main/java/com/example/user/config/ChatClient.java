package com.example.user.config;

import com.example.user.entity.dto.ChatResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "chat-service",
        url = "${services.chat-service.url}"
)
public interface ChatClient {

    @GetMapping("/v1/messages/{id}")
    ChatResponseDto getChat(@PathVariable("id") Long id);
}
