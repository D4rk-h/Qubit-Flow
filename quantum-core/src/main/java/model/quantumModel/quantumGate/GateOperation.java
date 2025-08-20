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
        return Set.of("Hadamard", "NOT (Pauli-X)", "Pauli-Y", "Pauli-Z", "T (π/8)", "Phase",
                "CNOT", "SWAP", "Toffoli").contains(gateName);
    }

    private void applyOptimized(QuantumState state) {
        switch(gate.getName()) {
            case "Hadamard" -> {
                if (targetQubits.length != 1) throw new IllegalArgumentException(gate.getName() + " requires exactly 1 qubit");
                state.applyHadamard(targetQubits[0]);
            }
            case "NOT (Pauli-X)" -> {
                if (targetQubits.length != 1) throw new IllegalArgumentException(gate.getName() + " requires exactly 1 qubit");
                state.applyNot(targetQubits[0]);
            }
            case "Pauli-Y" -> {
                if (targetQubits.length != 1) throw new IllegalArgumentException(gate.getName() + " requires exactly 1 qubit");
                state.applyY(targetQubits[0]);
            }
            case "Pauli-Z" -> {
                if (targetQubits.length != 1) throw new IllegalArgumentException(gate.getName() + " requires exactly 1 qubit");
                state.applyZ(targetQubits[0]);
            }
            case "T (π/8)" -> {
                if (targetQubits.length != 1) throw new IllegalArgumentException(gate.getName() + " requires exactly 1 qubit");
                state.applyT(targetQubits[0]);
            }
            case "Phase" -> {
                if (targetQubits.length != 1) throw new IllegalArgumentException(gate.getName() + " requires exactly 1 qubit");
                state.applyPhase(targetQubits[0]);
            }
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

    private void validateGateQubits(QuantumGate gate, int[] qubits) {
        if (gate.getNumQubits() != qubits.length) throw new IllegalArgumentException("Quantum gate must have same qubits as which it applies");
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
