package model.quantumModel.QuantumCircuit.quantumCircuitUtils;

import model.quantumModel.QuantumGate;
import model.quantumModel.QuantumGates.HadamardGate;
import model.quantumModel.QuantumGates.PauliXGate;
import model.quantumModel.QuantumGates.PauliYGate;
import model.quantumModel.QuantumGates.PauliZGate;

import java.util.List;

public class QuantumCircuitSeekToMerge {

    public void seekToMergeZ(List<Object> wire) {
        for (int i=0; i<wire.size();i++) {
            if (wire.get(i) instanceof QuantumGate){
                if (wire.get(i) instanceof HadamardGate && wire.get(i + 1) instanceof PauliXGate && wire.get(i + 2) instanceof HadamardGate) {
                    wire.set(i, new PauliZGate());
                    wire.set(i + 1, null);
                    wire.set(i + 2, null);
                }
            }
        }
    }

    public void seekToMergeMinusY(List<Object> wire) {
        for (int i=0; i<wire.size();i++) {
            if (wire.get(i) instanceof QuantumGate){
                if (wire.get(i) instanceof HadamardGate && wire.get(i + 1) instanceof PauliYGate && wire.get(i + 2) instanceof HadamardGate) {
                    wire.set(i, new PauliYGate().getMatrix().multiply(-1));
                    wire.set(i + 1, null);
                    wire.set(i + 2, null);
                }
            }
        }
    }

    public void seekToMergeX(List<Object> wire) {
        for (int i=0; i<wire.size();i++) {
            if (wire.get(i) instanceof QuantumGate){
                if (wire.get(i) instanceof HadamardGate && wire.get(i + 1) instanceof PauliZGate && wire.get(i + 2) instanceof HadamardGate) {
                    wire.set(i, new PauliXGate());
                    wire.set(i + 1, null);
                    wire.set(i + 2, null);
                }
            }
        }
    }
}
