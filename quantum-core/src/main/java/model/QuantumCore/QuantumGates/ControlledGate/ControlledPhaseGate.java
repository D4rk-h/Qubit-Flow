package model.QuantumCore.QuantumGates.ControlledGate;

import model.MathCore.Complex;
import model.MathCore.Matrix;
import model.QuantumCore.QuantumGate;
import model.QuantumCore.Qubit;

public class ControlledPhaseGate extends ControlledGate {
    public ControlledPhaseGate() {super(new Qubit[1], new Qubit[1], buildControlledPhaseGate());}

    private static QuantumGate buildControlledPhaseGate() {
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
        return new QuantumGate(new Matrix(cPhase), 2, "Controlled-Phase");
    }
}
