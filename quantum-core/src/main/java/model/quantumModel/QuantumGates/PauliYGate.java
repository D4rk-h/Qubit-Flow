package model.quantumModel.QuantumGates;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.QuantumGate;

public class PauliYGate extends QuantumGate {
    public PauliYGate() {
        super(buildPauliY(), 1, "Pauli-Y");
    }
    private static Matrix buildPauliY(){
        Complex[][] pauliY = new Complex[2][2];
        pauliY[0][0] = new Complex(0, 0);
        pauliY[0][1] = new Complex(0, -1);
        pauliY[1][0] = new Complex(0, 1);
        pauliY[1][1] = new Complex(0, 0);
        return new Matrix(pauliY);
    }
}