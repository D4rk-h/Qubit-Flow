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
        else state.applyGate(gate);
    }

    private boolean isOptimizedGate(String gateName) {
        return Set.of("Hadamard", "NOT (Pauli-X)", "Pauli-Y", "Pauli-Z", "T (π/8)", "Phase").contains(gateName);
    }

    private void applyOptimized(QuantumState state) {
        if (targetQubits.length != 1) throw new IllegalArgumentException(gate.getName() + " requires exactly 1 qubit");
        switch(gate.getName()) {
            case "Hadamard" -> state.applyHadamard(targetQubits[0]);
            case "NOT (Pauli-X)" -> state.applyNot(targetQubits[0]);
            case "Pauli-Y" -> state.applyY(targetQubits[0]);
            case "Pauli-Z" -> state.applyZ(targetQubits[0]);
            case "T (π/8)" -> state.applyT(targetQubits[0]);
            case "Phase" -> state.applyPhase(targetQubits[0]);
            default -> state.applyGate(gate);
        }
    }

    private void validateGateQubits(QuantumGate gate, int[] qubits) {
        if (gate.getNumQubits() != qubits.length) throw new IllegalArgumentException("Quantum gate must have same qubits as which it applies");
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
