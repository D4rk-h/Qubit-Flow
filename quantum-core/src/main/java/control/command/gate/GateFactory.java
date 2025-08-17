package control.command.gate;

import model.quantumModel.quantumGate.QuantumGate;
import model.quantumModel.quantumGate.QuantumGates;

public class GateFactory {
    public static QuantumGate createGate(GateType gateType) {
        return switch (gateType) {
            case HADAMARD -> QuantumGates.hadamard();
            case PAULI_X -> QuantumGates.not();
            case PAULI_Y -> QuantumGates.y();
            case PAULI_Z -> QuantumGates.z();
            case T_GATE -> QuantumGates.t();
            case S_GATE -> QuantumGates.phase();
            case CNOT -> QuantumGates.cnot();
            case SWAP -> QuantumGates.swap();
            case TOFFOLI -> QuantumGates.toffoli();
        };
    }
}