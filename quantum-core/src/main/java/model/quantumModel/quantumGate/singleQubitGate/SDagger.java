package model.quantumModel.quantumGate.singleQubitGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

public class SDagger extends QuantumGate {
    public SDagger() {
        super(buildSDagger(), 1, "S Dagger");
    }

    private static Matrix buildSDagger() {
        Complex[][] sDagger = new Complex[2][2];
        sDagger[0][0] = Complex.ONE;
        sDagger[0][1] = Complex.ZERO;
        sDagger[1][0] = Complex.ZERO;
        sDagger[1][1] = new Complex(0, -1);
        return new Matrix(sDagger);
    }
}