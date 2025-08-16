package model.quantumModel.quantumCircuit.quantumCircuitUtils;

import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumGate.QuantumGate;
import model.quantumModel.quantumGate.ControlGate.CNot;
import model.quantumModel.quantumGate.ControlGate.ControlGate;
import model.quantumModel.quantumGate.ControlGate.Swap;
import model.quantumModel.quantumGate.ControlGate.Toffoli;
import model.quantumModel.quantumGate.MultiQubitGateMarker;
import model.quantumModel.quantumGate.TGate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuantumCircuitValidation {

    public void validateQuantumGateBounds(QuantumCircuit quantumCircuit, int i, int j, QuantumGate gate) {
        if (i < 0 || i >= quantumCircuit.getnQubits() || j <= 0 || j >= quantumCircuit.getCircuit().get(0).size()) {throw new IndexOutOfBoundsException("Index out of bounds");}
    }

    public void validateBlochSphereBounds(QuantumCircuit quantumCircuit, int i, int j) {
        if (i < 0 || i >= quantumCircuit.getnQubits() || j < 0 || j >= quantumCircuit.getCircuit().get(0).size()) {throw new IndexOutOfBoundsException("Index out of bounds");}
    }

    public void validateQuantumStateBounds(QuantumCircuit quantumCircuit, int i) {
        if (i < 0 || i >= quantumCircuit.getnQubits()) {throw new IndexOutOfBoundsException("Index out of bounds");}
    }

    public void validateControlledGateBounds(ControlGate cGate, QuantumCircuit quantumCircuit, int i, int j) {
        if (i < 0 || j < 0 || i > (quantumCircuit.getCircuit().get(i).size() + 2) || j > (quantumCircuit.getCircuit().get(i).size() + 2)){throw new IllegalArgumentException("Index out of bounds");}
        if (i>quantumCircuit.getCircuit().size() || j>quantumCircuit.getCircuit().get(i).size()){extend(quantumCircuit.getCircuit(), i, j);}
    }

    private static void extend(List<List<Object>> circuit, int i, int j) {
        while (circuit.size() <= i) {circuit.add(new ArrayList<>());}
        while (circuit.get(i).size() <= j) {circuit.get(i).add(null);}
    }

    public boolean isValidCircuit(QuantumCircuit circuit) {
        for (int depth = 0; depth < circuit.getDepth(); depth++) {
            Set<Integer> usedQubits = new HashSet<>();
            for (int wire = 0; wire < circuit.getCircuit().size(); wire++) {
                if (depth < circuit.getCircuit().get(wire).size()) {
                    Object element = circuit.getCircuit().get(wire).get(depth);
                    if (element instanceof QuantumGate && !(element instanceof MultiQubitGateMarker)) {
                        for (int targetQubit : ((QuantumGate) element).getTargetQubits()) {
                            if (!usedQubits.add(targetQubit)) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void validateControlPlacement(QuantumCircuit circuit, int targetWire, int targetDepth) {
        Object targetGate = circuit.getCircuit().get(targetWire).get(targetDepth);
        if ((targetWire - 1) < 0 || targetDepth <= 0) throw new IllegalArgumentException("Target coordinates out of bounds");
        if (targetGate instanceof Swap || targetGate instanceof TGate) {
            throw new IllegalArgumentException("Control cannot be added to this gate");
        }
        if (!(targetGate instanceof QuantumGate) && !(targetGate instanceof Toffoli) && !(targetGate instanceof CNot)) {
            throw new IllegalArgumentException("Control cannot be added to something that isnt a gate");
        }
    }

}
