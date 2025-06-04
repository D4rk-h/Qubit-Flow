package QuantumCore.QuantumPorts;
import QuantumCore.QuantumGate;
import QuantumCore.State;

public interface QubitPort {
    int measure();
    void applyGate(QuantumGate gate);
    State getState();
}
