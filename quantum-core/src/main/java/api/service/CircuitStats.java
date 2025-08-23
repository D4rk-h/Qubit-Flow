package api.service;

import java.util.List;

public record CircuitStats(
        int qubits,
        int depth,
        int totalGates,
        List<String> gateTypes
) {
    @Override
    public String toString() {
        return String.format("CircuitStats{qubits=%d, depth=%d, totalGates=%d, gateTypes=%s}",
                qubits, depth, totalGates, gateTypes);
    }
}