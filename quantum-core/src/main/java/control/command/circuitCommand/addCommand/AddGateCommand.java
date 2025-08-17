package control.command.circuitCommand.addCommand;

import control.command.UndoableCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumGate.GateOperation;
import model.quantumModel.quantumGate.QuantumGate;
import model.quantumModel.quantumGate.QuantumGates;

import java.util.List;

public class AddGateCommand implements UndoableCommand {
    private final QuantumCircuit circuit;
    private final GateType gateType;
    private final int[] targetQubits;
    private GateOperation addedOperation;
    private int layerIndex;
    private boolean wasExecuted;

    public AddGateCommand(QuantumCircuit circuit, GateType gateType, int... targetQubits) {
        validateInputs(circuit, gateType, targetQubits);
        this.circuit = circuit;
        this.gateType = gateType;
        this.targetQubits = targetQubits.clone();
        this.wasExecuted = false;
    }

    @Override
    public void execute() {
        try {
            QuantumGate gate = GateFactory.createGate(gateType);
            addGateToCircuit(gate);
            trackAddedOperation();
            wasExecuted = true;
        } catch (Exception e) {
            wasExecuted = false;
            throw new IllegalStateException("Failed to add gate: " + e.getMessage(), e);
        }
    }

    @Override
    public void undo() {
        if (!canUndo()) return;

        try {
            removeOperationFromCircuit();
            wasExecuted = false;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to undo gate addition: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean canUndo() {
        return wasExecuted && addedOperation != null;
    }

    @Override
    public void redo() {
        execute();
    }

    private void validateInputs(QuantumCircuit circuit, GateType gateType, int[] targetQubits) {
        if (circuit == null) throw new IllegalArgumentException("Circuit cannot be null");
        if (gateType == null) throw new IllegalArgumentException("Gate type cannot be null");
        if (targetQubits == null || targetQubits.length == 0) {
            throw new IllegalArgumentException("Target qubits cannot be null or empty");
        }

        // Validate qubit indices
        for (int qubit : targetQubits) {
            if (qubit < 0 || qubit >= circuit.getNQubits()) {
                throw new IllegalArgumentException("Invalid qubit index: " + qubit);
            }
        }

        // Validate gate requirements
        if (!gateType.isValidForQubits(targetQubits.length)) {
            throw new IllegalArgumentException(
                    "Gate " + gateType + " requires " + gateType.getRequiredQubits() +
                            " qubits, but " + targetQubits.length + " were provided"
            );
        }
    }

    private void addGateToCircuit(QuantumGate gate) {
        switch (gateType) {
            case HADAMARD -> circuit.addHadamard(targetQubits[0]);
            case PAULI_X -> circuit.addNot(targetQubits[0]);
            case PAULI_Y -> circuit.addY(targetQubits[0]);
            case PAULI_Z -> circuit.addZ(targetQubits[0]);
            case T_GATE -> circuit.addT(targetQubits[0]);
            case S_GATE -> circuit.addPhase(targetQubits[0]);
            case CNOT -> circuit.addCNOT(targetQubits[0], targetQubits[1]);
            case SWAP -> circuit.addSwap(targetQubits[0], targetQubits[1]);
            case TOFFOLI -> circuit.addToffoli(targetQubits[0], targetQubits[1], targetQubits[2]);
            default -> throw new UnsupportedOperationException("Gate type not supported: " + gateType);
        }
    }

    private void trackAddedOperation() {
        List<CircuitLayer> layers = circuit.getLayers();
        if (!layers.isEmpty()) {
            CircuitLayer lastLayer = layers.get(layers.size() - 1);
            List<GateOperation> operations = lastLayer.getOperations();
            if (!operations.isEmpty()) {
                this.addedOperation = operations.get(operations.size() - 1);
                this.layerIndex = layers.size() - 1;
            }
        }
    }

    private void removeOperationFromCircuit() {
        List<CircuitLayer> layers = circuit.getLayers();
        if (layerIndex < layers.size()) {
            CircuitLayer layer = layers.get(layerIndex);
            layer.getOperations().remove(addedOperation);
            if (layer.getOperations().isEmpty()) {
                layers.remove(layerIndex);
            }
        }
    }
}
