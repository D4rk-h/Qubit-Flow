package QuantumCore.Core.QuantumCircuit;

import QuantumCore.Core.BlochSphere.BlochSphere;
import QuantumCore.Core.QuantumGate;
import QuantumCore.Core.Qubit;
import QuantumCore.QuantumGatesFactory.ControlledGate.ControlledGate;
import QuantumCore.QuantumPorts.QuantumCircuitPort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuantumCircuit implements QuantumCircuitPort {
    private int nQubits;
    private int depth;
    private List<List<Object>> circuit;

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
        this(nQubits, 50);
    }

    @Override
    public void add(QuantumGate gate, int i, int j) {
        if (i < 0 || i >= nQubits || j < 0 || j >= circuit.get(0).size()) {throw new IndexOutOfBoundsException("Index out of bounds");}
        circuit.get(i).set(j, gate);
    }

    @Override
    public void addControlled(ControlledGate controlledGate, int i, int j) {
        if (i < 0 || j < 0){
            throw new IllegalArgumentException("Index out of bounds");
        }
        circuit.get(i).set(j, controlledGate.getControlQubit());
        circuit.get(i + 1).set(j, controlledGate.getGate());
    }

    @Override
    public void add(BlochSphere sphere, int i, int j) {
        if (i < 0 || i >= nQubits || j < 0 || j >= circuit.get(0).size()) {throw new IndexOutOfBoundsException("Index out of bounds");}
        circuit.get(i).set(j, sphere);
    }

    @Override
    public void add(Qubit qubit, int i) {
        if (i < 0 || i >= nQubits) {throw new IndexOutOfBoundsException("Index out of bounds");}
        circuit.get(i).set(0, qubit);
    }

    public int getnQubits() {return nQubits;}

    public void setnQubits(int nQubits) {this.nQubits = nQubits;}

    public List<List<Object>> getCircuit() {return circuit;}

    public void setCircuit(List<List<Object>> circuit) {this.circuit = circuit;}

    public int getDepth () {return depth;}

    public void setDepth ( int depth){this.depth = depth;}
}