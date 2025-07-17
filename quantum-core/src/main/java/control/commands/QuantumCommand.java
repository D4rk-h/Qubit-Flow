package control.commands;

public interface QuantumCommand {
    void execute();
    void undo();
    boolean canUndo();
    String getDescription();
}
