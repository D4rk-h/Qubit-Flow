package model.quantumModel.QuantumPorts;
import model.quantumModel.State;

public interface QuantumGatePort {
    State apply(State qubit);
}
