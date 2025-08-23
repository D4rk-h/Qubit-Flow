package api.utils;

import java.util.ArrayList;
import java.util.List;

public record CircuitSummary(int qubits, int depth, int gateCount, List<String> gateTypes) {
    public CircuitSummary(int qubits, int depth, int gateCount, List<String> gateTypes) {
        this.qubits = qubits;
        this.depth = depth;
        this.gateCount = gateCount;
        this.gateTypes = new ArrayList<>(gateTypes);
    }

    @Override
    public List<String> gateTypes() {return new ArrayList<>(gateTypes);}

    @Override
    public String toString() {
        return "CircuitSummary{" +
                "qubits=" + qubits +
                ", depth=" + depth +
                ", gateCount=" + gateCount +
                ", gateTypes=" + gateTypes +
                '}';
    }
}