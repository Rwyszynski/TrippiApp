package com.example.chat;

import com.example.chat.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfig.class)
class ChatServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
