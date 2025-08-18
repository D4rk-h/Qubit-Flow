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

package control.command.importer.qiskit;

import control.command.parser.QiskitParser;
import control.command.ports.ImportStrategy;
import control.command.ports.UndoableCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class QiskitImportStrategy implements ImportStrategy {

    @Override
    public QuantumCircuit importCircuit(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String content = readFileContent(reader);
            QuantumCircuit circuit = parseCircuitFromQiskit(content);
            System.out.println("Circuit imported from Qiskit: " + filename);
            return circuit;
        } catch (IOException e) {
            throw new RuntimeException("Error importing from Qiskit: " + e.getMessage(), e);
        }
    }

    @Override
    public UndoableCommand createImportCommand(QuantumCircuit targetCircuit, String filename) {
        return new ImportQISKITCommand(targetCircuit, filename);
    }

    private String readFileContent(BufferedReader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) content.append(line).append("\n");
        return content.toString();
    }

    private QuantumCircuit parseCircuitFromQiskit(String qiskitContent) {
        QiskitParser parser = new QiskitParser(qiskitContent);
        return parser.parse();
    }
}