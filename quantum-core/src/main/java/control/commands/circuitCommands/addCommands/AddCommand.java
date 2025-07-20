package control.commands.circuitCommands.addCommands;

import control.commands.QuantumCommand;
import model.commandsModel.Display;
import model.commandsModel.Location;
import model.quantumModel.QuantumGate;
import model.quantumModel.QuantumState;

public class AddCommand implements QuantumCommand {
    private final Object elementToAdd;
    public final Location location;

    public AddCommand(Object elementToAdd, Location location){
        this.elementToAdd = elementToAdd;
        this.location = location;
    }

    @Override
    public void execute() {
        if (elementToAdd instanceof QuantumGate) {
            AddGateCommand add = new AddGateCommand(elementToAdd, location.circuit(), location.wire(), location.depth());
            add.addToCircuit();
        } else if (elementToAdd instanceof QuantumState) {
            AddQubitCommand add = new AddQubitCommand(elementToAdd, location.circuit(), location.wire(), location.depth());
            add.addToCircuit();
        } else if (elementToAdd instanceof Display) {
            AddDisplayCommand add = new AddDisplayCommand(elementToAdd, location.circuit(), location.wire(), location.depth());
            add.addToCircuit();
            add.addToCircuit();
        }

    }

    @Override
    public void undo() {

    }

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public void redo() {

    }

    @Override
    public String getDescription() {
        return "";
    }
}
