package QuantumCore.Core;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.QuantumPorts.QuantumGatePort;

public class QuantumGate implements QuantumGatePort {
    private final Matrix matrix;
    private final int numQubits;
    private final String name;


    public QuantumGate(Matrix matrix, int numQubits, String name) {
        this.matrix = matrix;
        this.numQubits = numQubits;
        this.name = name;
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

    @Override
    public State apply(State state) {
        Complex[] newAmplitudes = matrix.multiplyVector(state.getAmplitudes());
        for (int i=0;i<newAmplitudes.length;i++){
            newAmplitudes[i] = new Complex(newAmplitudes[i].getRealPart() / findTotalNorm(newAmplitudes),
                    newAmplitudes[i].getImaginaryPart() / findTotalNorm(newAmplitudes));
        }
        return new State(newAmplitudes, state.getNQubits());
    }

    private static double findTotalNorm(Complex[] amplitudes) {
        double totalNorm = 0.0;
        for (Complex amplitude : amplitudes) {
            totalNorm += amplitude.magnitude() * amplitude.magnitude();
        }
        totalNorm = Math.sqrt(totalNorm);
        return totalNorm;
    }
}