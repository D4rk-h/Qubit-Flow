package QuantumCore.QuantumGates.ControlledGate;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public abstract class FredkinGate extends QuantumGate {
    public FredkinGate() {
        Complex[][] fG = new Complex[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                fG[i][j] = new Complex(0, 0);
            }
        }
        fG[0][0] = new Complex(1, 0);
        fG[1][2] = new Complex(1, 0);
        fG[2][2] = new Complex(1, 0);
        fG[3][3] = new Complex(1, 0);
        fG[4][4] = new Complex(1, 0);
        fG[5][6] = new Complex(1, 0);
        fG[6][5] = new Complex(1, 0);
        fG[7][7] = new Complex(1, 0);
        super(new Matrix(fG), 3, "Fredkin (Controlled-swap)");
    }
}