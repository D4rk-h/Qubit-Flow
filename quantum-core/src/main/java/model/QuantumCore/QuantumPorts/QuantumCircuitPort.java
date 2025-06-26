package model.QuantumCore.QuantumPorts;

import model.QuantumCore.BlochSphere.BlochSphere;
import model.QuantumCore.QuantumGate;
import model.QuantumCore.Qubit;
import model.QuantumCore.QuantumGates.ControlledGate.ControlledGate;

public interface QuantumCircuitPort {
    void add(QuantumGate gate, int i, int j);
    void add(BlochSphere sphere, int i, int j);
    void add(Qubit qubit, int i);
    void addControlled(ControlledGate controlledGate, int i, int j);
}
