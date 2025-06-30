package model.quantumModel.QuantumGates.ControlledGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.QuantumGate;
import model.quantumModel.Qubit;

public class ControlledXGate extends ControlledGate {
    public ControlledXGate() {super(new Qubit[1], new Qubit[1], buildControlledXGate());}

    private static QuantumGate buildControlledXGate(){
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
        return new QuantumGate(new Matrix(controlledX), 2, "Controlled-X");
    }
}