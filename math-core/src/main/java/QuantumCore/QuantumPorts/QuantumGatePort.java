package QuantumCore.QuantumPorts;
import QuantumCore.State;

public interface QuantumGatePort {
    State apply(State qubit);
}
