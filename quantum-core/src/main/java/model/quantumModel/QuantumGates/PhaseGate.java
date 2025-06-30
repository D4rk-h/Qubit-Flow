package model.quantumModel.QuantumGates;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.QuantumGate;

public class PhaseGate extends QuantumGate {
    public PhaseGate() {
        super(buildPhase(), 1, "Phase");
    }

    private static Matrix buildPhase() {
        Complex[][] phaseG = new Complex[2][2];
        phaseG[0][0] = new Complex(1, 0);
        phaseG[0][1] = new Complex(0, 0);
        phaseG[1][0] = new Complex(0, 0);
        phaseG[1][1] = new Complex(0, 1);
        return new Matrix(phaseG);
    }
}