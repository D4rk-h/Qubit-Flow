package api.control;

import api.service.QuantumControllerService;
import control.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

@RestController
@RequestMapping("/api/quantum/gates")
@Tag(name = "Quantum Gates", description = "Quantum gate operations")
@CrossOrigin(origins = "*")
class QuantumGateController {

    private final Controller quantumController;

    @Autowired
    public QuantumGateController(QuantumControllerService controllerService) {
        this.quantumController = controllerService.getController();
    }

    @PostMapping("/hadamard")
    @Operation(summary = "Add Hadamard gate")
    public ResponseEntity<Map<String, Object>> addHadamard(@RequestParam int qubit) {
        try {
            quantumController.addHadamard(qubit);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Hadamard gate added to qubit " + qubit);
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/pauli-x")
    @Operation(summary = "Add Pauli-X (NOT) gate")
    public ResponseEntity<Map<String, Object>> addPauliX(@RequestParam int qubit) {
        try {
            quantumController.addPauliX(qubit);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Pauli-X gate added to qubit " + qubit);
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/cnot")
    @Operation(summary = "Add CNOT gate")
    public ResponseEntity<Map<String, Object>> addCNOT(@RequestParam int control, @RequestParam int target) {
        try {
            quantumController.addCNOT(control, target);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "CNOT gate added with control=" + control + ", target=" + target);
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/pauli-y")
    @Operation(summary = "Add Pauli-Y gate")
    public ResponseEntity<Map<String, Object>> addPauliY(@RequestParam int qubit) {
        try {
            quantumController.addPauliY(qubit);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Pauli-Y gate added to qubit "+ qubit);
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/pauli-z")
    @Operation(summary = "Add Pauli-Z gate")
    public ResponseEntity<Map<String, Object>> addPauliZ(@RequestParam int qubit) {
        try {
            quantumController.addPauliZ(qubit);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Pauli-Z gate added to qubit "+ qubit);
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/t-gate")
    @Operation(summary = "Add T gate")
    public ResponseEntity<Map<String, Object>> addTGate(@RequestParam int qubit) {
        try {
            quantumController.addTGate(qubit);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "T gate added to qubit "+ qubit);
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/s-gate")
    @Operation(summary = "Add S (Phase) gate")
    public ResponseEntity<Map<String, Object>> addSGate(@RequestParam int qubit) {
        try {
            quantumController.addSGate(qubit);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "S gate added to qubit " + qubit);
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/swap")
    @Operation(summary = "Add SWAP gate")
    public ResponseEntity<Map<String, Object>> addSwap(@RequestParam int qubit1, @RequestParam int qubit2) {
        try {
            quantumController.addSwap(qubit1, qubit2);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "SWAP gate added between qubits " + qubit1 + " and " + qubit2);
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/toffoli")
    @Operation(summary = "Add Toffoli gate")
    public ResponseEntity<Map<String, Object>> addToffoli(@RequestParam int control1, @RequestParam int control2, @RequestParam int target) {
        try {
            quantumController.addToffoli(control1, control2, target);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Toffoli gate added with controls=" + control1 + "," + control2 + ", target=" + target);
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
