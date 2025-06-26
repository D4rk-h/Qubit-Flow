package model.QuantumCore.QuantumGates.ControlledGate;

import model.MathCore.Complex;
import model.MathCore.Matrix;
import model.QuantumCore.QuantumGate;
import model.QuantumCore.Qubit;

public class ControlledZGate extends ControlledGate {
    public ControlledZGate() {super(new Qubit[1], new Qubit[1], buildControlledZGate());}

    private static QuantumGate buildControlledZGate() {
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
        return new QuantumGate(new Matrix(controlledZ), 2, "Controlled Z");
    }
}