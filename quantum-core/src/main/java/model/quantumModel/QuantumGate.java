package model.quantumModel;

import model.mathModel.Matrix;
import model.quantumModel.QuantumPorts.QuantumGatePort;

public class QuantumGate implements QuantumGatePort {
    private final Matrix matrix;
    private final int numQubits;
    private final String name;


    public QuantumGate(Matrix matrix, int numQubits, String name) {
        this.matrix = matrix;
        this.numQubits = numQubits;
        this.name = name;
        int expectedSize = (int) Math.pow(2, numQubits);
        if (matrix.getRows() != expectedSize || matrix.getCols() != expectedSize) {
            throw new IllegalArgumentException("Matrix size must be " + expectedSize + "x" + expectedSize);
        }
    }

    @Override
    public QuantumState apply(QuantumState state) {
        return state.applyGate(this);
    }

    public Matrix getMatrix() {
        return matrix;
    }
    public String getName() {
        return name;
    }
    public int getNumQubits() {
        return numQubits;
    }
}