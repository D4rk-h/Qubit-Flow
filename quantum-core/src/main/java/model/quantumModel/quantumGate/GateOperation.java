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

    public void applyTo(QuantumState state) {gate.apply(state);}

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
