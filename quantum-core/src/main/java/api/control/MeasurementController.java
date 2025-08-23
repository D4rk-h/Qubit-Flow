// Copyright 2025 D4rk-h
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package api.control;

import api.service.QuantumControllerService;
import control.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.quantumModel.quantumState.quantumStateUtils.MeasurementResult;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public MeasurementController(QuantumControllerService controllerService) {
        this.quantumController = controllerService.getController(); // Shared instance
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
            response.put("message", "Qubit " + qubit + " measured with outcome: " + result.outcome());
            response.put("qubitCount", quantumController.getQubitCount());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
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
            response.put("message", "All qubits measured with outcome: " + result.outcome());
            response.put("qubitCount", quantumController.getQubitCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/add")
    @Operation(summary = "Add measurement to circuit")
    public ResponseEntity<Map<String, Object>> addMeasurement(@RequestParam int qubit) {
        try {
            quantumController.addMeasurement(qubit);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Measurement added to qubit " + qubit);
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/add-all")
    @Operation(summary = "Add measurements to all qubits")
    public ResponseEntity<Map<String, Object>> addMeasurementAll() {
        try {
            quantumController.addMeasurementAll();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Measurements added to all qubits");
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            response.put("qubitCount", quantumController.getQubitCount());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/results")
    @Operation(summary = "Get measurement results")
    public ResponseEntity<Map<String, Object>> getMeasurementResults() {
        try {
            List<MeasurementResult> results = quantumController.getMeasurementResults();
            List<Map<String, Object>> formattedResults = results.stream()
                    .map(r -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("outcome", r.outcome());
                        map.put("probability", r.probability());
                        map.put("binaryRepresentation", r.getBinaryRepresentation());
                        map.put("timestamp", r.timestamp());
                        map.put("isDefinite", r.isDefiniteOutcome());
                        return map;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("results", formattedResults);
            response.put("count", formattedResults.size());
            response.put("qubitCount", quantumController.getQubitCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/results")
    @Operation(summary = "Clear measurement results")
    public ResponseEntity<Map<String, Object>> clearMeasurementResults() {
        try {
            quantumController.clearMeasurementResults();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Measurement results cleared successfully");
            response.put("success", true);
            response.put("qubitCount", quantumController.getQubitCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}