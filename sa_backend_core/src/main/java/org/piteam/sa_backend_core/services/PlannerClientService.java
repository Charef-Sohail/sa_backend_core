package org.piteam.sa_backend_core.services;

import org.piteam.sa_backend_core.dto.planner.OptimizationRequestDTO;
import org.piteam.sa_backend_core.dto.planner.OptimizationResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class PlannerClientService {

    // L'URL de votre microservice Python
    private final String FASTAPI_URL = "http://localhost:8000/api/planner/optimize";
    private final RestTemplate restTemplate;

    public PlannerClientService() {
        this.restTemplate = new RestTemplate();
    }

    public OptimizationResponseDTO askPythonForSchedule(OptimizationRequestDTO requestDTO) {
        // 1. Préparer les headers (On dit qu'on envoie du JSON)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2. Emballer la requête
        HttpEntity<OptimizationRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);

        try {
            // 3. Tirer la requête POST vers Python
            ResponseEntity<OptimizationResponseDTO> response = restTemplate.postForEntity(
                    FASTAPI_URL,
                    requestEntity,
                    OptimizationResponseDTO.class
            );

            return response.getBody();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la communication avec le service IA: " + e.getMessage());
        }
    }
}