package control.command;

public interface QuantumCommand extends CommandPort {
    @Override
    void execute();
    void undo();
    boolean canUndo();
    void redo();
}
