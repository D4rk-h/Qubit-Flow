package control.command.circuitCommand.exportCommand;

import control.command.UndoableCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.io.File;

public class ExportQASMCommand implements UndoableCommand {
    private final QuantumCircuit circuit;
    private final String filename;
    private boolean wasExecuted;

    public ExportQASMCommand(QuantumCircuit circuit, String filename) {
        this.circuit = circuit;
        this.filename = filename;
        this.wasExecuted = false;
    }

    @Override
    public void execute() {
        QasmExportStrategy strategy = new QasmExportStrategy();
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

