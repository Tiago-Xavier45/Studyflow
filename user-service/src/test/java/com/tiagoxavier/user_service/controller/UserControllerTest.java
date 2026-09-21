package com.tiagoxavier.user_service.controller;

import java.util.List;

import com.tiagoxavier.user_service.dto.UserResponse;
import com.tiagoxavier.user_service.service.UserService;
import com.tiagoxavier.user_service.exception.ResourceNotFoundException;
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
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.Mockito.doThrow;

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

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status()
                        .isCreated())
                .andExpect(jsonPath("$.name")
                        .value("Tiago"))
                .andExpect(jsonPath("$.email")
                .value("tiago@email.com"));
    }

    @Test
    void deveBuscarTodosOsUsuarios() throws Exception {

        UserResponse user1 = new UserResponse(1L, "Tiago", "tiago@email.com", null);

        UserResponse user2 = new UserResponse(2L, "Felipe", "Felipe@email.com", null);

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Tiago"))
                .andExpect(jsonPath("$[0].email").value("tiago@email.com"))
                .andExpect(jsonPath("$[1].name").value("Felipe"))
                .andExpect(jsonPath("$[1].email").value("Felipe@email.com"));

        verify(userService).getAllUsers();
    }

    @Test
    void BuscarUsuariosPeloId() throws  Exception{

        UserResponse response = new UserResponse(1L, "Tiago", "tiago@email.com", null);

        when(userService.getUserById(1L)).thenReturn(response);
        mockMvc.perform(get("/users/1")).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Tiago")).andExpect(jsonPath("$.email").value("tiago@email.com"));

        verify(userService).getUserById(1L);
    }

    @Test
    void Retornar404QuandoUsuarioNaoExistir() throws Exception {

        when(userService.getUserById(999L)).thenThrow(new ResourceNotFoundException("Usuário não encontrado"));

        mockMvc.perform(get("/users/999")).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Usuário não encontrado"));

        verify(userService).getUserById(999L);
    }

    @Test
    void AtualizarUsuario() throws Exception {

        UserResponse response = new UserResponse(1L, "Tiago Xavier", "tiagoxavier@email.com", null);

        when(userService.updateUser(org.mockito.ArgumentMatchers.eq(1L), any())).thenReturn(response);

        //Text block
        String json = """
            {
                "name": "Tiago Xavier",
                "email": "tiagoxavier@email.com",
                "password": "654321"
            }
            """;

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tiago Xavier"))
                .andExpect(jsonPath("$.email").value("tiagoxavier@email.com"));

        verify(userService).updateUser(org.mockito.ArgumentMatchers.eq(1L), any());
    }

    @Test
    void Retornar404AoAtualizarUsuarioInexistente() throws Exception {

        when(userService.updateUser(org.mockito.ArgumentMatchers.eq(999L), any())).thenThrow(new ResourceNotFoundException("Usuário não encontrado"));

        String json = """
            {
                "name": "Tiago Xavier",
                "email": "tiagoxavier@email.com",
                "password": "654321"
            }
            """;

        mockMvc.perform(put("/users/999").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Usuário não encontrado"));

        verify(userService).updateUser(org.mockito.ArgumentMatchers.eq(999L), any());
    }

    @Test
    void ExcluirUsuario() throws Exception {

        mockMvc.perform(delete("/users/1")).andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    void Retornar404AoExcluirUsuarioInexistente() throws Exception {

        doThrow(new ResourceNotFoundException("Usuário não encontrado")).when(userService).deleteUser(999L);

        mockMvc.perform(delete("/users/999")).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Usuário não encontrado"));

        verify(userService).deleteUser(999L);
    }

    @Test
    void Retornar400QuandoDadosForemInvalidos() throws Exception {

        String json = """
            {
                "name": "",
                "email": "email-invalido",
                "password": "123"
            }
            """;

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").exists());
    }

    @Test
    void Retornar400QuandoSenhaForMuitoCurta() throws Exception {

        String json = """
            {
                "name": "Tiago",
                "email": "tiago@email.com",
                "password": "123"
            }
            """;

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Senha deve conter no minímo 6 caracteres"));
    }
}