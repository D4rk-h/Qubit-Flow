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

package control.command.parser;

import control.command.ports.ImportParser;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumGate.QuantumGates;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QasmParser implements ImportParser {
    private final String qasmContent;

    public QasmParser(String qasmContent) {
        this.qasmContent = qasmContent;
    }

    @Override
    public QuantumCircuit parse() {
        final Pattern instructionPattern = Pattern.compile("^\\s*(\\w+)\\s+([^;]+);");
        final Pattern indexPattern = Pattern.compile("\\d+");
        QuantumCircuit circuit = new QuantumCircuit(extractQubitCount(qasmContent));
        String[] lines = qasmContent.split("\\R");
        for (String line : lines) {
            String trimmedLine = line.trim();
            Matcher matcher = instructionPattern.matcher(trimmedLine);
            if (!matcher.find()) {continue;}
            String operation = matcher.group(1);
            String args = matcher.group(2);
            List<Integer> indices = new ArrayList<>();
            Matcher indexMatcher = indexPattern.matcher(args);
            while (indexMatcher.find()) {indices.add(Integer.parseInt(indexMatcher.group()));}
            if (indices.isEmpty()) {continue;}
            switch (operation) {
                case "h":       circuit.addHadamard(indices.get(0)); break;
                case "x":       circuit.addNot(indices.get(0)); break;
                case "y":       circuit.addY(indices.get(0)); break;
                case "z":       circuit.addZ(indices.get(0)); break;
                case "s":       circuit.addPhase(indices.get(0)); break;
                case "t":       circuit.addT(indices.get(0)); break;
                case "cx":      circuit.addCNOT(indices.get(0), indices.get(1)); break;
                case "cy":      circuit.addControlled(QuantumGates.y(), indices.get(0), indices.get(1)); break;
                case "cz":      circuit.addControlled(QuantumGates.z(), indices.get(0), indices.get(1)); break;
                case "ch":      circuit.addControlled(QuantumGates.hadamard(), indices.get(0), indices.get(1)); break;
                case "swap":    circuit.addSwap(indices.get(0), indices.get(1)); break;
                case "ccx":     circuit.addToffoli(indices.get(0), indices.get(1), indices.get(2)); break;
                case "cswap":   circuit.addControlled(QuantumGates.swap(), indices.get(0), indices.get(1), indices.get(2)); break;
                case "c3x":
                case "c4x":
                    int target = indices.get(indices.size() - 1);
                    int[] controls = indices.subList(0, indices.size() - 1).stream().mapToInt(i -> i).toArray();
                    circuit.addControlled(QuantumGates.not(), target, controls);
                    break;
                case "measure":
                    break;
            }
        }
        return circuit;
    }

    private int extractQubitCount(String qasm) {
        Pattern qregPattern = Pattern.compile("qreg\\s+\\w+\\[(\\d+)\\];");
        Matcher matcher = qregPattern.matcher(qasm);
        if (matcher.find()) return Integer.parseInt(matcher.group(1));
        return 0;
    }

    public String getQasmContent() {return qasmContent;}

}
