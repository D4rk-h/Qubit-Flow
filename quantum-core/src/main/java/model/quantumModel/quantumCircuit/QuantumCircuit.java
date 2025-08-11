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
import model.quantumModel.quantumCircuit.quantumCircuitUtils.cliVisualization.QuantumCircuitCLIDisplay;
import model.quantumModel.quantumCircuit.quantumCircuitUtils.QuantumCircuitSeekToMerge;
import model.quantumModel.quantumCircuit.quantumCircuitUtils.QuantumCircuitValidation;
import model.quantumModel.measurementDisplay.blochSphere.BlochSphere;
import model.quantumModel.QuantumGate;
import model.quantumModel.quantumGate.ControlledGate.ControlGate;
import model.quantumModel.quantumGate.MultiQubitGateMarker;
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
    public void addControlled(ControlGate controlledGate, int wire, int depth) {
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

    public void mergeGates() {
        for (List<Object> wire : circuit) {
            seekToMerge.seekToMergeMinusY(wire);
            seekToMerge.seekToMergeX(wire);
            seekToMerge.seekToMergeZ(wire);
        }
    }

    public Map<Display, Object> executeWithDisplays(QuantumState initialState) {
        QuantumState finalState = execute(initialState);
        Map<Display, Object> displayResults = new HashMap<>();
        for (Display display : displays) {
            if (display.display() instanceof DisplayPort port) {
                QuantumState subState = extractSubState(finalState, display.fromWire(), display.toWire());
                Object result = port.renderContent(subState);
                displayResults.put(display, result);
            }
        }
        return displayResults;
    }

    private QuantumState extractSubState(QuantumState fullState, int fromWire, int toWire) {
        // TODO: Implement substate extraction for qubit range
        return fullState;
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
        Set<Integer> processedQubits = new HashSet<>();
        for (int wire = 0; wire < circuit.size(); wire++) {
            if (depth < circuit.get(wire).size() && !processedQubits.contains(wire)) {
                Object element = circuit.get(wire).get(depth);
                if (element instanceof QuantumGate && !(element instanceof MultiQubitGateMarker)) {
                    currentState =  ((QuantumGate) element).apply(currentState);
                    for (int targetQubit : ((QuantumGate) element).getTargetQubits()) {
                        processedQubits.add(targetQubit);
                    }
                }
                else if (element instanceof MultiQubitGateMarker) {
                    int primaryQubit = ((MultiQubitGateMarker) element).getPrimaryQubit();
                    if (!processedQubits.contains(primaryQubit)) {
                        Object primaryElement = circuit.get(primaryQubit).get(depth);
                        if (primaryElement instanceof QuantumGate) {
                            currentState = ((QuantumGate) primaryElement).apply(currentState);
                            for (int targetQubit : ((QuantumGate) primaryElement).getTargetQubits()) {
                                processedQubits.add(targetQubit);
                            }
                        }
                    }
                }
            }
        }
        return currentState;
    }

    public List<QuantumState> executeStepByStep(QuantumState initialState) {
        List<QuantumState> stateHistory = new ArrayList<>();
        QuantumState currentState = initialState.clone();
        stateHistory.add(currentState.clone());
        for (int depth = 0; depth < this.depth; depth++) {
            currentState = executeDepth(currentState, depth);
            stateHistory.add(currentState.clone());
        }
        return stateHistory;
    }

    public List<QuantumGate> getGatesAtDepth(int depth) {
        List<QuantumGate> gates = new ArrayList<>();
        Set<QuantumGate> uniqueGates = new HashSet<>();
        for (List<Object> objects : circuit) {
            if (depth < objects.size()) {
                Object element = objects.get(depth);
                if (element instanceof QuantumGate && !(element instanceof MultiQubitGateMarker)) {
                    if (uniqueGates.add(((QuantumGate) element))) {
                        gates.add(((QuantumGate) element));
                    }
                }
            }
        }
        return gates;
    }

    public void show() {
        System.out.println(cliUtils.formatCircuit(this));
    }

    public String getCircuitString() {
        return cliUtils.formatCircuit(this);
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