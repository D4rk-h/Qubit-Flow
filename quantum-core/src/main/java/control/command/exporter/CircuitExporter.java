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

package control.command.exporter;

import control.command.ports.ExportStrategy;
import control.command.ports.UndoableCommand;
import control.command.exporter.json.JsonExportStrategy;
import control.command.exporter.qasm.QasmExportStrategy;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.util.Map;

public class CircuitExporter {
    private final Map<ExportFormat, ExportStrategy> strategies;

    public CircuitExporter() {
        this.strategies = Map.of(
                ExportFormat.JSON, new JsonExportStrategy(),
                ExportFormat.QASM, new QasmExportStrategy()
        );
    }

    public void export(QuantumCircuit circuit, ExportFormat format) {
        ExportStrategy strategy = strategies.get(format);
        if (strategy == null) throw new UnsupportedOperationException("Export format not supported: " + format);
        String filename = generateFilename(format);
        strategy.export(circuit, filename);
    }

    public UndoableCommand createExportCommand(QuantumCircuit circuit, ExportFormat format) {
        ExportStrategy strategy = strategies.get(format);
        if (strategy == null) {
            throw new UnsupportedOperationException("Export format not supported: " + format);
        }

        String filename = generateFilename(format);
        return strategy.createExportCommand(circuit, filename);
    }

    private String generateFilename(ExportFormat format) {
        return "./exported_circuit." + format.getExtension();
    }
}