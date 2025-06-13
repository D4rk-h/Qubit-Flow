package QuantumCore.QuantumPorts;
import QuantumCore.Core.QuantumGate;

public interface QubitPort {
    int measure();
    void applyGate(QuantumGate gate);
}
