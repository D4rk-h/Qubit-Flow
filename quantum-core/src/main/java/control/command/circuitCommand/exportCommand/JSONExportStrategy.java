package control.command.circuitCommand.exportCommand;

import com.fasterxml.jackson.databind.ObjectMapper;
import control.ExportStrategy;
import control.command.UndoableCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JSONExportStrategy implements ExportStrategy {
    @Override
    public void export(QuantumCircuit circuit, String filename) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> circuitData = createCircuitData(circuit);
            mapper.writeValue(new File(filename), circuitData);
            System.out.println("Circuit exported to JSON: " + filename);
        } catch (IOException e) {
            throw new RuntimeException("Error exporting to JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public UndoableCommand createExportCommand(QuantumCircuit circuit, String filename) {
        return new control.export.ExportJSONCommand(circuit, filename);
    }

    private Map<String, Object> createCircuitData(QuantumCircuit circuit) {
        Map<String, Object> data = new HashMap<>();
        data.put("circuit", circuit.toString());
        data.put("numQubits", circuit.getNQubits());
        data.put("numLayers", circuit.getDepth());
        data.put("totalGates", circuit.getTotalGateCount());
        data.put("exportTimestamp", System.currentTimeMillis());
        return data;
    }
}

