package QuantumCore.QuantumGatesFactory.ControlledGate;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public abstract class ControlledXGate extends QuantumGate {
    public ControlledXGate() {
        Complex[][] cX = new Complex[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cX[i][j] = new Complex(0, 0);
            }
        }
        cX[0][0] = new Complex(1, 0);
        cX[1][1] = new Complex(1, 0);
        cX[2][3] = new Complex(1, 0);
        cX[3][2] = new Complex(1, 0);
        super(new Matrix(cX), 2, "Controlled-X");
    }
}