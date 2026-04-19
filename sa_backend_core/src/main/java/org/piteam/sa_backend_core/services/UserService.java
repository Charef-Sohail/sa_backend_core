package org.piteam.sa_backend_core.services;

import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.dto.LoginRequest;
import org.piteam.sa_backend_core.dto.LoginResponse;
import org.piteam.sa_backend_core.dto.RegisterRequest;
import org.piteam.sa_backend_core.dto.RegisterResponse;
import org.piteam.sa_backend_core.exceptions.EmailAlreadyExistsException;
import org.piteam.sa_backend_core.models.Student;
import org.piteam.sa_backend_core.models.User;
import org.piteam.sa_backend_core.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: " + request.getEmail());
        }

        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        student.setCreatedAt(Instant.now());

        User saved = userRepository.save(student);

        return new RegisterResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getRole()
        );
    }
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponse(token, user.getEmail(), user.getRole().name());
    }
}
