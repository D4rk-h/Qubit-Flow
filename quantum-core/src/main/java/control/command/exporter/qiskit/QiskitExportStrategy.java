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

package control.command.exporter.qiskit;

import control.command.parser.QiskitParser;
import control.command.ports.ExportStrategy;
import control.command.ports.UndoableCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.io.FileWriter;
import java.io.IOException;

public class QiskitExportStrategy implements ExportStrategy {

    @Override
    public void export(QuantumCircuit circuit, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            QiskitParser parser = new QiskitParser();
            String qiskitContent = parser.serialize(circuit);
            writer.write(qiskitContent);
            System.out.println("Circuit exported to Qiskit: " + filename);
        } catch (IOException e) {
            throw new RuntimeException("Error exporting to Qiskit: " + e.getMessage(), e);
        }
    }

    @Override
    public UndoableCommand createExportCommand(QuantumCircuit circuit, String filename) {
        return new ExportQISKITCommand(circuit, filename);
    }
}