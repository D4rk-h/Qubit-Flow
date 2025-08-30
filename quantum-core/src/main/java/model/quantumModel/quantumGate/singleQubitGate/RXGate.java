package model.quantumModel.quantumGate.singleQubitGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

public class RXGate extends QuantumGate {
    private static double theta = Math.PI;
    public RXGate() {
        super(buildRXGate(), 1, "RX (Rotation theta around x-axis)");
    }

    private static Matrix buildRXGate() {
        Complex[][] pauliX = new Complex[2][2];
        pauliX[0][0] = new Complex (Math.cos(theta/2), 0);
        pauliX[0][1] = new Complex (0, -Math.sin(theta/2));
        pauliX[1][0] = new Complex (0, -Math.sin(theta/2));
        pauliX[1][1] = new Complex (Math.cos(theta/2), 0);
        return new Matrix(pauliX);
    }

    public double getTheta() {return theta;}

    public void setTheta(double theta) {this.theta = theta;}
}