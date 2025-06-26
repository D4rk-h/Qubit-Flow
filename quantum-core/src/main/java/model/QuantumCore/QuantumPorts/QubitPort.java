package model.QuantumCore.QuantumPorts;
import model.QuantumCore.QuantumGate;

public interface QubitPort {
    int measure();
    void applyGate(QuantumGate gate);
}
