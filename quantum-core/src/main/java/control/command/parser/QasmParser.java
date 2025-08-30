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
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumGate.GateOperation;
import model.quantumModel.quantumGate.QuantumGates;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QasmParser implements ImportParser, ExportParser {
    private final String qasmContent;

    public QasmParser(String qasmContent) {
        this.qasmContent = qasmContent;
    }

    public QasmParser() {
        this.qasmContent = "";
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
            while (indexMatcher.find()) {
                indices.add(Integer.parseInt(indexMatcher.group()));
            }
            if (indices.isEmpty()) {continue;}
            switch (operation) {
                case "h": circuit.addHadamard(indices.get(0)); break;
                case "x": circuit.addNot(indices.get(0)); break;
                case "y": circuit.addY(indices.get(0)); break;
                case "z": circuit.addZ(indices.get(0)); break;
                case "s": circuit.addS(indices.get(0)); break;
                case "t": circuit.addT(indices.get(0)); break;
                case "tdg": circuit.addTDagger(indices.get(0)); break;
                case "sdg": circuit.addSDagger(indices.get(0)); break;
                case "p": circuit.addPhase(indices.get(0)); break;
                case "rx": circuit.addRX(indices.get(0)); break;
                case "ry": circuit.addRY(indices.get(0)); break;
                case "rz": circuit.addRZ(indices.get(0)); break;
                case "sx": circuit.addXRoot(indices.get(0)); break;
                case "u": circuit.addU(indices.get(0)); break;
                case "cp": circuit.addControlled(QuantumGates.phase(), indices.get(0)); break;
                case "crx": circuit.addControlled(QuantumGates.rx(), indices.get(0)); break;
                case "cry": circuit.addControlled(QuantumGates.ry(), indices.get(0)); break;
                case "crz": circuit.addControlled(QuantumGates.rz(), indices.get(0)); break;
                case "csx": circuit.addControlled(QuantumGates.xRoot(), indices.get(0)); break;
                case "cu": circuit.addControlled(QuantumGates.u(), indices.get(0)); break;
                case "cx": circuit.addCNOT(indices.get(0), indices.get(1)); break;
                case "cy": circuit.addControlled(QuantumGates.y(), indices.get(0), indices.get(1)); break;
                case "cz": circuit.addControlled(QuantumGates.z(), indices.get(0), indices.get(1)); break;
                case "ch": circuit.addControlled(QuantumGates.hadamard(), indices.get(0), indices.get(1)); break;
                case "swap": circuit.addSwap(indices.get(0), indices.get(1)); break;
                case "ccx": circuit.addToffoli(indices.get(0), indices.get(1), indices.get(2)); break;
                case "cswap": circuit.addControlled(QuantumGates.swap(), indices.get(0), indices.get(1), indices.get(2)); break;
                case "c3x":
                case "c4x":
                    int target = indices.get(indices.size() - 1);
                    int[] controls = indices.subList(0, indices.size() - 1).stream().mapToInt(i -> i).toArray();
                    circuit.addControlled(QuantumGates.not(), target, controls);
                    break;
                case "measure":
                    int[] i = new int[indices.size()];
                    for (int q=0;q<indices.size();q++) {i[q] = indices.get(q);}
                    circuit.addMeasurement(i);
                    break;
            }
        }
        return circuit;
    }

    @Override
    public String serialize(QuantumCircuit circuit) {
        StringBuilder qasmBuilder = new StringBuilder();
        qasmBuilder.append("OPENQASM 2.0;\n");
        qasmBuilder.append("include \"qelib1.inc\";\n\n");
        qasmBuilder.append("qreg q[").append(circuit.getNQubits()).append("];\n");
        if (hasMeasurements(circuit)) {qasmBuilder.append("creg c[").append(circuit.getNQubits()).append("];\n");}
        qasmBuilder.append("\n");
        List<String> operations = convertCircuitToQasmOperations(circuit);
        for (String operation : operations) {qasmBuilder.append(operation).append("\n");}
        return qasmBuilder.toString();
    }

    private int extractQubitCount(String qasm) {
        Pattern qregPattern = Pattern.compile("qreg\\s+\\w+\\[(\\d+)\\];");
        Matcher matcher = qregPattern.matcher(qasm);
        if (matcher.find()) return Integer.parseInt(matcher.group(1));
        return 0;
    }

    private List<String> convertCircuitToQasmOperations(QuantumCircuit circuit) {
        List<String> operations = new ArrayList<>();
        for (CircuitLayer layer : circuit.getLayers()) {
            for (GateOperation gateOp : layer.getOperations()) {
                String gateName = gateOp.getGate().getName();
                int[] qubits = gateOp.getTargetQubits();
                String qasmInstruction = convertGateToQasm(gateName, qubits);
                if (qasmInstruction != null) {
                    operations.add(qasmInstruction);
                }
            }
        }
        return operations;
    }

    private String convertGateToQasm(String gateName, int[] qubits) {
        return switch (gateName) {
            case "Hadamard" -> "h q[" + qubits[0] + "];";
            case "NOT (Pauli-X)" -> "x q[" + qubits[0] + "];";
            case "Pauli-Y" -> "y q[" + qubits[0] + "];";
            case "Pauli-Z" -> "z q[" + qubits[0] + "];";
            case "S" -> "s q[" + qubits[0] + "];";
            case "T (π/8)" -> "t q[" + qubits[0] + "];";
            case "P" -> "p(pi/2) q[" + qubits[0] + "];";
            case "RX" -> "rx q[" + qubits[0] + "];";
            case "RZ" -> "rz q[" + qubits[0] + "];";
            case "RY" -> "ry q[" + qubits[0] + "];";
            case "√X" -> "sx q[" + qubits[0] + "];";
            case "T Dagger" -> "sdg q[" + qubits[0] + "];";
            case "S Dagger" -> "tdg q[" + qubits[0] + "];";
            case "U" -> "u q[" + qubits[0] + "];";
            case "CNOT" -> "cx q[" + qubits[0] + "],q[" + qubits[1] + "];";
            case "SWAP" -> "swap q[" + qubits[0] + "],q[" + qubits[1] + "];";
            case "Toffoli" -> "ccx q[" + qubits[0] + "],q[" + qubits[1] + "],q[" + qubits[2] + "];";
            case "Measurement" -> "measure q[" + qubits[0] + "] -> c[" + qubits[0] + "];";
            default -> {
                if (gateName.startsWith("C-")) {
                    String baseGate = gateName.substring(2);
                    yield convertControlledGateToQasm(baseGate, qubits);
                }
                yield "// Unknown gate: " + gateName;
            }
        };
    }

    private String convertControlledGateToQasm(String baseGate, int[] qubits) {
        if (qubits.length < 2) return null;
        return switch (baseGate) {
            case "Pauli-Y" -> "cy q[" + qubits[0] + "],q[" + qubits[1] + "];";
            case "Pauli-Z" -> "cz q[" + qubits[0] + "],q[" + qubits[1] + "];";
            case "Hadamard" -> "ch q[" + qubits[0] + "],q[" + qubits[1] + "];";
            case "SWAP" -> "cswap q[" + qubits[0] + "],q[" + qubits[1] + "],q[" + qubits[2] + "];";
            case "NOT (Pauli-X)" -> {
                if (qubits.length == 3) {
                    yield "ccx q[" + qubits[0] + "],q[" + qubits[1] + "],q[" + qubits[2] + "];";
                } else if (qubits.length > 3) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("c").append(qubits.length - 1).append("x ");
                    for (int i = 0; i < qubits.length; i++) {
                        if (i > 0) sb.append(",");
                        sb.append("q[").append(qubits[i]).append("]");
                    }
                    sb.append(";");
                    yield sb.toString();
                }
                yield null;
            }
            default -> "// Unknown controlled gate: C-" + baseGate;
        };
    }

    public String getQasmContent() {
        return qasmContent;
    }

    private boolean hasMeasurements(QuantumCircuit circuit) {
        for (CircuitLayer layer : circuit.getLayers()) {
            for (GateOperation gateOp : layer.getOperations()) {
                if ("Measurement".equals(gateOp.getGate().getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
