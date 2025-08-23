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