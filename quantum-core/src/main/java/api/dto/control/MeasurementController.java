package api.dto.control;


import control.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.quantumModel.quantumState.quantumStateUtils.MeasurementResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quantum/measure")
@Tag(name = "Quantum Measurement", description = "Quantum measurement operations")
@CrossOrigin(origins = "*")
public class MeasurementController {

    private final Controller quantumController;

    public MeasurementController() {
        this.quantumController = new Controller();
    }

    @PostMapping("/qubit")
    @Operation(summary = "Measure a specific qubit")
    public ResponseEntity<Map<String, Object>> measureQubit(@RequestParam int qubit) {
        try {
            MeasurementResult result = quantumController.measureQubit(qubit);
            Map<String, Object> response = new HashMap<>();
            response.put("outcome", result.outcome());
            response.put("probability", result.probability());
            response.put("binaryRepresentation", result.getBinaryRepresentation());
            response.put("timestamp", result.timestamp());
            response.put("isDefinite", result.isDefiniteOutcome());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/all")
    @Operation(summary = "Measure all qubits")
    public ResponseEntity<Map<String, Object>> measureAll() {
        try {
            MeasurementResult result = quantumController.measureAll();
            Map<String, Object> response = new HashMap<>();
            response.put("outcome", result.outcome());
            response.put("probability", result.probability());
            response.put("binaryRepresentation", result.getBinaryRepresentation());
            response.put("timestamp", result.timestamp());
            response.put("isDefinite", result.isDefiniteOutcome());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/add")
    @Operation(summary = "Add measurement to circuit")
    public ResponseEntity<String> addMeasurement(@RequestParam int qubit) {
        try {
            quantumController.addMeasurement(qubit);
            return ResponseEntity.ok("Measurement added to qubit " + qubit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/add-all")
    @Operation(summary = "Add measurements to all qubits")
    public ResponseEntity<String> addMeasurementAll() {
        try {
            quantumController.addMeasurementAll();
            return ResponseEntity.ok("Measurements added to all qubits");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/results")
    @Operation(summary = "Get measurement results")
    public ResponseEntity<List<Map<String, Object>>> getMeasurementResults() {
        List<MeasurementResult> results = quantumController.getMeasurementResults();
        List<Map<String, Object>> response = results.stream()
                .map(r -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("outcome", r.outcome());
                    map.put("probability", r.probability());
                    map.put("binaryRepresentation", r.getBinaryRepresentation());
                    map.put("timestamp", r.timestamp());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/results")
    @Operation(summary = "Clear measurement results")
    public ResponseEntity<String> clearMeasurementResults() {
        quantumController.clearMeasurementResults();
        return ResponseEntity.ok("Measurement results cleared");
    }
}
