package model.quantumModel.QuantumPorts;
import model.quantumModel.QuantumGate;

public interface QubitPort {
    void applyGate(QuantumGate gate);
    int measure();
}
