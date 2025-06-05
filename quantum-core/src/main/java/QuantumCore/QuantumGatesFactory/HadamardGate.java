package QuantumCore.QuantumGatesFactory;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public abstract class HadamardGate extends QuantumGate {
    public HadamardGate() {
        Complex[][] h = new Complex[2][2];
        double value = 1 / Math.sqrt(2);
        h[0][0] = new Complex(value, 0);
        h[0][1] = new Complex(value, 0);
        h[1][0] = new Complex(value, 0);
        h[1][1] = new Complex(-value, 0);
        super(new Matrix(h), 1, "Hadamard");
    }
}
