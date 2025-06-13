package QuantumCore.QuantumGatesFactory.ControlledGate;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public abstract class ToffoliGate extends QuantumGate {
    public ToffoliGate() {
        super(buildToffoli(), 3, "Toffoli");
    }

    private static Matrix buildToffoli() {
        Complex[][] toffoliG = new Complex[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                toffoliG[i][j] = new Complex(0, 0);
            }
        }
        toffoliG[0][0] = new Complex(1, 0);
        toffoliG[1][2] = new Complex(1, 0);
        toffoliG[2][2] = new Complex(1, 0);
        toffoliG[3][3] = new Complex(1, 0);
        toffoliG[4][4] = new Complex(1, 0);
        toffoliG[5][5] = new Complex(1, 0);
        toffoliG[6][7] = new Complex(1, 0);
        toffoliG[7][6] = new Complex(1, 0);
        return new Matrix(toffoliG);
    }
}