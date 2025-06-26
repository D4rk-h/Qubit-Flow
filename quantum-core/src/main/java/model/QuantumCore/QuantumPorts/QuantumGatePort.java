package model.QuantumCore.QuantumPorts;
import model.QuantumCore.State;

public interface QuantumGatePort {
    State apply(State qubit);
}
