package model.quantumModel.QuantumPorts;

import model.quantumModel.BlochSphere.BlochSphere;
import model.quantumModel.QuantumGate;
import model.quantumModel.QuantumGates.ControlledGate.ControlledGate;
import model.quantumModel.QuantumState;

public interface QuantumCircuitPort {
    void add(QuantumGate gate, int i, int j);
    void add(BlochSphere sphere, int i, int j);
    void add(QuantumState state, int i);
    void addControlled(ControlledGate controlledGate, int i, int j);
}
