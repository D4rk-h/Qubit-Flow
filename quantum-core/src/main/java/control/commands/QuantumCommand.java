package control.commands;

public interface QuantumCommand extends CommandPort {
    @Override
    void execute();
    void undo();
    boolean canUndo();
    void redo();
    String getDescription();
}
