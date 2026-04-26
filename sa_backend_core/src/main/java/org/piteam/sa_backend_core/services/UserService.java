package org.piteam.sa_backend_core.services;

import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.dto.auth.LoginRequest;
import org.piteam.sa_backend_core.dto.auth.LoginResponse;
import org.piteam.sa_backend_core.dto.auth.RegisterRequest;
import org.piteam.sa_backend_core.dto.auth.RegisterResponse;
import org.piteam.sa_backend_core.exceptions.EmailAlreadyExistsException;
import org.piteam.sa_backend_core.exceptions.InvalidCredentialsException;
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
        String token = jwtService.generateToken(saved.getId(), saved.getEmail(), saved.getRole().name());

        return new RegisterResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getRole(),
                saved.getAge(),
                saved.getUniversity(),
                token
        );
    }
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return new LoginResponse(user.getId(), token, user.getEmail(), user.getRole().name(), user.getName(), user.getAge(), user.getUniversity());
    }
}
