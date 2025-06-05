package QuantumCore.QuantumGatesFactory.ControlledGate;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public abstract class SwapGate extends QuantumGate {
    public SwapGate() {
        Complex[][] sG = new Complex[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sG[i][j] = new Complex(0, 0);
            }
        }
        sG[0][0] = new Complex(1, 0);
        sG[1][2] = new Complex(1, 0);
        sG[2][1] = new Complex(1, 0);
        sG[3][3] = new Complex(1, 0);
        super(new Matrix(sG), 2, "Swap");
    }
}