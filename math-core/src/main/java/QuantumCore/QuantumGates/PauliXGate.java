package QuantumCore.QuantumGates;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.QuantumGate;

public abstract class PauliXGate extends QuantumGate {
    public PauliXGate() {
        Complex[][] pX = new Complex[2][2];
        pX[0][0] = new Complex(0, 0);
        pX[0][1] = new Complex(1, 0);
        pX[1][0] = new Complex(1, 0);
        pX[1][1] = new Complex(0, 0);
        super(new Matrix(pX), 1, "Pauli-X");
    }
}