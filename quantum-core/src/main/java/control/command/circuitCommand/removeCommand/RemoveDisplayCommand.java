package control.command.circuitCommand.removeCommand;

import control.command.QuantumCommand;
import model.quantumModel.measurementDisplay.Display;
import model.quantumModel.quantumCircuit.QuantumCircuit;

public class RemoveDisplayCommand implements QuantumCommand {
    private final Display display;
    private final QuantumCircuit circuit;
    private boolean wasRemoved = false;

    public RemoveDisplayCommand(Display display, QuantumCircuit circuit) {
        this.display = display;
        this.circuit = circuit;
    }

    @Override
    public void execute() {
        wasRemoved = circuit.removeDisplay(display);
    }

    @Override
    public void undo() {
        if (wasRemoved) {
            circuit.addDisplay(display);
        }
    }

    @Override
    public boolean canUndo() {
        return wasRemoved;
    }

    @Override
    public void redo() {
        execute();
    }

    public Display getDisplay() {
        return display;
    }

    public boolean wasSuccessfullyRemoved(Display display) {
        return display != null;
    }
}

