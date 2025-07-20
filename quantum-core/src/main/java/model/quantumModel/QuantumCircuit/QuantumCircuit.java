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

package model.quantumModel.QuantumCircuit;

import model.commandsModel.Display;
import model.quantumModel.QuantumMeasurementDisplays.BlochSphere.BlochSphere;
import model.quantumModel.QuantumGate;
import model.quantumModel.QuantumGates.ControlledGate.ControlledGate;
import model.quantumModel.QuantumPorts.QuantumCircuitPort;
import model.quantumModel.QuantumState;
import model.quantumModel.QuantumStates.BasicQuantumState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuantumCircuit implements QuantumCircuitPort {
    private int nQubits;
    private int depth;
    private List<List<Object>> circuit;
    private final static QuantumCircuitUtil utils = new QuantumCircuitUtil();
    private final static QuantumCircuitCLIDisplay displayUtil = new QuantumCircuitCLIDisplay();

    public QuantumCircuit(int nQubits, int depth) {
        if (nQubits <= 0) {throw new IllegalArgumentException("At least 1 qubit is required");}
        if (depth <= 0) {throw new IllegalArgumentException("Depth should be positive");}
        this.nQubits = nQubits;
        this.depth = depth;
        this.circuit = IntStream.range(0, nQubits)
                .mapToObj(i -> new ArrayList<>(Collections.nCopies(depth, null)))
                .collect(Collectors.toList());
    }

    public QuantumCircuit(int nQubits) {
        this(nQubits, 25);
    }

    @Override
    public void add(QuantumGate gate, int i, int j) {
        if (i < 0 || i >= nQubits || j < 0 || j >= circuit.get(0).size()) {throw new IndexOutOfBoundsException("Index out of bounds");}
        if (i > 0 && circuit.get(i - 1).get(j) instanceof ControlledGate){((ControlledGate) circuit.get(i-1).get(j)).addTarget(gate);}
        circuit.get(i).set(j, gate);
    }

    @Override
    public void add(Display display) {
        if (display.fromWire() < 0 || display.toWire() >= nQubits || display.fromDepth() < 0 || display.toDepth() >= circuit.get(0).size()) {throw new IndexOutOfBoundsException("Index out of bounds");}
        // Todo implement here after develop seekAndExtend method
    }

    @Override
    public void addControlled(ControlledGate controlledGate, int i, int j) {
        if (i < 0 || j < 0 || i > (circuit.get(i).size() + 2) || j > (circuit.get(i).size() + 2)){throw new IllegalArgumentException("Index out of bounds");}
        if (i>circuit.size() || j>circuit.get(i).size()){utils.extend(circuit, i, j);}
        circuit.get(i).set(j, controlledGate);
    }

    @Override
    public void add(BlochSphere sphere, int i, int j) {
        if (i < 0 || i >= nQubits || j < 0 || j >= circuit.get(0).size()) {throw new IndexOutOfBoundsException("Index out of bounds");}
        circuit.get(i).set(j, sphere);
    }

    @Override
    public void add(QuantumState state, int i) {
        if (i < 0 || i >= nQubits) {throw new IndexOutOfBoundsException("Index out of bounds");}
        circuit.get(i).set(0, state);
    }

    public void mergeGates() {
        for (List<Object> wire : circuit) {
            utils.seekToMergeMinusY(wire);
            utils.seekToMergeX(wire);
            utils.seekToMergeZ(wire);
        }
    }

    public void show() {
        System.out.println(displayUtil.formatCircuit(this));
    }

    public void showWithLabels() {
        System.out.println(displayUtil.formatCircuitWithLabels(this));
    }

    public String getCircuitString() {
        return displayUtil.formatCircuit(this);
    }

    public void setInitialState(BasicQuantumState state, int qubit) {
        if (qubit < 0 || qubit >= nQubits) {
            throw new IndexOutOfBoundsException("Qubit index out of bounds");
        }
        circuit.get(qubit).set(0, state);
    }

    public int getnQubits() {return nQubits;}

    public void setnQubits(int nQubits) {this.nQubits = nQubits;}

    public List<List<Object>> getCircuit() {return circuit;}

    public void setCircuit(List<List<Object>> circuit) {this.circuit = circuit;}

    public int getDepth () {return depth;}

    public void setDepth ( int depth){this.depth = depth;}
}