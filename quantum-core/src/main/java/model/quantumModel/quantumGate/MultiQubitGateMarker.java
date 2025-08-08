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
        super(createIdentityMatrix(), 1, "●");
        this.parentGate = parentGate;
        this.qubitIndex = qubitIndex;
        this.primaryQubit = primaryQubit;
    }

    private static Matrix createIdentityMatrix() {
        Complex[][] identity = new Complex[2][2];
        identity[0][0] = Complex.ONE;
        identity[0][1] = Complex.ZERO;
        identity[1][0] = Complex.ZERO;
        identity[1][1] = Complex.ONE;
        return new Matrix(identity);
    }

    public QuantumGate getParentGate() { return parentGate; }
    public int getQubitIndex() { return qubitIndex; }
    public int getPrimaryQubit() { return primaryQubit; }

    @Override
    public QuantumState apply(QuantumState state) {
        return state;
    }
}
