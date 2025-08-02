package control.command.circuitCommand.exportCommand;

import com.fasterxml.jackson.databind.ObjectMapper;
import control.command.QuantumCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumCircuit.quantumCircuitUtils.cliVisualization.QuantumCircuitCLIDisplay;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExportJSON implements QuantumCommand {
    private final QuantumCircuit circuit;
    public QuantumCircuitCLIDisplay utils = new QuantumCircuitCLIDisplay();

    public ExportJSON(QuantumCircuit circuit) {
        this.circuit = circuit;
    }

    @Override
    public void execute() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> circuitData = new HashMap<>();
            Map<String, String> wire = new HashMap<>();
            for (int i=0;i<circuit.getCircuit().size();i++) {
                String nWire = "wire-" + i;
                wire.put(nWire, utils.formatWire(circuit.getCircuit().get(i)));
                circuitData.put("circuit", wire);
            }
            circuitData.put("numOfQubits", circuit.getnQubits());
            circuitData.put("depth", circuit.getDepth());
            mapper.writeValue(new File("./exported_circuit.json"), circuitData);
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
