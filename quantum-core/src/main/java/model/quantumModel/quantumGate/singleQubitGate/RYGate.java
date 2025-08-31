package model.quantumModel.quantumGate.singleQubitGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

public class RYGate extends QuantumGate {
    private double theta;

    public RYGate(double theta) {
        super(buildRYGate(theta), 1, "RY(" + theta + ")");
        this.theta = theta;
    }

    public RYGate() {
        this(Math.PI);
    }

    private static Matrix buildRYGate(double theta) {
        Complex[][] ry = new Complex[2][2];
        ry[0][0] = new Complex(Math.cos(theta/2), 0);
        ry[0][1] = new Complex(-Math.sin(theta/2), 0);
        ry[1][0] = new Complex(Math.sin(theta/2), 0);
        ry[1][1] = new Complex(Math.cos(theta/2), 0);
        return new Matrix(ry);
    }

    public double getTheta() { return theta; }

    public void setTheta(double theta) {
        this.theta = theta;
        this.updateMatrix(theta);
    }
}
