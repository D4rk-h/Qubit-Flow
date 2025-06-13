package QuantumCore.QuantumGatesFactory.ControlledGate;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public abstract class ControlledZGate extends QuantumGate {
    public ControlledZGate() {
        super(buildControlledZ(), 2, "Controlled-Z");
    }

    private static Matrix buildControlledZ() {
        Complex[][] controlledZ = new Complex[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                controlledZ[i][j] = new Complex(0, 0);
            }
        }
        controlledZ[0][0] = new Complex(1, 0);
        controlledZ[1][1] = new Complex(1, 0);
        controlledZ[2][2] = new Complex(1, 0);
        controlledZ[3][3] = new Complex(-1, 0);
        return new Matrix(controlledZ);
    }
}