// CircuitImporter.java
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

package control.command.importer;

import control.command.ports.ImportStrategy;
import control.command.ports.UndoableCommand;
import control.command.importer.qasm.QasmImportStrategy;
import control.command.importer.qiskit.QiskitImportStrategy;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.util.Map;

public class CircuitImporter {
    private final Map<ImportFormat, ImportStrategy> strategies;

    public CircuitImporter() {
        this.strategies = Map.of(
                ImportFormat.QASM, new QasmImportStrategy(),
                ImportFormat.QISKIT, new QiskitImportStrategy()
        );
    }

    public QuantumCircuit importCircuit(String filename, ImportFormat format) {
        ImportStrategy strategy = strategies.get(format);
        if (strategy == null) throw new UnsupportedOperationException("Import format not supported: " + format);
        return strategy.importCircuit(filename);
    }

    public UndoableCommand createImportCommand(QuantumCircuit circuit, String filename, ImportFormat format) {
        ImportStrategy strategy = strategies.get(format);
        if (strategy == null) throw new UnsupportedOperationException("Import format not supported: " + format);
        return strategy.createImportCommand(circuit, filename);
    }
}