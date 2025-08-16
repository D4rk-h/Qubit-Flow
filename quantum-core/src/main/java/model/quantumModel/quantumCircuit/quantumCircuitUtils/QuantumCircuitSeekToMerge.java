package model.quantumModel.quantumCircuit.quantumCircuitUtils;

import model.quantumModel.quantumGate.QuantumGate;
import model.quantumModel.quantumGate.HadamardGate;
import model.quantumModel.quantumGate.NotGate;
import model.quantumModel.quantumGate.PauliYGate;
import model.quantumModel.quantumGate.PauliZGate;

import java.util.List;

public class QuantumCircuitSeekToMerge {

    public void seekToMergeZ(List<Object> wire) {
        for (int i=0; i<wire.size();i++) {
            if (wire.get(i) instanceof QuantumGate){
                if (wire.get(i) instanceof HadamardGate && wire.get(i + 1) instanceof NotGate && wire.get(i + 2) instanceof HadamardGate) {
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
                    wire.set(i, new NotGate());
                    wire.set(i + 1, null);
                    wire.set(i + 2, null);
                }
            }
        }
    }
}
