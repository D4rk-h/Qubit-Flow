package model.quantumModel.quantumGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.QuantumGate;
import model.quantumModel.quantumState.QuantumState;

public class MultiQubitGateMarker extends QuantumGate {
    private final QuantumGate parentGate;
    private final int qubitIndex;
    private final int primaryQubit;

    public MultiQubitGateMarker(QuantumGate parentGate, int qubitIndex, int primaryQubit) {
        super(Matrix.createIdentityMatrix(2), 1, "●");
        this.parentGate = parentGate;
        this.qubitIndex = qubitIndex;
        this.primaryQubit = primaryQubit;
    }


    public QuantumGate getParentGate() { return parentGate; }
    public int getQubitIndex() { return qubitIndex; }
    public int getPrimaryQubit() { return primaryQubit; }

    @Override
    public QuantumState apply(QuantumState state) {
        return state;
    }
}
