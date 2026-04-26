package org.piteam.sa_backend_core.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@Data
@NoArgsConstructor
@Document(collection = "users")
public abstract class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String passwordHash;
    private String name;
    private Role role;
    private Instant createdAt;
    private Integer age;
    private String university;

    public abstract void login();
    public abstract void logout();
}
