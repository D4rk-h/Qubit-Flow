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

import model.mathModel.Complex;
import model.mathModel.Matrix;
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
        else applyGeneralGate(state);
    }

    private void applyGeneralGate(QuantumState state) {
        Matrix gateMatrix = gate.getMatrix();
        Complex[] amplitudes = state.getAmplitudes();
        Complex[] newAmplitudes = new Complex[amplitudes.length];
        Arrays.fill(newAmplitudes, Complex.ZERO);
        if (gate.getNumQubits() == 1) {
            int targetQubit = targetQubits[0];
            applySingleQubitMatrix(amplitudes, newAmplitudes, gateMatrix, targetQubit);
        } else applyMultiQubitMatrix(amplitudes, newAmplitudes, gateMatrix, targetQubits);
        System.arraycopy(newAmplitudes, 0, amplitudes, 0, amplitudes.length);
    }

    private void applySingleQubitMatrix(Complex[] amplitudes, Complex[] newAmplitudes, Matrix gate, int targetQubit) {
        Complex g00 = gate.get(0, 0);
        Complex g01 = gate.get(0, 1);
        Complex g10 = gate.get(1, 0);
        Complex g11 = gate.get(1, 1);
        int mask = 1 << targetQubit;
        for (int i = 0; i < amplitudes.length; i += 2 * mask) {
            for (int j = 0; j < mask; j++) {
                Complex a0 = amplitudes[i + j];
                Complex a1 = amplitudes[i + j + mask];
                newAmplitudes[i + j] = g00.multiply(a0).add(g01.multiply(a1));
                newAmplitudes[i + j + mask] = g10.multiply(a0).add(g11.multiply(a1));
            }
        }
    }

    private void applyMultiQubitMatrix(Complex[] amplitudes, Complex[] newAmplitudes, Matrix gate, int[] targets) {
        int gateSize = gate.getRows();
        for (int i=0; i < amplitudes.length; i++) {
            if (amplitudes[i].magnitude() < Complex.EPSILON) continue;
            int targetBits = 0;
            for (int k=0; k < targets.length; k++) if ((i & (1 << targets[k])) != 0) targetBits |= (1 << k);
            for (int gateRow=0; gateRow < gateSize; gateRow++) {
                Complex matrixElement = gate.get(gateRow, targetBits);
                if (matrixElement.magnitude() < Complex.EPSILON) continue;
                int j = i;
                for (int k=0; k < targets.length; k++) {
                    j &= ~(1 << targets[k]);
                    if ((gateRow & (1 << k)) != 0) j |= (1 << targets[k]);
                }
                newAmplitudes[j] = newAmplitudes[j].add(matrixElement.multiply(amplitudes[i]));
            }
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