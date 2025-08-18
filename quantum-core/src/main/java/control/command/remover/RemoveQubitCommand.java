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

package control.command.remover;

import control.command.ports.UndoableCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumState.QuantumState;

import java.util.Arrays;

public class RemoveQubitCommand implements UndoableCommand {
    private final QuantumCircuit originalCircuit;
    private final QuantumState originalState;
    private final int originalQubits;
    private QuantumCircuit newCircuit;
    private QuantumState newState;
    private boolean wasExecuted;

    public RemoveQubitCommand(QuantumCircuit circuit, QuantumState state) {
        if (circuit.getNQubits() <= 1) {
            throw new IllegalStateException("Cannot remove qubit: minimum 1 qubit required");
        }

        this.originalCircuit = circuit;
        this.originalState = state;
        this.originalQubits = circuit.getNQubits();
        this.wasExecuted = false;
    }

    @Override
    public void execute() {
        int newNumQubits = originalQubits - 1;
        newCircuit = new QuantumCircuit(newNumQubits);

        // Copy only compatible gates (those not using the removed qubit)
        copyCompatibleGates(newNumQubits);

        // Create new initial state
        newState = QuantumState.zero(newNumQubits);

        wasExecuted = true;
    }

    @Override
    public void undo() {
        wasExecuted = false;
    }

    @Override
    public boolean canUndo() {
        return wasExecuted && newCircuit != null && newState != null;
    }

    @Override
    public void redo() {
        execute();
    }

    public QuantumCircuit getNewCircuit() { return newCircuit; }
    public QuantumState getNewState() { return newState; }
    public QuantumCircuit getOriginalCircuit() { return originalCircuit; }
    public QuantumState getOriginalState() { return originalState; }

    private void copyCompatibleGates(int maxQubits) {
        originalCircuit.getLayers().forEach(layer -> {
            var compatibleOps = layer.getOperations().stream()
                    .filter(op -> allQubitsValid(op.getTargetQubits(), maxQubits))
                    .toList();

            if (!compatibleOps.isEmpty()) {
                CircuitLayer newLayer = new CircuitLayer();
                compatibleOps.forEach(newLayer::addOperation);
                newCircuit.getLayers().add(newLayer);
            }
        });
    }

    private boolean allQubitsValid(int[] qubits, int maxQubits) {
        return Arrays.stream(qubits).allMatch(q -> q < maxQubits);
    }
}