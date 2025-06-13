package QuantumCore.QuantumGatesFactory;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public class PauliYGate extends QuantumGate {
    public PauliYGate() {
        super(buildPauliY(), 1, "Pauli-Y");
    }
    private static Matrix buildPauliY(){
        Complex[][] pauliY = new Complex[2][2];
        pauliY[0][0] = new Complex(0, 0);
        pauliY[0][1] = new Complex(0, -1);
        pauliY[1][0] = new Complex(0, 1);
        pauliY[1][1] = new Complex(0, 0);
        return new Matrix(pauliY);
    }
}