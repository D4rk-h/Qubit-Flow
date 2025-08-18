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

package control.command.exporter.qasm;

import control.command.ports.UndoableCommand;
import control.command.ports.ExportStrategy;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.io.FileWriter;
import java.io.IOException;

public class QasmExportStrategy implements ExportStrategy {

    @Override
    public void export(QuantumCircuit circuit, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writeQASMHeader(writer);
            writeQASMBody(writer, circuit);
            System.out.println("Circuit exported to QASM: " + filename);
        } catch (IOException e) {
            throw new RuntimeException("Error exporting to QASM: " + e.getMessage(), e);
        }
    }

    @Override
    public UndoableCommand createExportCommand(QuantumCircuit circuit, String filename) {
        return new ExportQASMCommand(circuit, filename);
    }

    private void writeQASMHeader(FileWriter writer) throws IOException {
        writer.write("OPENQASM 2.0;\n");
        writer.write("include \"qelib1.inc\";\n");
    }

    private void writeQASMBody(FileWriter writer, QuantumCircuit circuit) throws IOException {
        writer.write("qreg q[" + circuit.getNQubits() + "];\n");
        writer.write("creg c[" + circuit.getNQubits() + "];\n");

        // TODO: Implement circuit to QASM translation
        writer.write("// Circuit operations would be translated here\n");
    }
}