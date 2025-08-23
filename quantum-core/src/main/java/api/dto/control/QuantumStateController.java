package api.dto.control;


import api.dto.QuantumStateDto;
import control.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.quantumModel.quantumState.QuantumState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quantum/state")
@Tag(name = "Quantum State", description = "Quantum state operations")
@CrossOrigin(origins = "*")
public class QuantumStateController {
    private final Controller quantumController;

    public QuantumStateController() {
        this.quantumController = new Controller();
    }

    @GetMapping("/current")
    @Operation(summary = "Get current quantum state")
    public ResponseEntity<QuantumStateDto> getCurrentState() {
        QuantumStateDto dto = QuantumStateDto.from(quantumController.getCurrentState());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset state to |0...0⟩")
    public ResponseEntity<String> resetState() {
        quantumController.resetState();
        return ResponseEntity.ok("State reset to |0...0⟩");
    }

    @PostMapping("/set")
    @Operation(summary = "Set initial state")
    public ResponseEntity<String> setInitialState(@RequestBody QuantumStateDto stateDto) {
        try {
            QuantumState state = stateDto.toQuantumState();
            quantumController.setInitialState(state);
            return ResponseEntity.ok("Initial state set successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/probabilities")
    @Operation(summary = "Get measurement probabilities")
    public ResponseEntity<double[]> getProbabilities() {
        double[] probabilities = quantumController.getCurrentState().getProbabilities();
        return ResponseEntity.ok(probabilities);
    }
}