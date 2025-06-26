package QuantumCore.model.QuantumGates.ControlledGate;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.model.QuantumGate;

public class SwapGate extends QuantumGate {
    public SwapGate() {
        super(buildSwap(), 2, "Swap Controlled");
    }

    private static Matrix buildSwap() {
        Complex[][] swapG = new Complex[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                swapG[i][j] = new Complex(0, 0);
            }
        }
        swapG[0][0] = new Complex(1, 0);
        swapG[1][2] = new Complex(1, 0);
        swapG[2][1] = new Complex(1, 0);
        swapG[3][3] = new Complex(1, 0);
        return new Matrix(swapG);
    }
}