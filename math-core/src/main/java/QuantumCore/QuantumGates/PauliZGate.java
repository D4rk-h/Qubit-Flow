package QuantumCore.QuantumGates;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.QuantumGate;

public abstract class PauliZGate extends QuantumGate {
    public PauliZGate() {
        Complex[][] pZ = new Complex[2][2];
        pZ[0][0] = new Complex(1, 0);
        pZ[0][1] = new Complex(0, 0);
        pZ[1][0] = new Complex(0, 0);
        pZ[1][1] = new Complex(-1, 0);
        super(new Matrix(pZ), 1, "Pauli-Z");
    }
}