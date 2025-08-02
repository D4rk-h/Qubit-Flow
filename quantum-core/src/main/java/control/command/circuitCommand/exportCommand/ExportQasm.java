package control.command.circuitCommand.exportCommand;

import control.command.QuantumCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportQasm implements QuantumCommand {
    private final QuantumCircuit circuit;

    public ExportQasm(QuantumCircuit circuit) {
        this.circuit = circuit;
    }

    @Override
    public void execute() {
        try (FileWriter writer = new FileWriter("./exported_circuit.qasm");) {
            String header = "OPENQASM 2.0;\n" + "include " + "qelib1.inc;\n";
            writer.write(header);
            // Todo: Implementation of all kind of circuit elements translation to qasm
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void undo() {
        File exportedFile = new File("./exported_circuit.qasm");
        if (exportedFile.exists()) {
            exportedFile.delete();
        }
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void redo() {
        execute();
    }
}
