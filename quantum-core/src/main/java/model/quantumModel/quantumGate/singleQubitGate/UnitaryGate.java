package model.quantumModel.quantumGate.singleQubitGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

public class UnitaryGate extends QuantumGate {
    private double theta = Math.PI;
    private double phi = Math.PI;
    private double lambda = Math.PI;

    public UnitaryGate() {
        super(buildUnitary(Math.PI, Math.PI, Math.PI), 1, "U(" + Math.PI + "," + Math.PI + "," + Math.PI + ")");
    }

    private UnitaryGate(double theta, double phi, double lambda) {
        super(buildUnitary(theta, phi, lambda), 1, "U(" + theta + "," + phi + "," + lambda + ")");
        this.theta = theta;
        this.phi = phi;
        this.lambda = lambda;
    }

    private static Matrix buildUnitary(double theta, double phi, double lambda) {
        Complex[][] unitaryGate = new Complex[2][2];
        double cosHalfTheta = Math.cos(theta / 2);
        double sinHalfTheta = Math.sin(theta / 2);
        unitaryGate[0][0] = new Complex(cosHalfTheta, 0);
        Complex eiLambda = Complex.exponential(lambda);
        unitaryGate[0][1] = eiLambda.multiply(new Complex(-sinHalfTheta, 0));
        Complex eiPhi = Complex.exponential(phi);
        unitaryGate[1][0] = eiPhi.multiply(new Complex(sinHalfTheta, 0));
        Complex eiPhiPlusLambda = Complex.exponential(phi + lambda);
        unitaryGate[1][1] = eiPhiPlusLambda.multiply(new Complex(cosHalfTheta, 0));
        return new Matrix(unitaryGate);
    }

    public double getTheta() {return theta;}

    public double getPhi() {return phi;}

    public double getLambda() {return lambda;}

    public void setTheta(double theta) {
        this.theta = theta;
        buildUnitary(theta, phi, lambda);
    }

    public void setPhi(double phi) {
        this.phi = phi;
        buildUnitary(theta, phi, lambda);
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
        buildUnitary(theta, phi, lambda);
    }


}