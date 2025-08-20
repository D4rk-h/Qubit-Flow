package control.command.parser;

import model.quantumModel.quantumCircuit.QuantumCircuit;

public interface ExportParser {
    String serialize(QuantumCircuit circuit);
}
