package QuantumCore.QuantumPorts;
import QuantumCore.Core.State;

public interface QuantumGatePort {
    State apply(State qubit);
}
