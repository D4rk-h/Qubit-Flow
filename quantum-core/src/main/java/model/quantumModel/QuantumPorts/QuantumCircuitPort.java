package model.quantumModel.QuantumPorts;

import model.quantumModel.BlochSphere.BlochSphere;
import model.quantumModel.QuantumGate;
import model.quantumModel.Qubit;
import model.quantumModel.QuantumGates.ControlledGate.ControlledGate;

public interface QuantumCircuitPort {
    void add(QuantumGate gate, int i, int j);
    void add(BlochSphere sphere, int i, int j);
    void add(Qubit qubit, int i);
    void addControlled(ControlledGate controlledGate, int i, int j);
}
