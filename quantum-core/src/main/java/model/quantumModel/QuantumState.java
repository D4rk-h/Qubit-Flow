package model.quantumModel;
import model.mathModel.Complex;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuantumState {
    private Complex[] amplitudes;
    private int numQubits;
    private final static QuantumStateUtils utils = new QuantumStateUtils();
    private static final double EPSILON = 1e-10;

    public QuantumState(int numQubits) {
        if (numQubits <= 0) {throw new IllegalArgumentException("Number of qubits must be positive");}
        this.numQubits = numQubits;
        int nAmplitudes = (int) Math.pow(2, numQubits);
        this.amplitudes = new Complex[nAmplitudes];
        amplitudes[0] = new Complex(1.0, 0.0);
        for (int i = 1; i<nAmplitudes;i++) {
            amplitudes[i] = new Complex(0.0, 0.0);
        }
    }

    public QuantumState(Complex[] amplitudes, int NumQubits) {
        this.numQubits = NumQubits;
        int expectedSize = (int) Math.pow(2, NumQubits);
        if (amplitudes.length != expectedSize){
            throw new IllegalArgumentException("Incorrect number of amplitudes");
        }
        this.amplitudes = amplitudes.clone();
        utils.validateNormalization(amplitudes, this);
    }

    public QuantumState(Complex alpha, Complex beta) {
        this.numQubits = 1;
        this.amplitudes = new Complex[]{alpha, beta};
    }

    public QuantumState applyGate(QuantumGate gate) {
        if (gate.getNumQubits() != this.numQubits){
            throw new IllegalArgumentException("Gate requires " + gate.getNumQubits() + "qubits");
        }
        Complex[] newAmplitudes = gate.getMatrix().multiplyVector(this.amplitudes);
        return new QuantumState(newAmplitudes, this.numQubits);
    }

    private int measure() {
        Random random = new Random();
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i=0;i<amplitudes.length;i++){
            double probability = amplitudes[i].magnitude() * amplitudes[i].magnitude();
            cumulativeProbability += probability;
            if (randomValue < cumulativeProbability) {
                collapse(i);
                return i;
            }
        }
        collapse(amplitudes.length - 1);
        return amplitudes.length - 1;
    }

    private void collapse(int measuredState) {
        for (int i=0;i<amplitudes.length;i++){
            if (i == measuredState) {
                amplitudes[i] = new Complex(1.0, 0.0);
            } else {
                amplitudes[i] = new Complex(0.0, 0.0);
            }
        }
    }

    public Complex getAlpha() {
        if (numQubits != 1){throw new IllegalStateException("getAlpha is only available for single qubit");}
        return amplitudes[0];
    }
    public Complex getBeta() {
        if (numQubits != 1){throw new IllegalStateException("getAlpha is only available for single qubit");}
        return amplitudes[1];
    }
    public Complex[] getAmplitudes() {return amplitudes.clone();}
    public int getNumQubits() {return numQubits;}

    @Override
    public String toString() {
        String result = IntStream.range(0, amplitudes.length)
                .filter(i -> amplitudes[i].magnitude() > EPSILON)
                .mapToObj(i -> amplitudes[i].toString() + "|" + Integer.toBinaryString(i) + "⟩")
                .collect(Collectors.joining(" + "));
        return result.isEmpty() ? "0" : result;
    }
}
