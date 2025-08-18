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

package control.command.exporter.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import control.command.ports.UndoableCommand;
import control.command.ports.ExportStrategy;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonExportStrategy implements ExportStrategy {
    @Override
    public void export(QuantumCircuit circuit, String filename) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> circuitData = createCircuitData(circuit);
            mapper.writeValue(new File(filename), circuitData);
            System.out.println("Circuit exported to JSON: " + filename);
        } catch (IOException e) {
            throw new RuntimeException("Error exporting to JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public UndoableCommand createExportCommand(QuantumCircuit circuit, String filename) {
        return new ExportJSONCommand(circuit, filename);
    }

    private Map<String, Object> createCircuitData(QuantumCircuit circuit) {
        Map<String, Object> data = new HashMap<>();
        data.put("circuit", circuit.toString());
        data.put("numQubits", circuit.getNQubits());
        data.put("numLayers", circuit.getDepth());
        data.put("totalGates", circuit.getTotalGateCount());
        data.put("exportTimestamp", System.currentTimeMillis());
        return data;
    }
}

