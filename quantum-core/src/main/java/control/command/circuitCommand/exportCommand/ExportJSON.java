package control.command.circuitCommand.exportCommand;

import com.fasterxml.jackson.databind.ObjectMapper;
import control.command.QuantumCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportJSON implements QuantumCommand {
    private final QuantumCircuit circuit;

    public ExportJSON(QuantumCircuit circuit) {
        this.circuit = circuit;
    }

    @Override
    public void execute() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = new HashMap<>();
            Map<String, List<Object>> circuitMap = new HashMap<>();
            List<List<Object>> circuitData = circuit.getCircuit();
            for (int i=0;i<circuitData.size();i++) {
                String qubitKey = "qubit-" + (i + 1);
                circuitMap.put(qubitKey, circuitData.get(i));
            }
            data.put("circuit", circuitMap);
            data.put("numOfQubits", circuit.getnQubits());
            data.put("depth", circuit.getDepth());
            mapper.writeValue(new File("./exported_circuit.json"), data);
        } catch (IOException e) {
            throw new RuntimeException("Error creating json response: "+ e.getMessage(), e);
        }
    }

    @Override
    public void undo() {
        File exportedFile = new File("./exported_circuit.json");
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
