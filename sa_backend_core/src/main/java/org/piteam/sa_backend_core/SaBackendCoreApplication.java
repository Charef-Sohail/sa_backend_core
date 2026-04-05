package org.piteam.sa_backend_core;

import org.piteam.sa_backend_core.models.Task;
import org.piteam.sa_backend_core.repositories.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SaBackendCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaBackendCoreApplication.class, args);
    }

    @Bean
    CommandLineRunner start(TaskRepository taskRepository) {
        return args -> {
            taskRepository.save(
                    Task.builder()
                            .id("abc456")
                            .title("Dormir")
                            .description("Aller dormir dans la chambre")
                            .build()
            );
            taskRepository.findAll().forEach( t -> System.out.println(t));
        };
    }

}
