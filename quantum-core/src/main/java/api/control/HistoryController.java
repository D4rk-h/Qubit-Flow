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
@RequestMapping("/api/quantum/history")
@Tag(name = "Command History", description = "Undo/Redo operations")
@CrossOrigin(origins = "*")
public class HistoryController {
    private final Controller quantumController;

    @Autowired
    public HistoryController(QuantumControllerService controllerService) {
        this.quantumController = controllerService.getController();
    }

    @PostMapping("/undo")
    @Operation(summary = "Undo last operation")
    public ResponseEntity<Map<String, Object>> undo() {
        try {
            boolean success = quantumController.undo();
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", success ? "Operation undone successfully" : "No operation to undo");
            response.put("canUndo", quantumController.canUndo());
            response.put("canRedo", quantumController.canRedo());
            response.put("historySize", quantumController.getHistorySize());
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            response.put("qubitCount", quantumController.getQubitCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("canUndo", quantumController.canUndo());
            errorResponse.put("canRedo", quantumController.canRedo());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/redo")
    @Operation(summary = "Redo last undone operation")
    public ResponseEntity<Map<String, Object>> redo() {
        try {
            boolean success = quantumController.redo();
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", success ? "Operation redone successfully" : "No operation to redo");
            response.put("canUndo", quantumController.canUndo());
            response.put("canRedo", quantumController.canRedo());
            response.put("historySize", quantumController.getHistorySize());
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            response.put("qubitCount", quantumController.getQubitCount());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("canUndo", quantumController.canUndo());
            errorResponse.put("canRedo", quantumController.canRedo());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Get history status")
    public ResponseEntity<Map<String, Object>> getHistoryStatus() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("canUndo", quantumController.canUndo());
            response.put("canRedo", quantumController.canRedo());
            response.put("historySize", quantumController.getHistorySize());
            response.put("lastCommand", quantumController.getLastCommandType());
            response.put("circuitDepth", quantumController.getCircuitDepth());
            response.put("totalGates", quantumController.getTotalGateCount());
            response.put("qubitCount", quantumController.getQubitCount());
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear command history")
    public ResponseEntity<Map<String, Object>> clearHistory() {
        try {
            quantumController.clearCommandHistory();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Command history cleared successfully");
            response.put("historySize", quantumController.getHistorySize());
            response.put("canUndo", quantumController.canUndo());
            response.put("canRedo", quantumController.canRedo());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}