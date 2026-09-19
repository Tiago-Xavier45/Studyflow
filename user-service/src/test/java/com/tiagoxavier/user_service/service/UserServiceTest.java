package com.tiagoxavier.user_service.service;

import com.tiagoxavier.user_service.dto.UserRequest;
import com.tiagoxavier.user_service.dto.UserResponse;
import com.tiagoxavier.user_service.entity.User;
import com.tiagoxavier.user_service.service.UserService;
import com.tiagoxavier.user_service.repository.UserRepository;
import com.tiagoxavier.user_service.exception.EmailAlreadyExistsException;
import com.tiagoxavier.user_service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void CriarUsuario() {

        UserRequest request = new UserRequest();
        request.setName("Tiago");
        request.setEmail("tiago@email.com");
        request.setPassword("123456");

        User user = new User();
        user.setName("Tiago");
        user.setEmail("tiago@email.com");
        user.setPassword("123456");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("Tiago", response.getName());
        assertEquals("tiago@email.com", response.getEmail());

        verify(userRepository).existsByEmail("tiago@email.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void VerificarEmailDuplicado() {

        UserRequest request = new UserRequest();
        request.setName("Tiago");
        request.setEmail("tiago@email.com");
        request.setPassword("123456");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(request));

        verify(userRepository).existsByEmail("tiago@email.com");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deveBuscarUsuarioPorId() {

        User user = new User();
        user.setName("Tiago");
        user.setEmail("tiago@email.com");
        user.setPassword("123456");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals("Tiago", response.getName());
        assertEquals("tiago@email.com", response.getEmail());

        verify(userRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(999L));
        verify(userRepository).findById(999L);
    }

    @Test
    void deveExcluirUsuario() {

        User user = new User();
        user.setName("Tiago");
        user.setEmail("tiago@email.com");
        user.setPassword("123456");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.deleteUser(1L);
        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void TentarExcluirUsuarioNaoExistente() {

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(999L));

        verify(userRepository).findById(999L);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void deveAtualizarUsuario() {

        User user = new User();
        user.setName("Tiago");
        user.setEmail("tiago@email.com");
        user.setPassword("123456");

        UserRequest request = new UserRequest();
        request.setName("Tiago Xavier");
        request.setEmail("tiagoxavier@email.com");
        request.setPassword("654321");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.updateUser(1L, request);

        assertNotNull(response);
        assertEquals("Tiago Xavier", response.getName());
        assertEquals("tiagoxavier@email.com", response.getEmail());

        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void TentarAtualziarUsuarioNaoExistente() {

        UserRequest request = new UserRequest();
        request.setName("Tiago Xavier");
        request.setEmail("tiagoxavier@email.com");
        request.setPassword("654321");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(999L, request));
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void naoDeveAtualizarUsuarioComEmailDuplicado() {

        User user = new User();
        user.setName("Tiago");
        user.setEmail("tiago@email.com");
        user.setPassword("123456");

        UserRequest request = new UserRequest();
        request.setName("Tiago Xavier");
        request.setEmail("outro@email.com");
        request.setPassword("654321");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.updateUser(1L, request));

        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("outro@email.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deveBuscarTodosUsuarios() {

        User user1 = new User();
        user1.setName("Tiago");
        user1.setEmail("tiago@email.com");
        user1.setPassword("123456");

        User user2 = new User();
        user2.setName("Felipe");
        user2.setEmail("Felipe@email.com");
        user2.setPassword("123456");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponse> response = userService.getAllUsers();

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals("Tiago", response.get(0).getName());
        assertEquals("tiago@email.com", response.get(0).getEmail());

        assertEquals("Felipe", response.get(1).getName());
        assertEquals("Felipe@email.com", response.get(1).getEmail());

        verify(userRepository).findAll();
    }
    
}