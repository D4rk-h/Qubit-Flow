package api.dto.control;

import control.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quantum/gates")
@Tag(name = "Quantum Gates", description = "Quantum gate operations")
@CrossOrigin(origins = "*")
public class QuantumGateController {

    private final Controller quantumController;

    public QuantumGateController() {
        this.quantumController = new Controller();
    }

    @PostMapping("/hadamard")
    @Operation(summary = "Add Hadamard gate")
    public ResponseEntity<String> addHadamard(@RequestParam int qubit) {
        try {
            quantumController.addHadamard(qubit);
            return ResponseEntity.ok("Hadamard gate added to qubit " + qubit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/pauli-x")
    @Operation(summary = "Add Pauli-X (NOT) gate")
    public ResponseEntity<String> addPauliX(@RequestParam int qubit) {
        try {
            quantumController.addPauliX(qubit);
            return ResponseEntity.ok("Pauli-X gate added to qubit " + qubit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/pauli-y")
    @Operation(summary = "Add Pauli-Y gate")
    public ResponseEntity<String> addPauliY(@RequestParam int qubit) {
        try {
            quantumController.addPauliY(qubit);
            return ResponseEntity.ok("Pauli-Y gate added to qubit " + qubit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/pauli-z")
    @Operation(summary = "Add Pauli-Z gate")
    public ResponseEntity<String> addPauliZ(@RequestParam int qubit) {
        try {
            quantumController.addPauliZ(qubit);
            return ResponseEntity.ok("Pauli-Z gate added to qubit " + qubit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/t-gate")
    @Operation(summary = "Add T gate")
    public ResponseEntity<String> addTGate(@RequestParam int qubit) {
        try {
            quantumController.addTGate(qubit);
            return ResponseEntity.ok("T gate added to qubit " + qubit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/s-gate")
    @Operation(summary = "Add S (Phase) gate")
    public ResponseEntity<String> addSGate(@RequestParam int qubit) {
        try {
            quantumController.addSGate(qubit);
            return ResponseEntity.ok("S gate added to qubit " + qubit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/cnot")
    @Operation(summary = "Add CNOT gate")
    public ResponseEntity<String> addCNOT(@RequestParam int control, @RequestParam int target) {
        try {
            quantumController.addCNOT(control, target);
            return ResponseEntity.ok("CNOT gate added with control=" + control + ", target=" + target);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/swap")
    @Operation(summary = "Add SWAP gate")
    public ResponseEntity<String> addSwap(@RequestParam int qubit1, @RequestParam int qubit2) {
        try {
            quantumController.addSwap(qubit1, qubit2);
            return ResponseEntity.ok("SWAP gate added between qubits " + qubit1 + " and " + qubit2);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/toffoli")
    @Operation(summary = "Add Toffoli gate")
    public ResponseEntity<String> addToffoli(@RequestParam int control1, @RequestParam int control2, @RequestParam int target) {
        try {
            quantumController.addToffoli(control1, control2, target);
            return ResponseEntity.ok("Toffoli gate added with controls=" + control1 + "," + control2 + ", target=" + target);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
