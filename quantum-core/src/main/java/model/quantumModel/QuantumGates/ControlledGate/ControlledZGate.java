package model.quantumModel.QuantumGates.ControlledGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.QuantumGate;

public class ControlledZGate extends QuantumGate {
    public ControlledZGate() {super(buildControlledZMatrix(), 2, "Controlled Z");}

    private static Matrix buildControlledZMatrix() {
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
    public static int[] controlQubits() {
        int[] result = new int[1];
        result[0] = 0;
        return result;
    }
}