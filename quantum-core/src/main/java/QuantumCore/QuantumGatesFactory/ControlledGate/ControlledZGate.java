package QuantumCore.QuantumGatesFactory.ControlledGate;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public abstract class ControlledZGate extends QuantumGate {
    public ControlledZGate() {
        Complex[][] cZ = new Complex[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cZ[i][j] = new Complex(0, 0);
            }
        }
        cZ[0][0] = new Complex(1, 0);
        cZ[1][1] = new Complex(1, 0);
        cZ[2][2] = new Complex(1, 0);
        cZ[3][3] = new Complex(-1, 0);
        super(new Matrix(cZ), 2, "Controlled-Z");
    }
}