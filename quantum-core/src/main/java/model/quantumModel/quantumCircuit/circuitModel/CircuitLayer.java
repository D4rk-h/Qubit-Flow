package model.quantumModel.quantumCircuit.circuitModel;

import model.quantumModel.quantumGate.GateOperation;
import model.quantumModel.quantumState.QuantumState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CircuitLayer {
    private Set<Integer> usedQubits;
    private List<GateOperation> operations;

    public CircuitLayer() {
        this.usedQubits = new HashSet<>();
        this.operations = new ArrayList<>();
    }

    public boolean hasConflictWith(int... qubits) {
        for (int qubit : qubits) if (usedQubits.contains(qubit)) return true;
        return false;
    }

    public void executeOn(QuantumState state) {for (GateOperation operation : operations) operation.applyTo(state);}

    public void addOperation(GateOperation operation) {
        if (hasConflictWith(operation.getTargetQubits())) throw new IllegalArgumentException("Qubit conflict found");
        operations.add(operation);
        for (int qubit: operation.getTargetQubits()) usedQubits.add(qubit);
    }

    public int getOperationCount() {return operations.size();}
    public List<GateOperation> getOperations() {return operations;}
    public void setOperations(List<GateOperation> operations) {this.operations = operations;}
    public Set<Integer> getUsedQubits() {return usedQubits;}
    public void setUsedQubits(Set<Integer> usedQubits) {this.usedQubits = usedQubits;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < operations.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(operations.get(i).toString());
        }
        sb.append("]");
        return sb.toString();
    }
}
