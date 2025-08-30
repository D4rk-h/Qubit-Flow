package model.quantumModel.quantumGate.singleQubitGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

public class TDaggerGate extends QuantumGate {
    public TDaggerGate() {
        super(buildTDagger(), 1, "T Dagger (-π/8)");
    }

    private static Matrix buildTDagger() {
        Complex[][] tDaggerGate = new Complex[2][2];
        tDaggerGate[0][0] = Complex.ONE;
        tDaggerGate[0][1] = Complex.ZERO;
        tDaggerGate[1][0] = Complex.ZERO;
        tDaggerGate[1][1] = Complex.exponential(-Math.PI / 4);
        return new Matrix(tDaggerGate);
    }
}