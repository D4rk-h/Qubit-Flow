package QuantumCore.QuantumGatesFactory;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public abstract class PauliYGate extends QuantumGate {
    public PauliYGate() {
        Complex[][] pY = new Complex[2][2];
        pY[0][0] = new Complex(0, 0);
        pY[0][1] = new Complex(0, -1);
        pY[1][0] = new Complex(0, 1);
        pY[1][1] = new Complex(0, 0);
        super(new Matrix(pY), 1, "Pauli-Y");
    }
}