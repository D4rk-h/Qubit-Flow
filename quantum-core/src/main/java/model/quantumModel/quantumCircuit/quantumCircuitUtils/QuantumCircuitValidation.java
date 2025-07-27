package model.quantumModel.quantumCircuit.quantumCircuitUtils;

import model.quantumModel.measurementDisplay.Display;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.QuantumGate;
import model.quantumModel.quantumGate.ControlledGate.ControlledGate;

import java.util.ArrayList;
import java.util.List;

public class QuantumCircuitValidation {
    public void validateDisplayBounds(Display display, QuantumCircuit quantumCircuit) {
        if (display.fromWire() < 0 || display.toWire() >= quantumCircuit.getnQubits()) {throw new IndexOutOfBoundsException("Wire index out of bounds");}
        if (display.fromDepth() < 0) {throw new IndexOutOfBoundsException("Depth index cannot be negative");}
        if (display.fromDepth() > display.toDepth() || display.fromWire() > display.toWire()) {throw new IllegalArgumentException("Invalid display range: from indices must be <= to indices");}
    }

    public void validateQuantumGateBounds(QuantumCircuit quantumCircuit, int i, int j, QuantumGate gate) {
        if (i < 0 || i >= quantumCircuit.getnQubits() || j < 0 || j >= quantumCircuit.getCircuit().get(0).size()) {throw new IndexOutOfBoundsException("Index out of bounds");}
        if (i > 0 && quantumCircuit.getCircuit().get(i - 1).get(j) instanceof ControlledGate){((ControlledGate) quantumCircuit.getCircuit().get(i-1).get(j)).addTarget(gate);}
    }

    public void validateBlochSphereBounds(QuantumCircuit quantumCircuit, int i, int j) {
        if (i < 0 || i >= quantumCircuit.getnQubits() || j < 0 || j >= quantumCircuit.getCircuit().get(0).size()) {throw new IndexOutOfBoundsException("Index out of bounds");}
    }

    public void validateQuantumStateBounds(QuantumCircuit quantumCircuit, int i) {
        if (i < 0 || i >= quantumCircuit.getnQubits()) {throw new IndexOutOfBoundsException("Index out of bounds");}
    }

    public void validateControlledGateBounds(ControlledGate cGate, QuantumCircuit quantumCircuit, int i, int j) {
        if (i < 0 || j < 0 || i > (quantumCircuit.getCircuit().get(i).size() + 2) || j > (quantumCircuit.getCircuit().get(i).size() + 2)){throw new IllegalArgumentException("Index out of bounds");}
        if (i>quantumCircuit.getCircuit().size() || j>quantumCircuit.getCircuit().get(i).size()){extend(quantumCircuit.getCircuit(), i, j);}
    }

    private static void extend(List<List<Object>> circuit, int i, int j) {
        while (circuit.size() <= i) {circuit.add(new ArrayList<>());}
        while (circuit.get(i).size() <= j) {circuit.get(i).add(null);}
    }
}
