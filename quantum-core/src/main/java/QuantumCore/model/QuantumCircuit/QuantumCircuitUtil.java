package QuantumCore.model.QuantumCircuit;

import QuantumCore.model.QuantumGates.ControlledGate.ControlledGate;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void seekControlled(ControlledGate controlledGate, int i, int j, List<List<Object>> circuit) {
        if (controlledGate.getGate().getName().contains("Fredkin")){
            circuit.get(i).set(j, controlledGate.getControlQubit());
            circuit.get(i+1).set(j, controlledGate);
            circuit.get(i+2).set(j, controlledGate);
        } else if (controlledGate.getGate().getName().contains("Toffoli")) {
            circuit.get(i).set(j, controlledGate.getControlQubit());
            circuit.get(i+1).set(j, controlledGate.getControlQubit());
            circuit.get(i+2).set(j, controlledGate);
        } else if (controlledGate.getGate().getName().equals("Swap")){
            circuit.get(i).set(j, controlledGate);
            circuit.get(i+1).set(j, controlledGate);
        } else {
            circuit.get(i).set(j, controlledGate.getControlQubit());
            circuit.get(i+1).set(j, controlledGate);
        }
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
