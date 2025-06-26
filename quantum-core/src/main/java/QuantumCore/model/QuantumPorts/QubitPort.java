package QuantumCore.model.QuantumPorts;
import QuantumCore.model.QuantumGate;

public interface QubitPort {
    int measure();
    void applyGate(QuantumGate gate);
}
