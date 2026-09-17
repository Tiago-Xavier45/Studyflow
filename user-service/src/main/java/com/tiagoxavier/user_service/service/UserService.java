package com.tiagoxavier.user_service.service;

import com.tiagoxavier.user_service.dto.UserRequest;
import com.tiagoxavier.user_service.dto.UserResponse;
import com.tiagoxavier.user_service.entity.User;
import com.tiagoxavier.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.tiagoxavier.user_service.exception.ResourceNotFoundException;
import com.tiagoxavier.user_service.exception.EmailAlreadyExistsException;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCreatedAt(LocalDateTime.now());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email já está cadastrado");
        }

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getCreatedAt());

    }

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt())).toList();
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException ("Usuário não encontrado"));

        userRepository.delete(user);
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException ("Usuário não encontrado"));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User updatedUser = userRepository.save(user);

        return new UserResponse(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(), updatedUser.getCreatedAt()  );
    }

}