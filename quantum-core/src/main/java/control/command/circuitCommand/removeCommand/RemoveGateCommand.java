package control.command.circuitCommand.removeCommand;

import control.command.UndoableCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumGate.GateOperation;

import java.util.List;

public class RemoveGateCommand implements UndoableCommand {
    private final QuantumCircuit circuit;
    private final GateOperation operationToRemove;
    private final int originalLayerIndex;
    private boolean wasExecuted;
    private CircuitLayer originalLayer;

    public RemoveGateCommand(QuantumCircuit circuit, GateOperation operation, int layerIndex) {
        this.circuit = circuit;
        this.operationToRemove = operation;
        this.originalLayerIndex = layerIndex;
        this.wasExecuted = false;
    }

    @Override
    public void execute() {
        List<CircuitLayer> layers = circuit.getLayers();
        if (originalLayerIndex >= 0 && originalLayerIndex < layers.size()) {
            originalLayer = layers.get(originalLayerIndex);
            boolean removed = originalLayer.getOperations().remove(operationToRemove);

            if (removed) {
                if (originalLayer.getOperations().isEmpty()) {
                    layers.remove(originalLayerIndex);
                }
                wasExecuted = true;
            }
        }
    }

    @Override
    public void undo() {
        if (!canUndo()) return;

        if (originalLayer.getOperations().isEmpty()) {
            // Re-add the layer if it was removed
            circuit.getLayers().add(originalLayerIndex, originalLayer);
        }
        originalLayer.addOperation(operationToRemove);
        wasExecuted = false;
    }

    @Override
    public boolean canUndo() {
        return wasExecuted && originalLayer != null;
    }

    @Override
    public void redo() {
        execute();
    }
}