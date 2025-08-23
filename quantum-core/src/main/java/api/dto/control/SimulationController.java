package api.dto.control;


import api.dto.QuantumStateDto;
import control.Controller;
import control.command.simulate.SimulateCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/quantum/simulate")
@Tag(name = "Quantum Simulation", description = "Quantum circuit simulation")
@CrossOrigin(origins = "*")
public class SimulationController {

    private final Controller quantumController;

    public SimulationController() {
        this.quantumController = new Controller();
    }

    @PostMapping("/execute")
    @Operation(summary = "Execute the quantum circuit")
    public ResponseEntity<QuantumStateDto> executeCircuit() {
        try {
            quantumController.executeCircuit();
            QuantumStateDto finalState = QuantumStateDto.from(quantumController.getCurrentState());
            return ResponseEntity.ok(finalState);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/run")
    @Operation(summary = "Run full simulation")
    public ResponseEntity<Map<String, Object>> runSimulation() {
        try {
            SimulateCommand simulation = quantumController.simulate();
            QuantumStateDto finalState = QuantumStateDto.from(simulation.getFinalState());
            return ResponseEntity.ok(Map.of(
                    "finalState", finalState,
                    "success", true,
                    "message", "Simulation completed successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
}