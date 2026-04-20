package org.piteam.sa_backend_core.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "users")
public class Admin extends User{
    private List<String> permissions;
    public Admin() {
        setRole(Role.ROLE_ADMIN);
    }
    @Override
    public void login(){}

    @Override
    public void logout(){}

}
