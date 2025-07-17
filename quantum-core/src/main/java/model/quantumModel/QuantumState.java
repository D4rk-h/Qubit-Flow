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
    private static final Random random = new Random();

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

    public QuantumState(Complex[] amplitudes, int numQubits) {
        this.numQubits = numQubits;
        int expectedSize = (int) Math.pow(2, numQubits);
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

    public static QuantumState createUniformSuperposition(int numQubits) {
        if (numQubits <= 0) {
            throw new IllegalArgumentException("Number of qubits cannot be negative");
        }
        int numStates = (int) Math.pow(2, numQubits);
        Complex[] amplitudes = new Complex[numStates];
        double amplitude = 1.0/Math.sqrt(numStates);
        for (int i=0;i<numStates;i++) {
            amplitudes[i] = new Complex(amplitude, 0.0);
        }
        return new QuantumState(amplitudes, numQubits);
    }

    public static QuantumState createCustomSuperposition(int numQubits, int[] states, Complex[] amplitudes) {
        if (states.length != amplitudes.length) {
            throw new IllegalArgumentException("States and amplitude length must be equal");
        }
        int numStates = (int) Math.pow(2, numQubits);
        Complex[] stateAmplitudes = new Complex[numStates];
        for (int i = 0; i < numStates; i++) {
            stateAmplitudes[i] = new Complex(0.0, 0.0);
        }
        for (int i = 0; i < states.length; i++) {
            if (states[i] < 0 || states[i] >= numStates) {
                throw new IllegalArgumentException("Invalid state index: " + states[i]);
            }
            stateAmplitudes[states[i]] = amplitudes[i];
        }
        return new QuantumState(stateAmplitudes, numQubits);
    }

    private QuantumState applyBitFlip(int targetQubit) {
        Complex[] newAmplitudes = new Complex[amplitudes.length];
        for (int i = 0; i < amplitudes.length; i++) {
            int flippedState = i ^ (1 << targetQubit);
            newAmplitudes[flippedState] = amplitudes[i];
        }
        return new QuantumState(newAmplitudes, this.numQubits);
    }

    private QuantumState applyPhaseFlip(int targetQubit) {
        Complex[] newAmplitudes = new Complex[amplitudes.length];
        for (int i = 0; i < amplitudes.length; i++) {
            boolean targetQubitIsOne = (i & (1 << targetQubit)) != 0;
            if (targetQubitIsOne) {
                newAmplitudes[i] = amplitudes[i].multiply(new Complex(-1.0, 0.0));
            } else {
                newAmplitudes[i] = amplitudes[i];
            }
        }
        return new QuantumState(newAmplitudes, this.numQubits);
    }

    public QuantumState applyDepolarizingNoise(double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("Noise probability must be between 0 and 1");
        }
        if (random.nextDouble() < probability) {
            return createUniformSuperposition(this.numQubits);
        }
        return new QuantumState(this.amplitudes.clone(), this.numQubits);
    }

    public QuantumState applyAmplitudeDamping(double gamma) {
        if (gamma < 0.0 || gamma > 1.0) {
            throw new IllegalArgumentException("Damping parameter must be between 0 and 1");
        }
        Complex[] newAmplitudes = new Complex[amplitudes.length];
        for (int i = 0; i < amplitudes.length; i++) {
            int excitedStates = Integer.bitCount(i);
            double dampingFactor = Math.pow(1.0 - gamma, excitedStates);
            newAmplitudes[i] = amplitudes[i].multiply(new Complex(Math.sqrt(dampingFactor), 0.0));
        }
        return normalizeState(newAmplitudes);
    }

    public QuantumState applyPhaseFlipNoise(double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("Noise probability must be between 0 and 1");
        }
        QuantumState noisyState = new QuantumState(this.amplitudes.clone(), this.numQubits);
        for (int qubit = 0; qubit < numQubits; qubit++) {
            if (random.nextDouble() < probability) {
                noisyState = noisyState.applyPhaseFlip(qubit);
            }
        }
        return noisyState;
    }

    public QuantumState applyBitFlipNoise(double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("Noise probability must be between 0 and 1");
        }
        QuantumState noisyState = new QuantumState(this.amplitudes.clone(), this.numQubits);
        for (int qubit = 0; qubit < numQubits; qubit++) {
            if (random.nextDouble() < probability) {
                noisyState = noisyState.applyBitFlip(qubit);
            }
        }
        return noisyState;
    }

    public QuantumState applyPhaseDamping(double gamma) {
        if (gamma < 0.0 || gamma > 1.0) {
            throw new IllegalArgumentException("Dephasing parameter must be between 0 and 1");
        }
        Complex[] newAmplitudes = new Complex[amplitudes.length];
        for (int i = 0; i < amplitudes.length; i++) {
            if (random.nextDouble() < gamma) {
                double randomPhase = random.nextDouble() * 2 * Math.PI;
                Complex phaseShift = new Complex(Math.cos(randomPhase), Math.sin(randomPhase));
                newAmplitudes[i] = amplitudes[i].multiply(phaseShift);
            } else {
                newAmplitudes[i] = amplitudes[i];
            }
        }
        return new QuantumState(newAmplitudes, this.numQubits);
    }

    public QuantumState applyBitPhaseFlipNoise(double bitFlipProb, double phaseFlipProb) {
        return this.applyBitFlipNoise(bitFlipProb)
                .applyPhaseFlipNoise(phaseFlipProb);
    }

    private QuantumState normalizeState(Complex[] amplitudes) {
        double norm = 0.0;
        for (Complex amp : amplitudes) {
            norm += amp.magnitudeSquared();
        }
        if (norm == 0.0) {
            throw new IllegalStateException("Cannot normalize zero state");
        }
        double normalizationFactor = 1.0 / Math.sqrt(norm);
        Complex[] normalizedAmplitudes = new Complex[amplitudes.length];
        for (int i = 0; i < amplitudes.length; i++) {
            normalizedAmplitudes[i] = amplitudes[i].multiply(new Complex(normalizationFactor, 0.0));
        }
        return new QuantumState(normalizedAmplitudes, this.numQubits);
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
