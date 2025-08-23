package api.control;


import api.dto.QuantumStateDto;
import api.service.QuantumControllerService;
import control.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/quantum/state")
@Tag(name = "Quantum State", description = "Quantum state operations")
@CrossOrigin(origins = "*")
class QuantumStateController {

    private final Controller quantumController;

    @Autowired
    public QuantumStateController(QuantumControllerService controllerService) {
        this.quantumController = controllerService.getController();
    }

    @GetMapping("/current")
    @Operation(summary = "Get current quantum state")
    public ResponseEntity<QuantumStateDto> getCurrentState() {
        QuantumStateDto dto = QuantumStateDto.from(quantumController.getCurrentState());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset state to |00...0⟩")
    public ResponseEntity<Map<String, Object>> resetState() {
        quantumController.resetState();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "State reset to |00...0⟩");
        response.put("qubits", quantumController.getQubitCount());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/probabilities")
    @Operation(summary = "Get measurement probabilities")
    public ResponseEntity<Map<String, Object>> getProbabilities() {
        double[] probabilities = quantumController.getCurrentState().getProbabilities();
        Map<String, Object> response = new HashMap<>();
        response.put("probabilities", probabilities);
        response.put("qubits", quantumController.getQubitCount());
        return ResponseEntity.ok(response);
    }
}