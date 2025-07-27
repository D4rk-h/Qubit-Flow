package control.command.circuitCommand;

import control.command.QuantumCommand;
import model.commandsModel.Location;
import model.quantumModel.QuantumGate;
import model.quantumModel.QuantumState;
import model.quantumModel.measurementDisplay.Display;

//Todo develop RemoveCircuitCommand

public class RemoveCommand implements QuantumCommand {
    private final Object elementToRemove;
    private final Location location;
    private Object removedElement;

    public RemoveCommand(Object elementToRemove, Location location) {
        this.elementToRemove = elementToRemove;
        this.location = location;
    }

    @Override
    public void execute() {
        if (elementToRemove instanceof QuantumGate) {
            removedElement = location.circuit().removeGate(location.wire(), location.depth());
        } else if (elementToRemove instanceof QuantumState) {
            removedElement = location.circuit().removeWire(location.wire());
        } else if (elementToRemove instanceof Display) {
            removedElement = location.circuit().removeDisplay((Display) elementToRemove);
        }
    }

    @Override
    public void undo() {
        if (removedElement != null) {
            if (removedElement instanceof QuantumGate) {
                location.circuit().add((QuantumGate) removedElement, location.wire(), location.depth());
            } else if (removedElement instanceof QuantumState) {
                location.circuit().add((QuantumState) removedElement, location.wire());
            } else if (removedElement instanceof Display) {
                location.circuit().add((Display) removedElement);
            }
        }
    }

    @Override
    public boolean canUndo() {
        return removedElement != null;
    }

    @Override
    public void redo() {
        execute();
    }
}
