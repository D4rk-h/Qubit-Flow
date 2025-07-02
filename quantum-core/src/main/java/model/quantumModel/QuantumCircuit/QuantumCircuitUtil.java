package model.quantumModel.QuantumCircuit;

import model.quantumModel.QuantumGates.ControlledGate.ControlledGate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuantumCircuitUtil {
    public String formatCircuit(List<List<Object>> circuit) {
        return circuit.stream()
                .map(row -> "[" + row.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")) + "]")
                .collect(Collectors.joining(",\n  ", "[\n  ", "\n]"));
    }

    public void extend(List<List<Object>> circuit, int i, int j) {
        while (circuit.size() <= i) {
            circuit.add(new ArrayList<>());
        }
        while (circuit.get(i).size() <= j) {
            circuit.get(i).add(null);
        }
    }
}
