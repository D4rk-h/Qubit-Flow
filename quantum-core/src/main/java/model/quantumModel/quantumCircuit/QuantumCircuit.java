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

package model.quantumModel.quantumCircuit;

import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumGate.GateOperation;
import model.quantumModel.quantumGate.QuantumGate;
import model.quantumModel.quantumGate.QuantumGates;
import model.quantumModel.quantumState.QuantumState;
import java.util.*;

public class QuantumCircuit {
    private int nQubit;
    private List<CircuitLayer> layers;

    public QuantumCircuit(int nQubit) {
        if (nQubit < 1 || nQubit > 10) throw new IllegalArgumentException("Circuit max number of qubits: 8, given: "+nQubit);
        this.nQubit = nQubit;
        this.layers = new ArrayList<>();
    }

    private void addGate(QuantumGate gate, int... targetQubits) {
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
        availableLayer.addOperation(new GateOperation(gate, targetQubits));
    }

    public void addHadamard(int qubit) {addGate(QuantumGates.hadamard(), qubit);}

    public void addNot(int qubit) {addGate(QuantumGates.not(), qubit);}

    public void addY(int qubit) {addGate(QuantumGates.y(), qubit);}

    public void addZ(int qubit) {addGate(QuantumGates.z(), qubit);}

    public void addT(int qubit) {addGate(QuantumGates.t(), qubit);}

    public void addPhase(int qubit) {addGate(QuantumGates.phase(), qubit);}

    public void addCNOT(int control, int target) {addGate(QuantumGates.cnot(), control, target);}

    public void addSwap(int qubit1, int qubit2) {addGate(QuantumGates.swap(), qubit1, qubit2);}

    public void addToffoli(int control1, int control2, int target) {addGate(QuantumGates.toffoli(), control1, control2, target);}

    public void addControlled(QuantumGate gate, int control, int... targets) {
        QuantumGate controlledGate = QuantumGates.controlled(gate);
        int[] allQubits = new int[targets.length + 1];
        allQubits[0] = control;
        System.arraycopy(targets, 0, allQubits, 1, targets.length);
        addGate(controlledGate, allQubits);
    }

    public void executeOn(QuantumState state) {
        if (state.getNumQubits() != this.nQubit) throw new IllegalArgumentException("State must have " + nQubit + " qubits");
        for (CircuitLayer layer : layers) {
            layer.executeOn(state);
        }
    }

    public int getDepth() {return layers.size();}
    public int getTotalGateCount() {return layers.stream().mapToInt(CircuitLayer::getOperationCount).sum();}
    public int getNQubits() {return nQubit;}
    public void setNQubits(int nQubits) {this.nQubit = nQubits;}
    public List<CircuitLayer> getLayers() {return layers;}
    public void setLayers(List<CircuitLayer> layers) {this.layers = layers;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Quantum Circuit (").append(nQubit).append(" qubits, ").append(getDepth()).append(" layers):\n");
        for (int i = 0; i < layers.size(); i++) {
            sb.append("Layer ").append(i + 1).append(": ");
            sb.append(layers.get(i).toString()).append("\n");
        }
        return sb.toString();
    }
}