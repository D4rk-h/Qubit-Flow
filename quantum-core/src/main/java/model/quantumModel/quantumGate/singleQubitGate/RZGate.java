package model.quantumModel.quantumGate.singleQubitGate;


import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

public class RZGate extends QuantumGate {
    private double phi;

    public RZGate(double phi) {
        super(buildRZGate(phi), 1, "RZ(" + phi + ")");
        this.phi = phi;
    }

    public RZGate() {
        this(Math.PI);
    }

    private static Matrix buildRZGate(double phi) {
        Complex[][] rz = new Complex[2][2];
        rz[0][0] = Complex.exponential(-phi/2);
        rz[0][1] = Complex.ZERO;
        rz[1][0] = Complex.ZERO;
        rz[1][1] = Complex.exponential(phi/2);
        return new Matrix(rz);
    }

    public double getPhi() { return phi; }

    public void setPhi(double phi) {
        this.phi = phi;
        this.updateMatrix(phi);
    }
}