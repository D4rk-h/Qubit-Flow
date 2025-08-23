package api.dto.control;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

public @RestController
@RequestMapping("/api/test")
@Tag(name = "Health Check", description = "API health and status")
@CrossOrigin(origins = "*")
class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "healthy",
                "timestamp", System.currentTimeMillis(),
                "service", "Qubit Flow API",
                "version", "1.0.0"
        ));
    }

    @GetMapping("/info")
    @Operation(summary = "API information")
    public ResponseEntity<Map<String, Object>> apiInfo() {
        return ResponseEntity.ok(Map.of(
                "name", "Qubit Flow REST API",
                "description", "REST API for quantum circuit simulation and manipulation",
                "version", "1.0.0",
                "endpoints", Map.of(
                        "circuits", "/api/quantum/circuit",
                        "gates", "/api/quantum/gates",
                        "state", "/api/quantum/state",
                        "simulation", "/api/quantum/simulate",
                        "measurement", "/api/quantum/measure",
                        "qubits", "/api/quantum/qubits",
                        "history", "/api/quantum/history"
                )
        ));
    }
}