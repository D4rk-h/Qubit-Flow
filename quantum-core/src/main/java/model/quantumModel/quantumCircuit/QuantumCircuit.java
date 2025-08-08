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

import model.quantumModel.measurementDisplay.Display;
import model.quantumModel.quantumCircuit.quantumCircuitUtils.cliVisualization.QuantumCircuitCLIDisplay;
import model.quantumModel.quantumCircuit.quantumCircuitUtils.QuantumCircuitSeekToMerge;
import model.quantumModel.quantumCircuit.quantumCircuitUtils.QuantumCircuitDisplayUtils;
import model.quantumModel.quantumCircuit.quantumCircuitUtils.QuantumCircuitValidation;
import model.quantumModel.measurementDisplay.blochSphere.BlochSphere;
import model.quantumModel.QuantumGate;
import model.quantumModel.quantumGate.ControlledGate.ControlledGate;
import model.quantumModel.quantumGate.MultiQubitGateMarker;
import model.quantumModel.quantumPort.QuantumCircuitPort;
import model.quantumModel.quantumState.QuantumState;
import model.quantumModel.quantumState.quantumStateUtils.BasicQuantumState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuantumCircuit implements QuantumCircuitPort {
    private int nQubits;
    private int depth;
    private List<List<Object>> circuit;
    private final static QuantumCircuitDisplayUtils displayUtils = new QuantumCircuitDisplayUtils();
    private final static QuantumCircuitValidation validateUtils = new QuantumCircuitValidation();
    private final static QuantumCircuitCLIDisplay cliUtils = new QuantumCircuitCLIDisplay();
    private final static QuantumCircuitSeekToMerge seekToMerge = new QuantumCircuitSeekToMerge();

    public QuantumCircuit(int nQubits, int depth) {
        if (nQubits <= 0) {throw new IllegalArgumentException("At least 1 qubit is required");}
        if (depth <= 0) {throw new IllegalArgumentException("Depth should be positive");}
        this.nQubits = nQubits;
        this.depth = depth;
        this.circuit = IntStream.range(0, nQubits)
                .mapToObj(wire -> new ArrayList<>(Collections.nCopies(depth, null)))
                .collect(Collectors.toList());
    }

    public QuantumCircuit(int nQubits) {
        this(nQubits, 25);
    }

    @Override
    public void add(QuantumGate gate, int wire, int depth) {
        validateUtils.validateQuantumGateBounds(this, wire, depth, gate);
        circuit.get(wire).set(depth, gate);
    }

    @Override
    public void add(Display display) {
        validateUtils.validateDisplayBounds(display, this);
        if (displayUtils.needsShift(display, circuit)) {
            int shiftAmount = displayUtils.calculateRequiredShiftAmount(display, circuit);
            this.circuit = displayUtils.shiftColumnsRight(circuit, display.fromDepth(), shiftAmount);
        }
        displayUtils.placeDisplay(display, this.circuit);
    }

    @Override
    public void addControlled(ControlledGate controlledGate, int wire, int depth) {
        validateUtils.validateControlledGateBounds(controlledGate, this, wire, depth);
        circuit.get(wire).set(depth, controlledGate);
    }

    @Override
    public void add(BlochSphere sphere, int wire, int depth) {
        validateUtils.validateBlochSphereBounds(this, wire, depth);
        circuit.get(wire).set(depth, sphere);
    }

    @Override
    public void add(QuantumState state, int wire) {
        validateUtils.validateQuantumStateBounds(this, wire);
        circuit.get(wire).set(0, state);
    }

    @Override
    public QuantumGate removeGate(int wire, int depth){
        if (circuit.get(wire).get(depth) instanceof QuantumGate deletedGate){
            circuit.get(wire).set(depth, null);
            return deletedGate;
        } else {
            return null;
        }
    }

    @Override
    public List<Object> removeWire(int wire){
        List<Object> deletedWire = circuit.get(wire);
        circuit.remove(wire);
        return deletedWire;
    }

    @Override
    public Display removeDisplay(Display display) {
        return displayUtils.removeDisplay(display, this.circuit);
    }

    public void mergeGates() {
        for (List<Object> wire : circuit) {
            seekToMerge.seekToMergeMinusY(wire);
            seekToMerge.seekToMergeX(wire);
            seekToMerge.seekToMergeZ(wire);
        }
    }

    public QuantumState execute(QuantumState initialState) {
        if (initialState.getNumQubits() != this.nQubits) {throw new IllegalArgumentException("Initial state qubits dont match circuit size");}
        QuantumState currentState = initialState.clone();
        for (int depth=0;depth<this.depth;depth++) {
            currentState = executeDepth(currentState, depth);
        }
        return currentState;
    }

    private QuantumState executeDepth(QuantumState state, int depth) {
        QuantumState currentState = state;
        for (List<Object> objects : circuit) {
            if (depth < objects.size()) {
                Object element = objects.get(depth);
                if (element instanceof QuantumGate gate && !(element instanceof MultiQubitGateMarker)) {
                    currentState = gate.apply(currentState);
                }
            }
        }
        return currentState;
    }

    public void show() {
        System.out.println(cliUtils.formatCircuit(this));
    }

    public void showWithLabels() {
        System.out.println(cliUtils.formatCircuitWithLabels(this));
    }

    public String getCircuitString() {
        return cliUtils.formatCircuit(this);
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