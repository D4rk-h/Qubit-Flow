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

package model.quantumModel.quantumGate;

import model.quantumModel.quantumState.QuantumState;

import java.util.Arrays;
import java.util.Set;

public class GateOperation {
    private QuantumGate gate;
    private int[] targetQubits;

    public GateOperation(QuantumGate gate, int... targetQubits) {
        this.gate = gate;
        this.targetQubits = Arrays.copyOf(targetQubits, targetQubits.length);
        validateGateQubits(gate, targetQubits);
    }

    public void applyTo(QuantumState state) {
        if (isOptimizedGate(gate.getName())) applyOptimized(state);
        else {
            gate.setUpdatedMatrix(gate.getMatrix().extendToMultiQubitGate(state.getNumQubits(), targetQubits));
            state.applyGate(gate);
        }
    }

    private boolean isOptimizedGate(String gateName) {
        return Set.of("Hadamard", "NOT (Pauli-X)", "Pauli-Y", "Pauli-Z", "T (π/8)", "S", "T Dagger", "S Dagger",
                "CNOT", "SWAP", "Toffoli", "√X").contains(gateName) ||
                gateName.startsWith("U(") || gateName.startsWith("RZ(") || gateName.startsWith("RY(") ||
                gateName.startsWith("RX(") || gateName.startsWith("P(");
    }

    private void applyOptimized(QuantumState state) {
        String gateName = gate.getName();
        if (targetQubits.length == 1) {
            int qubit = targetQubits[0];
            switch(gateName) {
                case "Hadamard" -> state.applyHadamard(qubit);
                case "NOT (Pauli-X)" -> state.applyNot(qubit);
                case "Pauli-Y" -> state.applyY(qubit);
                case "Pauli-Z" -> state.applyZ(qubit);
                case "T (π/8)" -> state.applyT(qubit);
                case "S" -> state.applyS(qubit);
                case "T Dagger" -> state.applyTDagger(qubit);
                case "S Dagger" -> state.applySDagger(qubit);
                case "√X" -> state.applyXRoot(qubit);
                default -> {
                    if (gateName.startsWith("U(")) {
                        String params = gateName.substring(2, gateName.length() - 1);
                        String[] values = params.split(",");
                        if (values.length == 3) {
                            double theta = Double.parseDouble(values[0]);
                            double phi = Double.parseDouble(values[1]);
                            double lambda = Double.parseDouble(values[2]);
                            state.applyUnitary(qubit, theta, phi, lambda);
                        } else {
                            state.applyGate(gate);
                        }
                    } else if (gateName.startsWith("RZ(")) {
                        String param = gateName.substring(3, gateName.length() - 1);
                        double phi = Double.parseDouble(param);
                        state.applyRZ(qubit, phi);
                    } else if (gateName.startsWith("RY(")) {
                        String param = gateName.substring(3, gateName.length() - 1);
                        double theta = Double.parseDouble(param);
                        state.applyRY(qubit, theta);
                    } else if (gateName.startsWith("RX(")) {
                        String param = gateName.substring(3, gateName.length() - 1);
                        double theta = Double.parseDouble(param);
                        state.applyRX(qubit, theta);
                    } else if (gateName.startsWith("P(")) {
                        String param = gateName.substring(2, gateName.length() - 1);
                        double phi = Double.parseDouble(param);
                        state.applyPhaseShift(qubit, phi);
                    } else {
                        state.applyGate(gate);
                    }
                }
            }
        }
        else {
            switch(gateName) {
                case "CNOT" -> {
                    if (targetQubits.length != 2) throw new IllegalArgumentException("CNOT requires exactly 2 qubits");
                    state.applyCNOT(targetQubits[0], targetQubits[1]);
                }
                case "SWAP" -> {
                    if (targetQubits.length != 2) throw new IllegalArgumentException("SWAP requires exactly 2 qubits");
                    state.applySwap(targetQubits[0], targetQubits[1]);
                }
                case "Toffoli" -> {
                    if (targetQubits.length != 3) throw new IllegalArgumentException("Toffoli requires exactly 3 qubits");
                    state.applyToffoli(targetQubits[0], targetQubits[1], targetQubits[2]);
                }
                default -> state.applyGate(gate);
            }
        }
    }

    private void validateGateQubits(QuantumGate gate, int[] qubits) {
        if (gate.getNumQubits() != qubits.length) {
            throw new IllegalArgumentException("Quantum gate must have same qubits as which it applies");
        }
    }

    @Override
    public GateOperation clone() {
        return new GateOperation(this.gate, this.targetQubits);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(gate.getName());
        sb.append("(");
        for (int i = 0; i < targetQubits.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(targetQubits[i]);
        }
        sb.append(")");
        return sb.toString();
    }

    public QuantumGate getGate() {return gate;}

    public void setGate(QuantumGate gate) {this.gate = gate;}

    public int[] getTargetQubits() {return Arrays.copyOf(targetQubits, targetQubits.length);}

    public void setTargetQubits(int[] targetQubits) {this.targetQubits = targetQubits;}
}