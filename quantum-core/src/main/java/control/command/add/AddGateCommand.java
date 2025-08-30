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
import control.command.gate.GateFactory;
import control.command.gate.GateType;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumGate.GateOperation;
import model.quantumModel.quantumGate.QuantumGate;

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
        for (int qubit : targetQubits) {
            if (qubit < 0 || qubit >= circuit.getNQubits()) {
                throw new IllegalArgumentException("Invalid qubit index: " + qubit);
            }
        }
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
            case S_GATE -> circuit.addS(targetQubits[0]);
            case T_DAGGER -> circuit.addTDagger(targetQubits[0]);
            case S_DAGGER -> circuit.addSDagger(targetQubits[0]);
            case PHASE -> circuit.addPhase(targetQubits[0]);
            case U_GATE -> circuit.addU(targetQubits[0]);
            case X_ROOT -> circuit.addXRoot(targetQubits[0]);
            case RX_GATE -> circuit.addRX(targetQubits[0]);
            case RZ_GATE -> circuit.addRZ(targetQubits[0]);
            case RY_GATE -> circuit.addRY(targetQubits[0]);
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
