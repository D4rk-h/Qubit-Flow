package control;

import control.command.UndoableCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;

public interface ExportStrategy {
    void export(QuantumCircuit circuit, String filename);
    UndoableCommand createExportCommand(QuantumCircuit circuit, String filename);
}