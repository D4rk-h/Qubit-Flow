package model.quantumModel.quantumGate.singleQubitGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

public class XRootGate extends QuantumGate {
    public XRootGate() {
        super(buildXRoot(), 1, "√X");
    }

    private static Matrix buildXRoot() {
        Complex[][] xRootGate = new Complex[2][2];
        Complex onePlusI = new Complex(1, 1);
        Complex oneMinusI = new Complex(1, -1);
        Complex half = new Complex(0.5, 0);
        xRootGate[0][0] = onePlusI.multiply(half);
        xRootGate[0][1] = oneMinusI.multiply(half);
        xRootGate[1][0] = oneMinusI.multiply(half);
        xRootGate[1][1] = onePlusI.multiply(half);
        return new Matrix(xRootGate);
    }
}