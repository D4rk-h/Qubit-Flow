package model.quantumModel.quantumGate;

import model.quantumModel.quantumState.QuantumState;

import java.util.Arrays;

public class GateOperation {
    private QuantumGate gate;
    private int[] targetQubits;

    public GateOperation(QuantumGate gate, int... targetQubits) {
        this.gate = gate;
        this.targetQubits = Arrays.copyOf(targetQubits, targetQubits.length);
        validateGateQubits(gate, targetQubits);
    }

    public void applyTo(QuantumState state) {
        switch(gate.getName()) {
            case "Hadamard":
                if (targetQubits.length != 1) throw new IllegalArgumentException("Hadamard gate requires exactly 1 qubit");
                state.applyHadamard(targetQubits[0]);
                break;
            case "NOT (Pauli-X)":
                if (targetQubits.length != 1) throw new IllegalArgumentException("NOT gate requires exactly 1 qubit");
                state.applyNot(targetQubits[0]);
                break;
            case "Pauli-Y":
                if (targetQubits.length != 1) throw new IllegalArgumentException("Pauli-Y gate requires exactly 1 qubit");
                state.applyY(targetQubits[0]);
                break;
            case "Pauli-Z":
                if (targetQubits.length != 1) throw new IllegalArgumentException("Pauli-Z gate requires exactly 1 qubit");
                state.applyZ(targetQubits[0]);
                break;
            case "T (π/8)":
                if (targetQubits.length != 1) throw new IllegalArgumentException("T gate requires exactly 1 qubit");
                state.applyT(targetQubits[0]);
                break;
            case "Phase":
                if (targetQubits.length != 1) throw new IllegalArgumentException("Phase gate requires exactly 1 qubit");
                state.applyPhase(targetQubits[0]);
                break;
            case "CNOT":
                if (targetQubits.length != 2) throw new IllegalArgumentException("CNOT gate requires exactly 2 qubits (control, target)");
                state.applyCNOT(targetQubits[0], targetQubits[1]);
                break;
            case "Swap":
                if (targetQubits.length != 2) throw new IllegalArgumentException("SWAP gate requires exactly 2 qubits");
                state.applySwap(targetQubits[0], targetQubits[1]);
                break;
            case "Toffoli":
                if (targetQubits.length != 3) throw new IllegalArgumentException("Toffoli gate requires exactly 3 qubits (control1, control2, target)");
                state.applyToffoli(targetQubits[0], targetQubits[1], targetQubits[2]);
                break;
            default:
                state.applyGate(gate);
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
