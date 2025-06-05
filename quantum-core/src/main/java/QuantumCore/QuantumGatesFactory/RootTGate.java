package QuantumCore.QuantumGatesFactory;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public abstract class RootTGate extends QuantumGate {
    public RootTGate() {
        Complex[][] rootTG = new Complex[2][2];
        rootTG[0][0] = new Complex(1, 0);
        rootTG[0][1] = new Complex(0, 0);
        rootTG[1][0] = new Complex(0, 0);
        rootTG[1][1] = Complex.exponential(Math.PI / 4);
        super(new Matrix(rootTG), 1, "√T (π/8)");
    }
}