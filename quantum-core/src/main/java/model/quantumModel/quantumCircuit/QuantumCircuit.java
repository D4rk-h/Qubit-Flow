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
import model.quantumModel.measurementDisplay.displayUtils.DisplayPort;
import model.quantumModel.quantumCircuit.quantumCircuitUtils.QuantumCircuitValidation;
import model.quantumModel.quantumGate.QuantumGate;
import model.quantumModel.quantumGate.multiQubitGate.CNot;
import model.quantumModel.quantumGate.Control.ControlGate;
import model.quantumModel.quantumGate.multiQubitGate.Toffoli;
import model.quantumModel.quantumPort.QuantumCircuitPort;
import model.quantumModel.quantumState.QuantumState;
import model.quantumModel.quantumState.quantumStateUtils.BasicQuantumState;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuantumCircuit implements QuantumCircuitPort {
    private int nQubits;
    private int depth;
    private List<List<Object>> circuit;
    private List<Display> displays;

    private final static QuantumCircuitValidation validateUtils = new QuantumCircuitValidation();

    public QuantumCircuit(int nQubits, int depth) {
        if (nQubits <= 0) {throw new IllegalArgumentException("At least 1 qubit is required");}
        if (depth <= 0) {throw new IllegalArgumentException("Depth should be positive");}
        this.nQubits = nQubits;
        this.depth = depth;
        this.circuit = IntStream.range(0, nQubits)
                .mapToObj(wire -> new ArrayList<>(Collections.nCopies(depth, null)))
                .collect(Collectors.toList());
        this.displays = new ArrayList<>();
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
    public void addCNot(CNot cnot, int wire, int depth) {
        if (this.nQubits < 2) throw new IllegalArgumentException("Cannot place a cnot into a single-qubit circuit");
        ControlGate control = new ControlGate((QuantumState) circuit.get(wire).getFirst(), cnot);
        control.setActivation(control.activateControl(this.nQubits));
        cnot.setControl(control);
        cnot.setNumOfQubits(this.nQubits);
        circuit.get(wire).set(depth, cnot.getControl());
        circuit.get(wire + 1).set(depth, cnot);
    }

    @Override
    public void addToffoli(Toffoli toff, int wire, int depth) {
        if (this.nQubits < 3) throw new IllegalArgumentException("Toffoli gate must be placed in a three/plus-qubit circuit");
        ControlGate control2 = new ControlGate((QuantumState) circuit.get(wire + 1).getFirst(), toff);
        ControlGate control1 = new ControlGate((QuantumState) circuit.get(wire).getFirst(), control2);
        toff.setFirstControl(control1);
        toff.setSecondControl(control2);
        toff.setNumOfQubits(this.nQubits);
        circuit.get(wire).set(depth, toff.getFirstControl());
        circuit.get(wire + 1).set(depth, toff.getSecondControl());
        circuit.get(wire + 2).set(depth, toff);
    }

    @Override
    public void addControl(ControlGate control, int targetWire, int targetDepth) {
        validateUtils.validateControlPlacement(this, targetWire, targetDepth);
        circuit.get(targetWire - 1).set(targetDepth, control);
    }

    @Override
    public void add(QuantumState state, int wire) {
        validateUtils.validateQuantumStateBounds(this, wire);
        circuit.get(wire).set(0, state);
    }

    @Override
    public void addDisplay(Display display) {
        validateDisplayBounds(display);
        displays.add(display);
    }

    public Display findDisplayById(UUID displayId) {
        return displays.stream()
                .filter(display -> display.id().equals(displayId))
                .findFirst()
                .orElse(null);
    }

    public List<Display> getDisplaysForWire(int wire) {
        return displays.stream()
                .filter(display -> display.coversWire(wire))
                .collect(Collectors.toList());
    }

    public List<Display> getAllDisplays() {
        return new ArrayList<>(displays);
    }

    public boolean hasDisplays() {
        return !displays.isEmpty();
    }

    private void validateDisplayBounds(Display display) {
        if (display.fromWire() < 0 || display.toWire() >= nQubits) {
            throw new IllegalArgumentException("Display wire range out of circuit bounds");
        }
        if (display.fromWire() > display.toWire()) {
            throw new IllegalArgumentException("Display fromWire cannot be greater than toWire");
        }
        if (display.display() instanceof DisplayPort port) {
            int qubitCount = display.getQubitCount();
            if (!port.isCompatibleWith(qubitCount)) throw new IllegalArgumentException("Display type not compatible");
        }
    }

    @Override
    public QuantumGate removeGate(int wire, int depth){
        if (wire < 0 || wire >= nQubits || depth < 0 || depth >= this.depth) return null;
        Object element = circuit.get(wire).get(depth);
        if (element instanceof QuantumGate gate) {
            circuit.get(wire).set(depth, null);
            return gate;
        }
        return null;
    }

    @Override
    public List<Object> removeWire(int wire) {
        if (wire < 0 || wire >= nQubits) return null;
        List<Object> deletedWire = circuit.get(wire);
        circuit.remove(wire);
        nQubits--;
        updateDisplaysAfterWireRemoval(wire);
        return deletedWire;
    }

    private void updateDisplaysAfterWireRemoval(int removedWire) {
        displays.removeIf(display -> display.coversWire(removedWire));
        List<Display> updatedDisplays = new ArrayList<>();
        for (Display display : displays) {
            if (display.fromWire() > removedWire) {
                Display updatedDisplay = new Display(
                        display.display(),
                        display.fromWire() - 1,
                        display.toWire() - 1,
                        display.id()
                );
                updatedDisplays.add(updatedDisplay);
            } else {
                updatedDisplays.add(display);
            }
        }
        displays = updatedDisplays;
    }

    @Override
    public boolean removeDisplay(Display display) {return displays.remove(display);}

    public void show() {
    //todo: develop a visualization method, CLI not viable
    }

    public void setInitialState(BasicQuantumState state, int qubit) {
        if (qubit < 0 || qubit >= nQubits) throw new IndexOutOfBoundsException("Qubit index out of bounds");
        circuit.get(qubit).set(0, state);
    }

    public int getnQubits() {return nQubits;}

    public void setnQubits(int nQubits) {this.nQubits = nQubits;}

    public List<List<Object>> getCircuit() {return circuit;}

    public void setCircuit(List<List<Object>> circuit) {this.circuit = circuit;}

    public int getDepth () {return depth;}

    public void setDepth ( int depth){this.depth = depth;}
}