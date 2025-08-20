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