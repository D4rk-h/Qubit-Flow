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
import model.quantumModel.quantumCircuit.quantumCircuitUtils.QuantumCircuitSeekToMerge;
import model.quantumModel.quantumCircuit.quantumCircuitUtils.QuantumCircuitDisplayShift;
import model.quantumModel.quantumCircuit.quantumCircuitUtils.QuantumCircuitValidation;
import model.quantumModel.measurementDisplay.blochSphere.BlochSphere;
import model.quantumModel.QuantumGate;
import model.quantumModel.quantumGate.ControlledGate.ControlledGate;
import model.quantumModel.quantumPort.QuantumCircuitPort;
import model.quantumModel.QuantumState;
import model.quantumModel.quantumState.BasicQuantumState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuantumCircuit implements QuantumCircuitPort {
    private int nQubits;
    private int depth;
    private List<List<Object>> circuit;
    private final static QuantumCircuitDisplayShift displayShifter = new QuantumCircuitDisplayShift();
    private final static QuantumCircuitValidation validate = new QuantumCircuitValidation();
    private final static QuantumCircuitCLIDisplay displayUtil = new QuantumCircuitCLIDisplay();
    private final static QuantumCircuitSeekToMerge seekToMerge = new QuantumCircuitSeekToMerge();

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
    public void add(QuantumGate gate, int wire, int depth) {
        validate.validateQuantumGateBounds(this, wire, depth, gate);
        circuit.get(wire).set(depth, gate);
    }

    @Override
    public void add(Display display) {
        validate.validateDisplayBounds(display, this);
        if (displayShifter.needsShift(display, circuit)) {
            int shiftAmount = displayShifter.calculateRequiredShiftAmount(display, circuit);
            this.circuit = displayShifter.shiftColumnsRight(circuit, display.fromDepth(), shiftAmount);
        }
        displayShifter.placeDisplay(display, this.circuit);
    }

    @Override
    public void addControlled(ControlledGate controlledGate, int i, int j) {
        validate.validateControlledGateBounds(controlledGate, this, i, j);
        circuit.get(i).set(j, controlledGate);
    }

    @Override
    public void add(BlochSphere sphere, int i, int j) {
        validate.validateBlochSphereBounds(this, i, j);
        circuit.get(i).set(j, sphere);
    }

    @Override
    public void add(QuantumState state, int wire) {
        validate.validateQuantumStateBounds(this, wire);
        circuit.get(wire).set(0, state);
    }

    @Override
    public QuantumGate removeGate(int wire, int depth){
        if (circuit.get(wire).get(depth) instanceof QuantumGate){
            QuantumGate deletedGate = (QuantumGate) circuit.get(wire).get(depth);
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
        Display deletedDisplay = null;
        for (int i = display.fromWire(); i < display.toWire(); i++) {
            for (int j = display.fromDepth(); j < display.toDepth(); j++) {
                if (circuit.get(i).get(j) instanceof Display) {
                    deletedDisplay = (Display) circuit.get(i).get(j);
                    circuit.get(i).set(j, null);
                }
            }
        }
        return deletedDisplay;
    }


    public void mergeGates() {
        for (List<Object> wire : circuit) {
            seekToMerge.seekToMergeMinusY(wire);
            seekToMerge.seekToMergeX(wire);
            seekToMerge.seekToMergeZ(wire);
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