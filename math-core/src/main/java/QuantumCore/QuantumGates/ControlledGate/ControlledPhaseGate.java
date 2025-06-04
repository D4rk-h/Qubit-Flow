package QuantumCore.QuantumGates.ControlledGate;

import MathCore.Complex;
import MathCore.Matrix;
import QuantumCore.QuantumGate;

public abstract class ControlledPhaseGate extends QuantumGate {
    public ControlledPhaseGate() {
        Complex[][] cPhase = new Complex[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cPhase[i][j] = new Complex(0, 0);
            }
        }
        cPhase[0][0] = new Complex(1, 0);
        cPhase[1][1] = new Complex(1, 0);
        cPhase[2][2] = new Complex(1, 0);
        cPhase[3][3] = new Complex(0, 1);
        super(new Matrix(cPhase), 2, "Controlled-Phase");

    }
}
