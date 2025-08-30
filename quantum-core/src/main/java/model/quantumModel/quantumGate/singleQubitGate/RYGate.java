package model.quantumModel.quantumGate.singleQubitGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

public class RYGate extends QuantumGate {
    private static double theta = Math.PI;

    public RYGate() {
        super(buildRYGate(), 1, "RY (Rotates theta around Y-axis)");
    }

    private static Matrix buildRYGate() {
        Complex[][] ry = new Complex[2][2];
        ry[0][0] = new Complex (Math.cos(theta/2), 0);
        ry[0][1] = new Complex (-Math.sin(theta/2),0);
        ry[1][0] = new Complex (Math.sin(theta/2), 0);
        ry[1][1] = new Complex (Math.cos(theta/2), 0);
        return new Matrix(ry);
    }

    public double getTheta() {return theta;}

    public void setTheta(double theta) {this.theta = theta;}
}