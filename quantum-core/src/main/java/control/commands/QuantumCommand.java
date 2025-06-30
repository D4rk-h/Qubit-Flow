package control.commands;

public interface QuantumCommand {
    CommandResult execute();
    void undo();
    boolean canUndo();
    String getDescription();
}
