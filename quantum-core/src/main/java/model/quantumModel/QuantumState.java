package model.quantumModel;
import model.mathModel.Complex;

import java.util.HashMap;
import java.util.Map;
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
        validateStateNormalization();
        Random random = new Random();
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i=0;i<amplitudes.length;i++){
            double probability = amplitudes[i].magnitudeSquared();
            cumulativeProbability += probability;
            if (randomValue < cumulativeProbability) {
                return i;
            }
        }
        return amplitudes.length - 1;
    }

    public int measureAndCollapse() {
        int result = measure();
        collapse(result);
        return result;
    }

    private void collapse(int measuredState) {
        if (measuredState < 0 || measuredState >= amplitudes.length) {
            throw new IllegalArgumentException("Invalid measured state: " + measuredState);
        }
        for (int i = 0; i < amplitudes.length; i++) {
            amplitudes[i] = (i == measuredState) ?
                    new Complex(1.0, 0.0) :
                    new Complex(0.0, 0.0);
        }
    }

    private void collapseOptimized(int measuredState) {
        if (measuredState < 0 || measuredState >= amplitudes.length) {
            throw new IllegalArgumentException("Invalid measured state: " + measuredState);
        }
        Complex one = new Complex(1.0, 0.0);
        Complex zero = new Complex(0.0, 0.0);
        for (int i = 0; i < amplitudes.length; i++) {
            amplitudes[i] = (i == measuredState) ? one : zero;
        }
    }

    public double[] getProbabilities() {
        double[] probabilities = new double[amplitudes.length];
        for (int i = 0; i < amplitudes.length; i++) {
            probabilities[i] = amplitudes[i].magnitudeSquared();
        }
        return probabilities;
    }

    private void validateStateNormalization() {
        if (!utils.isNormalized(amplitudes, this)) {
            throw new IllegalStateException("Cannot measure non-normalized quantum state");
        }
    }

    public Map<Integer, Integer> measureMultiple(int numMeasurements) {
        Map<Integer, Integer> results = new HashMap<>();
        QuantumState originalState = new QuantumState(this.amplitudes.clone(), this.numQubits);
        for (int i = 0; i < numMeasurements; i++) {
            this.amplitudes = originalState.amplitudes.clone();
            int result = measureAndCollapse();
            results.put(result, results.getOrDefault(result, 0) + 1);
        }
        return results;
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
