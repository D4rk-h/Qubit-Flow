package QuantumCore.QuantumPorts;

import QuantumCore.QuantumGate;
import QuantumCore.State;

public interface QubitPort {
    State measure();
    String collapse();
    void applyGate(QuantumGate gate);
    State getState();
}
