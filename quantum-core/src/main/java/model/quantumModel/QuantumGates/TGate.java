package model.quantumModel.QuantumGates;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.QuantumGate;

public class TGate extends QuantumGate {
    public TGate() {
        super(buildT(), 1, "√T (π/8)");
    }

    private static Matrix buildT() {
        Complex[][] tGate = new Complex[2][2];
        tGate[0][0] = new Complex(1, 0);
        tGate[0][1] = new Complex(0, 0);
        tGate[1][0] = new Complex(0, 0);
        tGate[1][1] = Complex.exponential(Math.PI / 4);
        return new Matrix(tGate);
    }
}