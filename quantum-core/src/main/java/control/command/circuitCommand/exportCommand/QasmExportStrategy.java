package control.command.circuitCommand.exportCommand;

import control.ExportStrategy;
import control.command.UndoableCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.io.FileWriter;
import java.io.IOException;

public class QasmExportStrategy implements ExportStrategy {

    @Override
    public void export(QuantumCircuit circuit, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writeQASMHeader(writer);
            writeQASMBody(writer, circuit);
            System.out.println("Circuit exported to QASM: " + filename);
        } catch (IOException e) {
            throw new RuntimeException("Error exporting to QASM: " + e.getMessage(), e);
        }
    }

    @Override
    public UndoableCommand createExportCommand(QuantumCircuit circuit, String filename) {
        return new ExportQASMCommand(circuit, filename);
    }

    private void writeQASMHeader(FileWriter writer) throws IOException {
        writer.write("OPENQASM 2.0;\n");
        writer.write("include \"qelib1.inc\";\n");
    }

    private void writeQASMBody(FileWriter writer, QuantumCircuit circuit) throws IOException {
        writer.write("qreg q[" + circuit.getNQubits() + "];\n");
        writer.write("creg c[" + circuit.getNQubits() + "];\n");

        // TODO: Implement circuit to QASM translation
        writer.write("// Circuit operations would be translated here\n");
    }
}