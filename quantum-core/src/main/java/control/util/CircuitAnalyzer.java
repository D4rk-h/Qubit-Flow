package control.util;

import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumGate.GateOperation;
import model.quantumModel.quantumState.QuantumState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CircuitAnalyzer {

    public static GateOperation findLastGateOnQubit(QuantumCircuit circuit, int qubit) {
        List<CircuitLayer> layers = circuit.getLayers();

        for (int i = layers.size() - 1; i >= 0; i--) {
            CircuitLayer layer = layers.get(i);
            List<GateOperation> operations = layer.getOperations();

            for (int j = operations.size() - 1; j >= 0; j--) {
                GateOperation operation = operations.get(j);
                if (operationTargetsQubit(operation, qubit)) {
                    return operation;
                }
            }
        }
        return null;
    }

    /**
     * Get all gate operations that target a specific qubit
     */
    public static List<GateOperation> getGatesOnQubit(QuantumCircuit circuit, int qubit) {
        List<GateOperation> result = new ArrayList<>();

        for (CircuitLayer layer : circuit.getLayers()) {
            for (GateOperation operation : layer.getOperations()) {
                if (operationTargetsQubit(operation, qubit)) {
                    result.add(operation);
                }
            }
        }

        return result;
    }

    /**
     * Get total number of operations in the circuit
     */
    public static int getTotalOperations(QuantumCircuit circuit) {
        return circuit.getLayers().stream()
                .mapToInt(layer -> layer.getOperations().size())
                .sum();
    }

    /**
     * Check if circuit can be executed on given state
     */
    public static boolean canExecuteOn(QuantumCircuit circuit, QuantumState state) {
        return circuit.getNQubits() == state.getNumQubits();
    }

    /**
     * Get operations that are compatible with given number of qubits
     */
    public static List<GateOperation> getCompatibleOperations(
            QuantumCircuit circuit, int maxQubits) {

        return circuit.getLayers().stream()
                .flatMap(layer -> layer.getOperations().stream())
                .filter(op -> allQubitsValid(op.getTargetQubits(), maxQubits))
                .collect(Collectors.toList());
    }

    /**
     * Create circuit statistics summary
     */
    public static CircuitStats analyzeCircuit(QuantumCircuit circuit) {
        return new CircuitStats(
                circuit.getNQubits(),
                circuit.getDepth(),
                circuit.getTotalGateCount(),
                getTotalOperations(circuit),
                getLayerDistribution(circuit)
        );
    }

    private static boolean operationTargetsQubit(GateOperation operation, int qubit) {
        return Arrays.stream(operation.getTargetQubits()).anyMatch(q -> q == qubit);
    }

    private static boolean allQubitsValid(int[] qubits, int maxQubits) {
        return Arrays.stream(qubits).allMatch(q -> q < maxQubits);
    }

    private static List<Integer> getLayerDistribution(QuantumCircuit circuit) {
        return circuit.getLayers().stream()
                .map(layer -> layer.getOperations().size())
                .collect(Collectors.toList());
    }

    public static class CircuitStats {
        private final int qubits;
        private final int depth;
        private final int totalGates;
        private final int totalOperations;
        private final List<Integer> layerDistribution;

        public CircuitStats(int qubits, int depth, int totalGates,
                            int totalOperations, List<Integer> layerDistribution) {
            this.qubits = qubits;
            this.depth = depth;
            this.totalGates = totalGates;
            this.totalOperations = totalOperations;
            this.layerDistribution = new ArrayList<>(layerDistribution);
        }

        public int getQubits() { return qubits; }
        public int getDepth() { return depth; }
        public int getTotalGates() { return totalGates; }
        public int getTotalOperations() { return totalOperations; }
        public List<Integer> getLayerDistribution() { return new ArrayList<>(layerDistribution); }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Circuit Statistics:\n");
            sb.append("- Qubits: ").append(qubits).append("\n");
            sb.append("- Depth: ").append(depth).append("\n");
            sb.append("- Total Gates: ").append(totalGates).append("\n");
            sb.append("- Total Operations: ").append(totalOperations).append("\n");
            sb.append("- Average Operations per Layer: ")
                    .append(depth > 0 ? (double) totalOperations / depth : 0).append("\n");

            return sb.toString();
        }
    }
}