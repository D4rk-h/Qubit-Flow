package model.quantumModel.QuantumGates.ControlledGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.QuantumGate;

public class FredkinGate extends QuantumGate {
    public FredkinGate() {
        super(buildFredkinMatrix(), 3, "Fredkin (Controlled-swap)");
    }

    private static Matrix buildFredkinMatrix() {
        Complex[][] fredkinG = new Complex[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {fredkinG[i][j] = new Complex(0, 0);}
        }
        fredkinG[0][0] = new Complex(1, 0);
        fredkinG[1][2] = new Complex(1, 0);
        fredkinG[2][2] = new Complex(1, 0);
        fredkinG[3][3] = new Complex(1, 0);
        fredkinG[4][4] = new Complex(1, 0);
        fredkinG[5][6] = new Complex(1, 0);
        fredkinG[6][5] = new Complex(1, 0);
        fredkinG[7][7] = new Complex(1, 0);
        return new Matrix(fredkinG);
    }
}