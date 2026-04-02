package com.example.user;

import com.example.user.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestConfig.class)
@SpringBootTest
class UserApplicationTests {

    @Test
    void contextLoads() {
    }

}
