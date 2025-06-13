package QuantumCore.QuantumGatesFactory;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public class PauliXGate extends QuantumGate {
    public PauliXGate() {
        super(buildPauliX(), 1, "Pauli-X");
    }

    private static Matrix buildPauliX() {
        Complex[][] pauliX = new Complex[2][2];
        pauliX[0][0] = new Complex(0, 0);
        pauliX[0][1] = new Complex(1, 0);
        pauliX[1][0] = new Complex(1, 0);
        pauliX[1][1] = new Complex(0, 0);
        return new Matrix(pauliX);
    }
}