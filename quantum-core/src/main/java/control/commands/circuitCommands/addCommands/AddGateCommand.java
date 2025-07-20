package control.commands.circuitCommands.addCommands;

import model.commandsModel.Location;
import model.quantumModel.QuantumCircuit.QuantumCircuit;
import model.quantumModel.QuantumGate;

public class AddGateCommand extends AddCommand implements AddCommandPort {
    private final QuantumGate gate;

    public AddGateCommand(Object gate, QuantumCircuit circuit, int wire, int depth) {
        super(gate, new Location(circuit, wire, depth));
        this.gate = (QuantumGate) gate;
    }

    @Override
    public void addToCircuit() {
        location.circuit().add(gate, location.wire(), location.depth());
    };
}
