package com.bryan.springsecurity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@SpringBootTest
class SpringSecurityApplicationTests {

    @Test
    void testPasswordEncoders() {
        System.out.println(new BCryptPasswordEncoder().encode("password"));
        System.out.println(new BCryptPasswordEncoder().encode("password"));
        System.out.println(new Pbkdf2PasswordEncoder().encode("password"));
    }

}
