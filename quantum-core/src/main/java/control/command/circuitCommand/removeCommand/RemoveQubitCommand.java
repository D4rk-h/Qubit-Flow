package control.command.circuitCommand.removeCommand;

import control.command.QuantumCommand;
import model.commandsModel.Location;
import model.quantumModel.quantumState.QuantumState;

import java.util.List;

public class RemoveQubitCommand implements QuantumCommand {
    private final Location location;
    private List<Object> removedWire;

    public RemoveQubitCommand(Location location) {
        this.location = location;
    }

    @Override
    public void execute() {
        removedWire = location.circuit().removeWire(location.wire());
    }

    @Override
    public void undo() {
        if (removedWire != null) {
            location.circuit().add((QuantumState) removedWire.getFirst(), location.wire());
        }
    }

    @Override
    public boolean canUndo() {
        return removedWire != null;
    }

    @Override
    public void redo() {
        execute();
    }

    public List<Object> getRemovedWire() {
        return removedWire;
    }

    public QuantumState getRemovedState() {
        return (QuantumState) removedWire.getFirst();
    }
}