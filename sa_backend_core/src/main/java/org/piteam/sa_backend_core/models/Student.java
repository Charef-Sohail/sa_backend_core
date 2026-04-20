package org.piteam.sa_backend_core.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "users")

public class Student extends User{
    private Map<String, Object> preferences;
    public Student() {
        setRole(Role.ROLE_STUDENT);
    }
    @Override
    public void login(){}

    @Override
    public void logout(){}

    public void updatePrefrences(Map<String, Object> preferences) {
        this.preferences = preferences;
    }

}
