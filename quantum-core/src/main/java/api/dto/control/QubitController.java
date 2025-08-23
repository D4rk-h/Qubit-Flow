package api.dto.control;

import control.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/quantum/qubits")
@Tag(name = "Qubit Management", description = "Qubit management operations")
@CrossOrigin(origins = "*")
public class QubitController {
    private final Controller quantumController;

    public QubitController() {
        this.quantumController = new Controller();
    }

    @PostMapping("/add")
    @Operation(summary = "Add a qubit to the circuit")
    public ResponseEntity<Map<String, Object>> addQubit() {
        try {
            quantumController.addQubit();
            return ResponseEntity.ok(Map.of(
                    "message", "Qubit added successfully",
                    "newQubitCount", quantumController.getQubitCount()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Remove a qubit from the circuit")
    public ResponseEntity<Map<String, Object>> removeQubit() {
        try {
            quantumController.removeQubit();
            return ResponseEntity.ok(Map.of(
                    "message", "Qubit removed successfully",
                    "newQubitCount", quantumController.getQubitCount()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/set-count")
    @Operation(summary = "Set the number of qubits")
    public ResponseEntity<Map<String, Object>> setQubitCount(@RequestParam int count) {
        try {
            quantumController.setQubitCount(count);
            return ResponseEntity.ok(Map.of(
                    "message", "Qubit count set to " + count,
                    "qubitCount", quantumController.getQubitCount()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/count")
    @Operation(summary = "Get current qubit count")
    public ResponseEntity<Map<String, Object>> getQubitCount() {
        return ResponseEntity.ok(Map.of(
                "qubitCount", quantumController.getQubitCount(),
                "maxQubits", 10,
                "minQubits", 1
        ));
    }
}