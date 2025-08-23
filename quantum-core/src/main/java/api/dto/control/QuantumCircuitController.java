package api.dto.control;

import api.dto.*;
import api.service.CircuitService;
import api.utils.CircuitSummary;
import api.utils.QuantumCircuitDtoUtils;
import control.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/quantum/circuit")
@Tag(name = "Quantum Circuit", description = "Quantum circuit operations")
@CrossOrigin(origins = "*")
public class QuantumCircuitController {

    private final Controller quantumController;
    private final CircuitService circuitService;

    public QuantumCircuitController() {
        this.quantumController = new Controller();
        this.circuitService = new CircuitService();
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new quantum circuit")
    public ResponseEntity<QuantumCircuitDto> createCircuit(
            @Parameter(description = "Number of qubits") @RequestParam int qubits) {
        try {
            quantumController.createNewCircuit(qubits);
            QuantumCircuitDto dto = CircuitService.toDto(quantumController.getCircuit());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/info")
    @Operation(summary = "Get circuit information")
    public ResponseEntity<Map<String, Object>> getCircuitInfo() {
        return ResponseEntity.ok(Map.of(
                "info", quantumController.getCircuitInfo(),
                "qubits", quantumController.getQubitCount(),
                "depth", quantumController.getCircuitDepth(),
                "gates", quantumController.getTotalGateCount()
        ));
    }

    @GetMapping("/current")
    @Operation(summary = "Get current circuit state")
    public ResponseEntity<QuantumCircuitDto> getCurrentCircuit() {
        QuantumCircuitDto dto = CircuitService.toDto(quantumController.getCircuit());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/clear")
    @Operation(summary = "Clear the circuit")
    public ResponseEntity<Void> clearCircuit() {
        quantumController.clearCircuit();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/import/qasm")
    @Operation(summary = "Import circuit from QASM")
    public ResponseEntity<QuantumCircuitDto> importFromQASM(@RequestParam String filename) {
        try {
            quantumController.importFromQASM(filename);
            return ResponseEntity.ok(CircuitService.toDto(quantumController.getCircuit()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/export/qasm")
    @Operation(summary = "Export circuit to QASM")
    public ResponseEntity<String> exportToQASM() {
        try {
            quantumController.exportToQASM();
            return ResponseEntity.ok("Circuit exported to QASM successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Export failed");
        }
    }

    @PostMapping("/export/json")
    @Operation(summary = "Export circuit to JSON")
    public ResponseEntity<String> exportToJSON() {
        try {
            quantumController.exportToJSON();
            return ResponseEntity.ok("Circuit exported to JSON successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Export failed");
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "Get circuit statistics")
    public ResponseEntity<CircuitSummary> getStats() {
        QuantumCircuitDto dto = CircuitService.toDto(quantumController.getCircuit());
        CircuitSummary summary = QuantumCircuitDtoUtils.getSummary(dto);
        return ResponseEntity.ok(summary);
    }
}
