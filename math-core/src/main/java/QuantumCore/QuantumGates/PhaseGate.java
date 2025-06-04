package QuantumCore.QuantumGates;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.Core.QuantumGate;

public abstract class PhaseGate extends QuantumGate {
    public PhaseGate() {
        Complex[][] phaseG = new Complex[2][2];
        phaseG[0][0] = new Complex(1, 0);
        phaseG[0][1] = new Complex(0, 0);
        phaseG[1][0] = new Complex(0, 0);
        phaseG[1][1] = new Complex(0, 1);
        super(new Matrix(phaseG), 1, "Phase");
    }
}