package QuantumCore.Core;

import MathCore.Matrix;
import MathCore.Complex;
import QuantumCore.QuantumPorts.QuantumGatePort;

public abstract class QuantumGate implements QuantumGatePort {
    private final Matrix matrix;
    private final int numQubits;
    private final String name;


    public QuantumGate(Matrix matrix, int numQubits, String name) {
        this.matrix = matrix;
        this.numQubits = numQubits;
        this.name = name;
    }

    public State apply(State state) {
        Complex[] newAmplitudes = matrix.multiplyVector(state.getAmplitudes());
        return new State(newAmplitudes, state.getNQubits());
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