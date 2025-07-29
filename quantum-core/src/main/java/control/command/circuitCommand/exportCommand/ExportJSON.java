package control.command.circuitCommand.exportCommand;

import com.fasterxml.jackson.databind.ObjectMapper;
import control.command.QuantumCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.io.File;
import java.io.IOException;

public class ExportJSON implements QuantumCommand {
    private QuantumCircuit circuit;

    @Override
    public void execute() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonCircuit = mapper.writeValueAsString(circuit.getCircuit());
            mapper.writeValue(new File("./exported_circuit.json"), jsonCircuit);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void undo() {

    }

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public void redo() {

    }
}
