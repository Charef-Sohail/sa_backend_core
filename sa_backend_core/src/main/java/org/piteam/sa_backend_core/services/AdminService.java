package org.piteam.sa_backend_core.services;

import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.dto.admin.AdminResponse;
import org.piteam.sa_backend_core.dto.admin.AdminUpdateUserRequest;
import org.piteam.sa_backend_core.exceptions.EmailAlreadyExistsException;
import org.piteam.sa_backend_core.exceptions.ResourceNotFoundException;
import org.piteam.sa_backend_core.models.User;
import org.piteam.sa_backend_core.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    private AdminResponse toResponse(User user) {
        return new AdminResponse(
                user.getCreatedAt(),
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public List<AdminResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public AdminResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return toResponse(user);
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public AdminResponse updateUser(String id, AdminUpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            if (userRepository.existsByEmail(request.getEmail())
                    && !request.getEmail().equals(user.getEmail())) {
                throw new EmailAlreadyExistsException("Email already in use: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        User updated = userRepository.save(user);
        return toResponse(updated);
    }

}
