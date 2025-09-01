// Copyright 2025 D4rk-h
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package model.quantumModel.quantumState.quantumStateUtils;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;
import model.quantumModel.quantumState.QuantumState;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class QuantumStateUtils {
    private static final Random random = new Random();
    private static final Map<String, Double[]> probabilityCache = new ConcurrentHashMap<>();
    private static final Map<String, Double> normCache = new ConcurrentHashMap<>();

    private QuantumStateUtils() {}

    public static int validatePositive(int value, String name) {
        if (value <= 0) {throw new IllegalArgumentException(name + " must be positive, got: " + value);}
        return value;
    }

    public static void validateQubitIndex(int qubitIndex, int numQubits) {
        if (qubitIndex < 0 || qubitIndex >= numQubits) {throw new IndexOutOfBoundsException("Qubit index " + qubitIndex + " out of bounds for " + numQubits + " qubits");}
    }

    public static void validateStateIndex(int state, int maxStates) {
        if (state < 0 || state >= maxStates) {throw new IndexOutOfBoundsException("State index " + state + " out of bounds for " + maxStates + " states");}
    }

    public static boolean isNormalized(Complex[] amplitudes) {
        double sum = Arrays.stream(amplitudes).mapToDouble(Complex::magnitudeSquared).sum();
        return Math.abs(sum - 1.0) < Complex.EPSILON;
    }

    public static void validateNormalization(Complex[] amplitudes) {
        if (!isNormalized(amplitudes)) {throw new IllegalArgumentException("State is not normalized");}
    }

    public static Matrix findDensityMatrix(Complex[] amplitudes) {
        int dimension = amplitudes.length;
        Matrix densityData = new Matrix(dimension, dimension);
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                densityData.set(i, j, amplitudes[i].multiply(amplitudes[j].conjugate()));
            }
        }
        return densityData;
    }


    public static Complex[] createZeroState(int numQubits) {
        int size = 1 << numQubits;
        Complex[] amplitudes = new Complex[size];
        amplitudes[0] = Complex.ONE;
        for (int i = 1; i < size; i++) {
            amplitudes[i] = Complex.ZERO;
        }
        return amplitudes;
    }

    public static Complex[] createOneState(int numQubits) {
        int size = 1 << numQubits;
        Complex[] amplitudes = new Complex[size];
        Arrays.fill(amplitudes, Complex.ZERO);
        amplitudes[size - 1] = Complex.ONE;
        return amplitudes;
    }

    public static Complex[] createUniformSuperposition(int numQubits) {
        validatePositive(numQubits, "Number of qubits");
        int numStates = 1 << numQubits;
        Complex[] amplitudes = new Complex[numStates];
        Complex amplitude = new Complex(1.0 / Math.sqrt(numStates), 0.0);
        Arrays.fill(amplitudes, amplitude);
        return amplitudes;
    }

    public static Complex[] createCustomSuperposition(int numQubits, int[] states, Complex[] amplitudeValues) {
        if (states.length != amplitudeValues.length) throw new IllegalArgumentException("States and amplitude arrays must have equal length");
        int numStates = 1 << numQubits;
        Complex[] amplitudes = new Complex[numStates];
        Arrays.fill(amplitudes, Complex.ZERO);
        for (int i = 0; i < states.length; i++) {
            validateStateIndex(states[i], numStates);
            amplitudes[states[i]] = amplitudeValues[i];
        }
        return amplitudes;
    }

    public static Complex[] normalize(Complex[] amplitudes) {
        double norm = calculateNorm(amplitudes);
        if (norm < Complex.EPSILON) throw new IllegalStateException("Cannot normalize zero state");
        Complex normalizationFactor = new Complex(1.0 / norm, 0.0);
        return Arrays.stream(amplitudes).map(amp -> amp.multiply(normalizationFactor)).toArray(Complex[]::new);
    }

    private static double calculateNorm(Complex[] amplitudes) {
        String key = Arrays.hashCode(amplitudes) + "_norm";
        return normCache.computeIfAbsent(key, k -> {
            double sum = Arrays.stream(amplitudes).mapToDouble(Complex::magnitudeSquared).sum();
            return Math.sqrt(sum);
        });
    }

    public static Complex[] applyGate(QuantumState state, QuantumGate gate) {
        if (gate.getNumQubits() != state.getNumQubits()) throw new IllegalArgumentException("State and gate number of qubits must be equal");
        return gate.getMatrix().multiplyVector(state.getAmplitudes());
    }

    public static Complex[] applyBitFlip(Complex[] amplitudes, int targetQubit, int numQubits) {
        validateQubitIndex(targetQubit, numQubits);
        Complex[] newAmplitudes = new Complex[amplitudes.length];
        for (int i = 0; i < amplitudes.length; i++) {
            int flippedState = i ^ (1 << targetQubit);
            newAmplitudes[flippedState] = amplitudes[i];
        }
        return newAmplitudes;
    }

    public static Complex[] applyPhaseFlip(Complex[] amplitudes, int targetQubit, int numQubits) {
        validateQubitIndex(targetQubit, numQubits);
        Complex[] newAmplitudes = new Complex[amplitudes.length];
        for (int i = 0; i < amplitudes.length; i++) {
            boolean targetQubitIsOne = (i & (1 << targetQubit)) != 0;
            newAmplitudes[i] = targetQubitIsOne ? amplitudes[i].multiply(new Complex(-1.0, 0.0)) : amplitudes[i];
        }
        return newAmplitudes;
    }

    public static MeasurementResult measure(QuantumState state) {
        validateNormalization(state.getAmplitudes());
        Complex[] amplitudes = state.getAmplitudes();
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < amplitudes.length; i++) {
            double probability = amplitudes[i].magnitudeSquared();
            cumulativeProbability += probability;
            if (randomValue < cumulativeProbability) {
                return new MeasurementResult(i, createCollapsedState(i, state.getNumQubits()), probability);
            }
        }
        int lastState = amplitudes.length - 1;
        return new MeasurementResult(lastState,
                createCollapsedState(lastState, state.getNumQubits()),
                amplitudes[lastState].magnitudeSquared());
    }

    public static MeasurementResult measureQubit(QuantumState state, int qubitIndex) {
        validateQubitIndex(qubitIndex, state.getNumQubits());
        Complex[] amplitudes = state.getAmplitudes();
        double prob0 = 0.0, prob1 = 0.0;
        for (int i = 0; i < amplitudes.length; i++) {
            double prob = amplitudes[i].magnitudeSquared();
            if ((i & (1 << qubitIndex)) == 0) {
                prob0 += prob;
            } else {
                prob1 += prob;
            }
        }
        int result = random.nextDouble() < prob0 ? 0 : 1;
        QuantumState collapsedState = collapseQubit(state, qubitIndex, result);
        return new MeasurementResult(result, collapsedState, result == 0 ? prob0 : prob1);
    }

    public static Map<Integer, Integer> measureMultiple(QuantumState state, int numMeasurements) {
        validatePositive(numMeasurements, "Number of measurements");
        Map<Integer, Integer> results = new HashMap<>();
        for (int i = 0; i < numMeasurements; i++) {
            int result = measure(state).outcome();
            results.merge(result, 1, Integer::sum);
        }
        return results;
    }

    private static QuantumState createCollapsedState(int measuredState, int numQubits) {
        int size = 1 << numQubits;
        Complex[] collapsedAmplitudes = new Complex[size];
        Arrays.fill(collapsedAmplitudes, Complex.ZERO);
        collapsedAmplitudes[measuredState] = Complex.ONE;
        return new QuantumState(collapsedAmplitudes, numQubits);
    }

    private static QuantumState collapseQubit(QuantumState state, int qubitIndex, int result) {
        Complex[] amplitudes = state.getAmplitudes();
        Complex[] newAmplitudes = new Complex[amplitudes.length];
        double normalizationFactor = 0.0;
        for (int i = 0; i < amplitudes.length; i++) {
            if (((i >> qubitIndex) & 1) == result) {
                normalizationFactor += amplitudes[i].magnitudeSquared();
            }
        }
        normalizationFactor = Math.sqrt(normalizationFactor);
        Complex factor = new Complex(1.0 / normalizationFactor, 0.0);
        for (int i = 0; i < amplitudes.length; i++) {
            if (((i >> qubitIndex) & 1) == result) {
                newAmplitudes[i] = amplitudes[i].multiply(factor);
            } else {
                newAmplitudes[i] = Complex.ZERO;
            }
        }
        return new QuantumState(newAmplitudes, state.getNumQubits());
    }

    public static double[] getProbabilities(Complex[] amplitudes) {
        String key = Arrays.hashCode(amplitudes) + "_probs";
        Double[] cached = probabilityCache.get(key);
        if (cached == null) {
            cached = Arrays.stream(amplitudes)
                    .map(Complex::magnitudeSquared)
                    .toArray(Double[]::new);
            probabilityCache.put(key, cached);
        }
        return Arrays.stream(cached).mapToDouble(Double::doubleValue).toArray();
    }

    public static double fidelity(QuantumState state1, QuantumState state2) {
        if (state1.getNumQubits() != state2.getNumQubits()) {throw new IllegalArgumentException("States must have same number of qubits");}
        Complex[] amps1 = state1.getAmplitudes();
        Complex[] amps2 = state2.getAmplitudes();
        Complex innerProduct = Complex.ZERO;
        for (int i = 0; i < amps1.length; i++) {
            innerProduct = innerProduct.add(amps1[i].conjugate().multiply(amps2[i]));
        }
        return innerProduct.magnitudeSquared();
    }

    public static double vonNeumannEntropy(Complex[] amplitudes) {
        double[] probabilities = getProbabilities(amplitudes);
        double entropy = 0.0;
        for (double prob : probabilities) {
            if (prob > Complex.EPSILON) {
                entropy -= prob * Math.log(prob);
            }
        }
        return entropy;
    }

    public static QuantumState tensorProduct(QuantumState first, QuantumState second) {
        int newNumQubits = first.getNumQubits() + second.getNumQubits();
        int newSize = 1 << newNumQubits;
        Complex[] newAmplitudes = new Complex[newSize];
        int index = 0;
        for (int i = 0; i < first.getAmplitudes().length; i++) {
            for (int j = 0; j < second.getAmplitudes().length; j++) {
                newAmplitudes[index++] = first.getAmplitudes()[i].multiply(second.getAmplitudes()[j]);
            }
        }
        return new QuantumState(newAmplitudes, newNumQubits);
    }

    public static Complex alpha(QuantumState state) {
        if (state.getNumQubits() != 1) throw new IllegalStateException("getAlpha is only available for single qubit states");
        return state.getAmplitudes()[0];
    }

    public static Complex beta(QuantumState state) {
        if (state.getNumQubits() != 1) throw new IllegalStateException("getBeta is only available for single qubit states");
        return state.getAmplitudes()[1];
    }

    public static String toString(Complex[] amplitudes, int numQubits) {
        String result = IntStream.range(0, amplitudes.length)
                .filter(i -> amplitudes[i].magnitude() > Complex.EPSILON)
                .mapToObj(i -> formatStateComponent(amplitudes[i], i, numQubits))
                .collect(Collectors.joining(" + "));
        return result.isEmpty() ? "0" : result;
    }

    private static String formatStateComponent(Complex amplitude, int state, int numQubits) {
        String binaryState = String.format("%" + numQubits + "s", Integer.toBinaryString(state))
                .replace(' ', '0');
        return String.format("%s|%s⟩", amplitude.toString(), binaryState);
    }

    public static void clearCache() {
        probabilityCache.clear();
        normCache.clear();
    }

    public static int getCacheSize() {
        return probabilityCache.size() + normCache.size();
    }
}