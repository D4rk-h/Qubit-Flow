package QuantumCore.QuantumGates.ControlledGate;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.QuantumGate;

public abstract class ToffoliGate extends QuantumGate {
    public ToffoliGate() {
        Complex[][] tG = new Complex[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tG[i][j] = new Complex(0, 0);
            }
        }
        tG[0][0] = new Complex(1, 0);
        tG[1][2] = new Complex(1, 0);
        tG[2][2] = new Complex(1, 0);
        tG[3][3] = new Complex(1, 0);
        tG[4][4] = new Complex(1, 0);
        tG[5][5] = new Complex(1, 0);
        tG[6][7] = new Complex(1, 0);
        tG[7][6] = new Complex(1, 0);
        super(new Matrix(tG), 3, "Toffoli");
    }
}