package QuantumCore.model.QuantumPorts;
import QuantumCore.model.State;

public interface QuantumGatePort {
    State apply(State qubit);
}
