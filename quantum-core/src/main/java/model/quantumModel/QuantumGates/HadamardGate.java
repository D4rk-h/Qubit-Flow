package model.quantumModel.QuantumGates;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.QuantumGate;

public class HadamardGate extends QuantumGate {
    public HadamardGate() {
        super(buildHadamard(), 1, "Hadamard");
    }

    private static Matrix buildHadamard(){
        Complex[][] hadamard = new Complex[2][2];
        double value = 1 / Math.sqrt(2);
        hadamard[0][0] = new Complex(value, 0);
        hadamard[0][1] = new Complex(value, 0);
        hadamard[1][0] = new Complex(value, 0);
        hadamard[1][1] = new Complex(-value, 0);
        return new Matrix(hadamard);
    }
}
