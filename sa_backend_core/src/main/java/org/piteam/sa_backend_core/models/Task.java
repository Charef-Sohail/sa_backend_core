package org.piteam.sa_backend_core.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "tasks")
@Builder @Setter
@Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class Task {
    @Id
    private String id;
    private String title;
    private String description;



}
