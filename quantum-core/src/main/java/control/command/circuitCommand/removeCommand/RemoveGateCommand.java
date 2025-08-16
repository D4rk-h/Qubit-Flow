package control.command.circuitCommand.removeCommand;

import control.command.QuantumCommand;
import model.commandsModel.Location;
import model.quantumModel.quantumGate.QuantumGate;

public class RemoveGateCommand implements QuantumCommand {
    private final Location location;
    private QuantumGate removedGate;

    public RemoveGateCommand(Location location) {
        this.location = location;
    }

    @Override
    public void execute() {
        removedGate = location.circuit().removeGate(location.wire(), location.depth());
    }

    @Override
    public void undo() {
        if (removedGate != null) {
            location.circuit().add(removedGate, location.wire(), location.depth());
        }
    }

    @Override
    public boolean canUndo() {
        return removedGate != null;
    }

    @Override
    public void redo() {
        execute();
    }

    public QuantumGate getRemovedGate() {
        return removedGate;
    }
}