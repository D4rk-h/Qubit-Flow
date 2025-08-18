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

package control.command.add;

import control.command.ports.UndoableCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumGate.MeasurementGate;
import model.quantumModel.quantumGate.MeasurementOperation;
import model.quantumModel.quantumState.QuantumState;
import model.quantumModel.quantumState.quantumStateUtils.MeasurementResult;

import java.util.List;

public class AddMeasurementCommand implements UndoableCommand {
    private final QuantumCircuit circuit;
    private final int[] targetQubits;
    private final QuantumState originalState;
    private MeasurementOperation addedOperation;
    private CircuitLayer targetLayer;
    private boolean wasExecuted;
    private QuantumState stateBeforeMeasurement;
    private MeasurementResult measurementResult;

    public AddMeasurementCommand(QuantumCircuit circuit, QuantumState currentState, int... targetQubits) {
        validateInputs(circuit, currentState, targetQubits);
        this.circuit = circuit;
        this.originalState = currentState.clone();
        this.targetQubits = targetQubits.clone();
        this.wasExecuted = false;
    }

    @Override
    public void execute() {
        if (wasExecuted) return;
        try {
            stateBeforeMeasurement = originalState.clone();
            MeasurementGate measurementGate = new MeasurementGate(targetQubits);
            MeasurementOperation measurementOp = new MeasurementOperation(measurementGate, targetQubits);
            addMeasurementToCircuit(measurementOp);
            this.addedOperation = measurementOp;
            wasExecuted = true;
        } catch (Exception e) {
            wasExecuted = false;
            throw new IllegalStateException("Failed to add measurement: " + e.getMessage(), e);
        }
    }

    @Override
    public void undo() {
        if (!canUndo()) return;
        try {
            removeOperationFromCircuit();
            this.addedOperation = null;
            this.targetLayer = null;
            this.measurementResult = null;
            wasExecuted = false;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to undo measurement: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean canUndo() {return wasExecuted && addedOperation != null && targetLayer != null;}

    @Override
    public void redo() {
        if (wasExecuted) return;
        execute();
    }

    public MeasurementResult getMeasurementResult() {
        return addedOperation != null ? addedOperation.getMeasurementResult() : measurementResult;
    }

    public QuantumState getStateBeforeMeasurement() {
        return stateBeforeMeasurement != null ? stateBeforeMeasurement.clone() : null;
    }

    public QuantumState getOriginalState() {return originalState.clone();}

    public int[] getTargetQubits() {return targetQubits.clone();}

    public MeasurementOperation getAddedOperation() {return addedOperation;}

    public boolean wasExecuted() {return wasExecuted;}

    private void validateInputs(QuantumCircuit circuit, QuantumState state, int[] targetQubits) {
        if (circuit == null) {
            throw new IllegalArgumentException("Circuit cannot be null");
        }
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        }
        if (targetQubits == null || targetQubits.length == 0) {
            throw new IllegalArgumentException("Target qubits cannot be null or empty");
        }
        for (int qubit : targetQubits) {
            if (qubit < 0 || qubit >= circuit.getNQubits()) {
                throw new IllegalArgumentException("Invalid qubit index: " + qubit +
                        ". Must be between 0 and " + (circuit.getNQubits() - 1));
            }
        }
        for (int i = 0; i < targetQubits.length; i++) {
            for (int j = i + 1; j < targetQubits.length; j++) {
                if (targetQubits[i] == targetQubits[j]) {
                    throw new IllegalArgumentException("Duplicate qubit index: " + targetQubits[i]);
                }
            }
        }
        if (state.getNumQubits() != circuit.getNQubits()) {
            throw new IllegalArgumentException(
                    "State must have same number of qubits as circuit. " +
                            "Circuit: " + circuit.getNQubits() + ", State: " + state.getNumQubits()
            );
        }
    }

    private void addMeasurementToCircuit(MeasurementOperation measurementOp) {
        List<CircuitLayer> layers = circuit.getLayers();
        CircuitLayer availableLayer = null;
        for (CircuitLayer layer : layers) {
            if (!layer.hasConflictWith(targetQubits)) {
                availableLayer = layer;
                break;
            }
        }
        if (availableLayer == null) {
            availableLayer = new CircuitLayer();
            layers.add(availableLayer);
        }
        availableLayer.addOperation(measurementOp);
        this.targetLayer = availableLayer;
    }

    private void removeOperationFromCircuit() {
        if (targetLayer == null || addedOperation == null) {
            return;
        }
        targetLayer.getOperations().remove(addedOperation);
        if (targetLayer.getOperations().isEmpty()) {
            circuit.getLayers().remove(targetLayer);
        }
    }
}
