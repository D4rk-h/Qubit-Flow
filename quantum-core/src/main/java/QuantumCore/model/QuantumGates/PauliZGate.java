package QuantumCore.model.QuantumGates;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.model.QuantumGate;

public class PauliZGate extends QuantumGate {
    public PauliZGate() {
        super(buildPauliZ(), 1, "Pauli-Z");
    }

    private static Matrix buildPauliZ() {
        Complex[][] pauliZ = new Complex[2][2];
        pauliZ[0][0] = new Complex(1, 0);
        pauliZ[0][1] = new Complex(0, 0);
        pauliZ[1][0] = new Complex(0, 0);
        pauliZ[1][1] = new Complex(-1, 0);
        return new Matrix(pauliZ);
    }
}