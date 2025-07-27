package control.command.circuitCommand.addCommand;

import model.commandsModel.Location;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.QuantumState;

public class AddQubitCommand extends AddCommand implements AddCommandPort {
    private final QuantumState state;

    public AddQubitCommand(Object state, QuantumCircuit circuit, int wire, int depth) {
        super(state, new Location(circuit, wire, depth));
        this.state = (QuantumState) state;
    }

    @Override
    public void addToCircuit() {
        location.circuit().add(state, location.wire());
    }
}
