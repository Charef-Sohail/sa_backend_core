package org.piteam.sa_backend_core.dto.place;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    private boolean success;
    private Object data;
    private String error;

    public ApiResponse(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public ApiResponse(boolean success, String error) {
        this.success = success;
        this.error = error;
    }
}
