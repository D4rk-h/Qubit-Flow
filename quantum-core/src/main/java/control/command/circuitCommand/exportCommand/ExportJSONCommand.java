package control.export;

import control.command.UndoableCommand;
import control.command.circuitCommand.exportCommand.JSONExportStrategy;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.io.File;

/**
 * Command for JSON export with undo capability
 */
public class ExportJSONCommand implements UndoableCommand {
    private final QuantumCircuit circuit;
    private final String filename;
    private boolean wasExecuted;

    public ExportJSONCommand(QuantumCircuit circuit, String filename) {
        this.circuit = circuit;
        this.filename = filename;
        this.wasExecuted = false;
    }

    @Override
    public void execute() {
        JSONExportStrategy strategy = new JSONExportStrategy();
        strategy.export(circuit, filename);
        wasExecuted = true;
    }

    @Override
    public void undo() {
        if (wasExecuted) {
            File exportedFile = new File(filename);
            if (exportedFile.exists()) {
                exportedFile.delete();
                System.out.println("Removed exported file: " + filename);
            }
            wasExecuted = false;
        }
    }

    @Override
    public boolean canUndo() {
        return wasExecuted;
    }

    @Override
    public void redo() {
        execute();
    }
}
