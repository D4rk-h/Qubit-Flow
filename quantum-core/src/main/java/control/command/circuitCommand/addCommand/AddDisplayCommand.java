package control.command.circuitCommand.addCommand;

import model.quantumModel.measurementDisplay.Display;
import model.commandsModel.Location;
import model.quantumModel.quantumCircuit.QuantumCircuit;

public class AddDisplayCommand extends AddCommand implements AddCommandPort {
    private final Display display;

    public AddDisplayCommand(Object display, QuantumCircuit circuit, int wire, int depth) {
        super(display, new Location(circuit, wire, depth));
        this.display = (Display) display;
    }

    @Override
    public void addToCircuit() {
        location.circuit().add(display);
    }

}
