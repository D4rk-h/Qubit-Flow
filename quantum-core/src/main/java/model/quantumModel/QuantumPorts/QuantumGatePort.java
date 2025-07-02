package model.quantumModel.QuantumPorts;

import model.quantumModel.QuantumState;

public interface QuantumGatePort {
    QuantumState apply(QuantumState state);
}
