package model.quantumModel.quantumGate.singleQubitGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

public class Phase extends QuantumGate {
    private double phi;

    public Phase(double phi) {
        super(buildPhase(phi), 1, "P(" + phi + ")");
        this.phi = phi;
    }

    public Phase() {this(Math.PI / 2);}

    private static Matrix buildPhase(double phi) {
        Complex[][] phaseGate = new Complex[2][2];
        phaseGate[0][0] = Complex.ONE;
        phaseGate[0][1] = Complex.ZERO;
        phaseGate[1][0] = Complex.ZERO;
        phaseGate[1][1] = Complex.exponential(phi);
        return new Matrix(phaseGate);
    }

    public double getPhi() {return phi;}

    public void setPhi(double phi) {
        this.phi = phi;
        this.updateMatrix(phi);
    }
}
