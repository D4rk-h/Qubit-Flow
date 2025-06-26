package QuantumCore.model.QuantumPorts;

import QuantumCore.model.BlochSphere.BlochSphere;
import QuantumCore.model.QuantumGate;
import QuantumCore.model.Qubit;
import QuantumCore.model.QuantumGates.ControlledGate.ControlledGate;

public interface QuantumCircuitPort {
    void add(QuantumGate gate, int i, int j);
    void add(BlochSphere sphere, int i, int j);
    void add(Qubit qubit, int i);
    void addControlled(ControlledGate controlledGate, int i, int j);
}
