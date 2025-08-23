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
import control.command.simulate.SimulateCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/quantum/simulate")
@Tag(name = "Quantum Simulation", description = "Quantum circuit simulation")
@CrossOrigin(origins = "*")
public class SimulationController {
    private final Controller quantumController;

    @Autowired
    public SimulationController(QuantumControllerService controllerService) {
        this.quantumController = controllerService.getController();
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
            Map<String, Object> response = new HashMap<>();
            response.put("finalState", finalState);
            response.put("success", true);
            response.put("message", "Simulation completed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}