package control.command.circuitCommand.exportCommand;

import com.fasterxml.jackson.databind.ObjectMapper;
import control.command.QuantumCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExportJSON implements QuantumCommand {
    private final QuantumCircuit circuit;

    public ExportJSON(QuantumCircuit circuit) {this.circuit = circuit;}

    @Override
    public void execute() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> circuitData = new HashMap<>();
            circuitData.put("circuit", circuit.toString());
            circuitData.put("nQubit", circuit.getNQubits());
            circuitData.put("nLayer", circuit.getDepth());
            mapper.writeValue(new File("./exported_circuit.json"), circuitData);
        } catch (IOException e) {
            throw new RuntimeException("Error creating json response: "+ e.getMessage(), e);
        }
    }

    @Override
    public void undo() {
        File exportedFile = new File("./exported_circuit.json");
        if (exportedFile.exists()) exportedFile.delete();
    }

    @Override
    public boolean canUndo() {return true;}

    @Override
    public void redo() {execute();}
}
