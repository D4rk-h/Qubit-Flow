package QuantumCore.QuantumPorts;
import QuantumCore.Core.QuantumGate;
import QuantumCore.Core.State;

public interface QubitPort {
    int measure();
    void applyGate(QuantumGate gate);
    State getState();
}
