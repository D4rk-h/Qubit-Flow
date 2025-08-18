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
import model.quantumModel.quantumState.QuantumState;

import java.util.ArrayList;
import java.util.List;

public class AddQubitCommand implements UndoableCommand {
    private final QuantumCircuit originalCircuit;
    private final QuantumState originalState;
    private final int originalQubits;
    private QuantumCircuit newCircuit;
    private QuantumState newState;
    private boolean wasExecuted;

    public AddQubitCommand(QuantumCircuit circuit, QuantumState state) {
        this.originalCircuit = circuit;
        this.originalState = state;
        this.originalQubits = circuit.getNQubits();
        this.wasExecuted = false;
    }

    @Override
    public void execute() {
        int newNumQubits = originalQubits + 1;
        newCircuit = new QuantumCircuit(newNumQubits);
        copyExistingLayers();
        QuantumState zeroQubit = QuantumState.zero(1);
        newState = originalState.tensorProduct(zeroQubit);
        wasExecuted = true;
    }

    @Override
    public void undo() {wasExecuted = false;}

    @Override
    public boolean canUndo() {return wasExecuted && newCircuit != null && newState != null;}

    @Override
    public void redo() {execute();}

    public QuantumCircuit getNewCircuit() { return newCircuit; }
    public QuantumState getNewState() { return newState; }
    public QuantumCircuit getOriginalCircuit() { return originalCircuit; }
    public QuantumState getOriginalState() { return originalState; }

    private void copyExistingLayers() {
        List<CircuitLayer> newLayers = new ArrayList<>();
        for (CircuitLayer layer : originalCircuit.getLayers()) {
            CircuitLayer newLayer = new CircuitLayer();
            layer.getOperations().forEach(newLayer::addOperation);
            newLayers.add(newLayer);
        }
        newCircuit.setLayers(newLayers);
    }
}