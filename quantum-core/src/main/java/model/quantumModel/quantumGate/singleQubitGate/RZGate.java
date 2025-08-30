package model.quantumModel.quantumGate.singleQubitGate;


import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

public class RZGate extends QuantumGate {
    private double phi = Math.PI;

    public RZGate() {
        super(buildRZGate(Math.PI), 1, "RZ (Rotates theta around Z-axis)");
    }

    public RZGate(double phi) {
        super(buildRZGate(phi), 1, "RZ (Rotates theta around Z-axis)");
        this.phi = phi;
    }

    private static Matrix buildRZGate(double phi) {
        Complex[][] rz = new Complex[2][2];
        rz[0][0] = Complex.exponential(-phi/2);
        rz[0][1] = Complex.ZERO;
        rz[1][0] = Complex.ZERO;
        rz[1][1] = Complex.exponential(phi/2);
        return new Matrix(rz);
    }

    public double getPhi() {return phi;}

    public void setPhi(double phi) {
        this.phi = phi;
        new RZGate(phi);
    }
}