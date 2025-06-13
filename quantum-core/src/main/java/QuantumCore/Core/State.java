package QuantumCore.Core;
import MathCore.Complex;


public class State {
    private Complex[] amplitudes;
    private int nQubits;

    public State(int nQubits) {
        this.nQubits = nQubits;
        this.amplitudes = new Complex[(int) Math.pow(2, nQubits)];
        amplitudes[0] = new Complex(1.0, 0.0);
        for (int i = 1; i<amplitudes.length;i++) {
            amplitudes[i] = new Complex(0.0, 0.0);
        }
    }

    public State(Complex[] amplitudes, int nQubits) {
        this.nQubits = nQubits;
        if (amplitudes.length != Math.pow(2, nQubits)){
            throw new IllegalArgumentException("Incorrect number of amplitudes");
        }
        if (isNormalized(amplitudes)) {throw new IllegalArgumentException("Unnormalized state");}
        this.amplitudes = amplitudes;
    }

    private boolean isNormalized(Complex[] amplitudes) {
        double sum = 0.0;
        for (Complex amplitude : amplitudes) {
            sum += amplitude.magnitude() * amplitude.magnitude();
        }
        return Math.abs(sum - 1.0) > 1e-10;
    }

    public State(Complex alpha, Complex beta) {
        this(1);
        amplitudes[0] = alpha;
        amplitudes[1] = beta;
    }

    public Complex alpha() {
        return amplitudes[0];
    }
    public Complex beta() {
        return amplitudes[1];
    }

    public Complex[] getAmplitudes() {
        return amplitudes;
    }

    public void setAmplitudes(Complex[] amplitudes) {
        this.amplitudes = amplitudes;
    }

    public int getNQubits() {
        return nQubits;
    }

    public void setNQubits(int nQubits) {
        this.nQubits = nQubits;
    }
}
