package QuantumCore.QuantumPorts;

import QuantumCore.Core.BlochSphere.BlochSphere;
import QuantumCore.Core.QuantumGate;
import QuantumCore.Core.Qubit;
import QuantumCore.QuantumGatesFactory.ControlledGate.ControlledGate;

public interface QuantumCircuitPort {
    void add(QuantumGate gate, int i, int j);
    void add(BlochSphere sphere, int i, int j);
    void add(Qubit qubit, int i);
    void addControlled(ControlledGate controlledGate, int i, int j);
}
