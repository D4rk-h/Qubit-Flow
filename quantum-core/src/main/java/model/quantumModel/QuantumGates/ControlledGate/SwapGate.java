package model.quantumModel.QuantumGates.ControlledGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.QuantumGate;
import model.quantumModel.Qubit;

public class SwapGate extends ControlledGate {
    public SwapGate() {super(new Qubit[1], new Qubit[1], buildSwapGate());}

    private static QuantumGate buildSwapGate() {
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
        return new QuantumGate(new Matrix(swapG), 2, "Swap");
    }
}