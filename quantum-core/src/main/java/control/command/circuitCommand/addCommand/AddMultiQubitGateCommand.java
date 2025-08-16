package control.command.circuitCommand.addCommand;

import model.commandsModel.Location;
import model.quantumModel.quantumGate.QuantumGate;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumGate.MultiQubitGateMarker;

public class AddMultiQubitGateCommand extends AddCommand implements AddCommandPort{
    private final QuantumGate gate;
    private final int[] qubitPositions;
    private final int systemSize;

    public AddMultiQubitGateCommand(QuantumGate gate, QuantumCircuit circuit, int[] qubitPositions, int depth) {
        super(gate, new Location(circuit, qubitPositions[0], depth));
        this.gate = gate;
        this.qubitPositions = qubitPositions.clone();
        this.systemSize = circuit.getnQubits();
        validatePositions(circuit);
    }

    private void validatePositions(QuantumCircuit circuit) {
        for (int pos : qubitPositions) {
            if (pos < 0 || pos >= circuit.getnQubits()) {
                throw new IndexOutOfBoundsException("Qubit position " + pos + " out of bounds");
            }
        }
        if (qubitPositions.length != gate.getNumQubits()) {
            throw new IllegalArgumentException("Gate requires " + gate.getNumQubits() + " qubits, got " + qubitPositions.length);
        }
    }

    @Override
    public void addToCircuit() {
        QuantumGate expandedGate = gate.expandToSystem(systemSize, qubitPositions);
        for (int i = 0; i < qubitPositions.length; i++) {
            int qubitPos = qubitPositions[i];
            if (i == 0) {location.circuit().add(expandedGate, qubitPos, location.depth());
            } else {
                MultiQubitGateMarker marker = new MultiQubitGateMarker(expandedGate, i, qubitPositions[0]);
                location.circuit().add(marker, qubitPos, location.depth());
            }
        }
    }
}
