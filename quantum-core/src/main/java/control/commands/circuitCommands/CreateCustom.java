package control.commands.circuitCommands;

import control.commands.QuantumCommand;

public class CreateCustom implements QuantumCommand {
    @Override
    public void execute() {

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
