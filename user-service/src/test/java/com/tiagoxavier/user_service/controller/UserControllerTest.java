package com.tiagoxavier.user_service.controller;

import com.tiagoxavier.user_service.dto.UserResponse;
import com.tiagoxavier.user_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void deveCriarUsuario() throws Exception {

        UserResponse response = new UserResponse(1L, "Tiago", "tiago@email.com", null);

        when(userService.createUser(any()))
                .thenReturn(response);

        //Text Block
        String json = """
                {
                    "name": "Tiago",
                    "email": "tiago@email.com",
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated()).andExpect(jsonPath("$.name").value("Tiago")).andExpect(jsonPath("$.email").value("tiago@email.com"));
    }
}