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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/quantum/qubits")
@Tag(name = "Qubit Management", description = "Qubit management operations")
@CrossOrigin(origins = "*")
public class QubitController {
    private final Controller quantumController;

    @Autowired
    public QubitController(QuantumControllerService controllerService) {
        this.quantumController = controllerService.getController(); // Shared instance
    }

    @PostMapping("/add")
    @Operation(summary = "Add a qubit to the circuit")
    public ResponseEntity<Map<String, Object>> addQubit() {
        try {
            quantumController.addQubit();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Qubit added successfully");
            response.put("newQubitCount", quantumController.getQubitCount());
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
            errorResponse.put("currentQubitCount", quantumController.getQubitCount());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Remove a qubit from the circuit")
    public ResponseEntity<Map<String, Object>> removeQubit() {
        try {
            quantumController.removeQubit();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Qubit removed successfully");
            response.put("newQubitCount", quantumController.getQubitCount());
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
            errorResponse.put("currentQubitCount", quantumController.getQubitCount());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/set-count")
    @Operation(summary = "Set the number of qubits")
    public ResponseEntity<Map<String, Object>> setQubitCount(@RequestParam int count) {
        try {
            int previousCount = quantumController.getQubitCount();
            quantumController.setQubitCount(count);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Qubit count set to " + count);
            response.put("previousQubitCount", previousCount);
            response.put("newQubitCount", quantumController.getQubitCount());
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
            errorResponse.put("currentQubitCount", quantumController.getQubitCount());
            errorResponse.put("requestedCount", count);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/count")
    @Operation(summary = "Get current qubit count")
    public ResponseEntity<Map<String, Object>> getQubitCount() {
        Map<String, Object> response = new HashMap<>();
        response.put("qubitCount", quantumController.getQubitCount());
        response.put("circuitDepth", quantumController.getCircuitDepth());
        response.put("totalGates", quantumController.getTotalGateCount());
        response.put("maxQubits", 10); // Assuming system limit
        response.put("minQubits", 1);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    @Operation(summary = "Get detailed qubit information")
    public ResponseEntity<Map<String, Object>> getQubitInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("qubitCount", quantumController.getQubitCount());
        response.put("circuitDepth", quantumController.getCircuitDepth());
        response.put("totalGates", quantumController.getTotalGateCount());
        response.put("circuitInfo", quantumController.getCircuitInfo());
        response.put("canAddQubit", quantumController.getQubitCount() < 10);
        response.put("canRemoveQubit", quantumController.getQubitCount() > 1);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}