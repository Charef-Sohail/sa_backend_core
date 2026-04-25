package org.piteam.sa_backend_core.config;

import org.bson.Document;
import org.piteam.sa_backend_core.models.Admin;
import org.piteam.sa_backend_core.models.Role;
import org.piteam.sa_backend_core.models.Student;
import org.piteam.sa_backend_core.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.Instant;
import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(new UserReadConverter()));
    }

    @ReadingConverter
    static class UserReadConverter implements Converter<Document, User> {

        @Override
        public User convert(Document source) {
            String roleStr = source.getString("role");
            User user;

            if ("ROLE_ADMIN".equals(roleStr)) {
                user = new Admin();
            } else {
                user = new Student();
            }

            user.setId(source.getObjectId("_id").toHexString());
            user.setEmail(source.getString("email"));
            user.setPasswordHash(source.getString("passwordHash"));
            user.setName(source.getString("name"));
            user.setCreatedAt(source.getDate("createdAt") != null
                    ? source.getDate("createdAt").toInstant()
                    : Instant.now());

            if (roleStr != null) {
                try {
                    user.setRole(Role.valueOf(roleStr));
                } catch (IllegalArgumentException ignored) {}
            }

            return user;
        }
    }
}