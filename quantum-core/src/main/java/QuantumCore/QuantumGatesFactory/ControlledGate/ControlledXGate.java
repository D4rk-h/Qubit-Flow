package QuantumCore.QuantumGatesFactory.ControlledGate;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public abstract class ControlledXGate extends QuantumGate {
    public ControlledXGate() {
        super(buildControlledX(), 2, "Controlled-X");
    }

    private static Matrix buildControlledX(){
        Complex[][] controlledX = new Complex[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                controlledX[i][j] = new Complex(0, 0);
            }
        }
        controlledX[0][0] = new Complex(1, 0);
        controlledX[1][1] = new Complex(1, 0);
        controlledX[2][3] = new Complex(1, 0);
        controlledX[3][2] = new Complex(1, 0);
        return new Matrix(controlledX);
    }
}