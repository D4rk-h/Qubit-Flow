package model.QuantumCore.QuantumGates.ControlledGate;

import model.MathCore.Complex;
import model.MathCore.Matrix;
import model.QuantumCore.QuantumGate;
import model.QuantumCore.Qubit;

public class FredkinGate extends ControlledGate {
    public FredkinGate() {
        super(new Qubit[1], new Qubit[2], buildFredkinGate());
    }

    private static QuantumGate buildFredkinGate() {
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
        return new QuantumGate(new Matrix(fredkinG), 3, "Fredkin (Controlled-swap)");
    }
}