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

public class QiskitParser implements ImportParser, ExportParser {
    private final String qiskitContent;

    public QiskitParser(String qiskitContent) {
        this.qiskitContent = qiskitContent;
    }

    public QiskitParser() {
        this.qiskitContent = "";
    }

    @Override
    public QuantumCircuit parse() {
        String qiskitContent = getQiskitContent();
        int nQubit = extractQubitCount(qiskitContent);
        String circuitVar = extractCircuitVarName(qiskitContent);
        if (nQubit == 0 || circuitVar == null) return new QuantumCircuit(0);
        QuantumCircuit circuit = new QuantumCircuit(nQubit);
        final Pattern indexPattern = Pattern.compile("\\d+");
        final Pattern instructionPattern = Pattern.compile(
                "^\\s*" + Pattern.quote(circuitVar) + "\\.(\\w+)\\(([^)]*)\\)"
        );
        String[] lines = qiskitContent.split("\\R");
        for (String line : lines) {
            Matcher matcher = instructionPattern.matcher(line.trim());
            if (matcher.find()) {
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
                    case "measure":
                        circuit.addMeasurement(indices.get(0), indices.get(1));
                        break;
                }
            }
        }
        return circuit;
    }

    @Override
    public String serialize(QuantumCircuit circuit) {
        StringBuilder qiskitBuilder = new StringBuilder();
        qiskitBuilder.append("from qiskit import QuantumRegister, ClassicalRegister, QuantumCircuit\n");
        qiskitBuilder.append("from numpy import pi\n\n");
        qiskitBuilder.append("qreg_q = QuantumRegister(").append(circuit.getNQubits()).append(", 'q')\n");
        if (hasMeasurements(circuit)) {
            qiskitBuilder.append("creg_c = ClassicalRegister(").append(circuit.getNQubits()).append(", 'c')\n");
            qiskitBuilder.append("circuit = QuantumCircuit(qreg_q, creg_c)\n\n");
        } else {
            qiskitBuilder.append("circuit = QuantumCircuit(qreg_q)\n\n");
        }
        List<String> operations = convertCircuitToQiskitOperations(circuit);
        for (String operation : operations) qiskitBuilder.append(operation).append("\n");
        return qiskitBuilder.toString();
    }

    private int extractQubitCount(String qiskitContent) {
        Pattern qregPattern = Pattern.compile("QuantumRegister\\s*\\(\\s*(\\d+)");
        Matcher matcher = qregPattern.matcher(qiskitContent);
        if (matcher.find()) return Integer.parseInt(matcher.group(1));
        return 0;
    }

    private String extractCircuitVarName(String qiskitContent) {
        Pattern circuitVarPattern = Pattern.compile("(\\w+)\\s*=\\s*QuantumCircuit");
        Matcher matcher = circuitVarPattern.matcher(qiskitContent);
        if (matcher.find()) return matcher.group(1);
        return null;
    }

    private List<String> convertCircuitToQiskitOperations(QuantumCircuit circuit) {
        List<String> operations = new ArrayList<>();
        for (CircuitLayer layer : circuit.getLayers()) {
            for (GateOperation gateOp : layer.getOperations()) {
                String gateName = gateOp.getGate().getName();
                int[] qubits = gateOp.getTargetQubits();
                String qiskitInstruction = convertGateToQiskit(gateName, qubits);
                if (qiskitInstruction != null) {
                    operations.add(qiskitInstruction);
                }
            }
        }
        return operations;
    }

    private String convertGateToQiskit(String gateName, int[] qubits) {
        return switch (gateName) {
            case "Hadamard" -> "circuit.h(qreg_q[" + qubits[0] + "])";
            case "NOT (Pauli-X)" -> "circuit.x(qreg_q[" + qubits[0] + "])";
            case "Pauli-Y" -> "circuit.y(qreg_q[" + qubits[0] + "])";
            case "Pauli-Z" -> "circuit.z(qreg_q[" + qubits[0] + "])";
            case "S" -> "circuit.s(qreg_q[" + qubits[0] + "])";
            case "T (π/8)" -> "circuit.t(qreg_q[" + qubits[0] + "])";
            case "Phase" -> "circuit.p(qreg_q[" + qubits[0] + "])";
            case "T Dagger" -> "circuit.tdg(qreg_q[" + qubits[0] + "])";
            case "S Dagger" -> "circuit.sdg(qreg_q[" + qubits[0] + "])";
            case "RX" -> "circuit.rx(qreg_q[" + qubits[0] + "])";
            case "RY" -> "circuit.ry(qreg_q[" + qubits[0] + "])";
            case "RZ" -> "circuit.t(qreg_rz[" + qubits[0] + "])";
            case "√X" -> "circuit.s(qreg_sx[" + qubits[0] + "])";
            case "U" -> "circuit.u(qreg_q[" + qubits[0] + "])";
            case "CNOT" -> "circuit.cx(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "])";
            case "SWAP" -> "circuit.swap(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "])";
            case "Toffoli" -> "circuit.ccx(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "], qreg_q[" + qubits[2] + "])";
            case "Measurement" -> "circuit.measure(qreg_q[" + qubits[0] + "], creg_c[" + qubits[0] + "])";
            default -> {
                if (gateName.startsWith("C-")) {
                    String baseGate = gateName.substring(2);
                    yield convertControlledGateToQiskit(baseGate, qubits);
                }
                yield "# Unknown gate: " + gateName;
            }
        };
    }

    private String convertControlledGateToQiskit(String baseGate, int[] qubits) {
        if (qubits.length < 2) return null;
        return switch (baseGate) {
            case "Pauli-Y" -> "circuit.cy(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "])";
            case "Pauli-Z" -> "circuit.cz(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "])";
            case "Hadamard" -> "circuit.ch(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "])";
            case "Phase" -> "circuit.cp(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "])";
            case "RX" -> "circuit.crx(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "])";
            case "RY" -> "circuit.cry(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "])";
            case "RZ" -> "circuit.ct(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "])";
            case "√X" -> "circuit.cs(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "])";
            case "U" -> "circuit.cu(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "])";
            case "SWAP" -> "circuit.cswap(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "], qreg_q[" + qubits[2] + "])";
            case "NOT (Pauli-X)" -> {
                if (qubits.length == 3) {
                    yield "circuit.ccx(qreg_q[" + qubits[0] + "], qreg_q[" + qubits[1] + "], qreg_q[" + qubits[2] + "])";
                } else if (qubits.length > 3) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("circuit.mcx([");
                    for (int i = 0; i < qubits.length - 1; i++) {
                        if (i > 0) sb.append(", ");
                        sb.append("qreg_q[").append(qubits[i]).append("]");
                    }
                    sb.append("], qreg_q[").append(qubits[qubits.length - 1]).append("])");
                    yield sb.toString();
                }
                yield null;
            }
            default -> "# Unknown controlled gate: C-" + baseGate;
        };
    }

    public String getQiskitContent() {return qiskitContent;}

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