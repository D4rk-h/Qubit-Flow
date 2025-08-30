package model.quantumModel.quantumGate.singleQubitGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

public class RXGate extends QuantumGate {
    private static double theta = Math.PI;

    public RXGate() {
        super(buildRXGate(), 1, "RX (Rotates theta around x-axis)");
    }

    private static Matrix buildRXGate() {
        Complex[][] rx = new Complex[2][2];
        rx[0][0] = new Complex (Math.cos(theta/2), 0);
        rx[0][1] = new Complex (0, -Math.sin(theta/2));
        rx[1][0] = new Complex (0, -Math.sin(theta/2));
        rx[1][1] = new Complex (Math.cos(theta/2), 0);
        return new Matrix(rx);
    }

    public double getTheta() {return theta;}

    public void setTheta(double theta) {this.theta = theta;}
}