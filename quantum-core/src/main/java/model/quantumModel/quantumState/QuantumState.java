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

package model.quantumModel.quantumState;

import model.mathModel.Complex;
import model.quantumModel.quantumGate.QuantumGate;
import model.quantumModel.quantumState.quantumStateUtils.MeasurementResult;
import model.quantumModel.quantumState.quantumStateUtils.QuantumStateUtils;

import java.util.Map;

public class QuantumState implements Cloneable {
    private final Complex[] amplitudes;
    private final int numQubits;
    private final boolean isNormalized;

    private QuantumState(Complex[] amplitudes, int numQubits, boolean skipValidation) {
        this.numQubits = numQubits;
        this.amplitudes = amplitudes.clone();
        this.isNormalized = skipValidation || QuantumStateUtils.isNormalized(amplitudes);
        if (!skipValidation && !this.isNormalized) {throw new IllegalArgumentException("State is not normalized");}
    }

    public QuantumState(int numQubits) {
        this.numQubits = QuantumStateUtils.validatePositive(numQubits, "Number of qubits");
        this.amplitudes = QuantumStateUtils.createZeroState(numQubits);
        this.isNormalized = true;
    }

    public QuantumState(Complex[] amplitudes, int numQubits) {
        this(amplitudes, numQubits, false);
    }

    public QuantumState(Complex alpha, Complex beta) {
        this(new Complex[]{alpha, beta}, 1, false);
    }

    public static QuantumState zero(int numQubits) {return new QuantumState(numQubits);}

    public static QuantumState one(int numQubits) {return new QuantumState(QuantumStateUtils.createOneState(numQubits), numQubits, true);}

    public static QuantumState uniformSuperposition(int numQubits) {
        return new QuantumState(QuantumStateUtils.createUniformSuperposition(numQubits), numQubits, true);
    }

    public static QuantumState customSuperposition(int numQubits, int[] states, Complex[] amplitudes) {
        return new QuantumState(QuantumStateUtils.createCustomSuperposition(numQubits, states, amplitudes), numQubits);
    }

    public QuantumState applyGate(QuantumGate gate) {
        return new QuantumState(QuantumStateUtils.applyGate(this, gate), numQubits, true);
    }

    public QuantumState applyBitFlip(int targetQubit) {
        return new QuantumState(QuantumStateUtils.applyBitFlip(amplitudes, targetQubit, numQubits), numQubits, true);
    }

    public QuantumState applyPhaseFlip(int targetQubit) {
        return new QuantumState(QuantumStateUtils.applyPhaseFlip(amplitudes, targetQubit, numQubits), numQubits, true);
    }

    public QuantumState normalize() {
        if (isNormalized) return this;
        return new QuantumState(QuantumStateUtils.normalize(amplitudes), numQubits, true);
    }

    public QuantumState tensorProduct(QuantumState other) {return QuantumStateUtils.tensorProduct(this, other);}

    public MeasurementResult measure() {return QuantumStateUtils.measure(this);}

    public MeasurementResult measureQubit(int qubitIndex) {return QuantumStateUtils.measureQubit(this, qubitIndex);}

    public Map<Integer, Integer> measureMultiple(int numMeasurements) {
        return QuantumStateUtils.measureMultiple(this, numMeasurements);
    }

    public double[] getProbabilities() {return QuantumStateUtils.getProbabilities(amplitudes);}

    public double getProbability(int state) {return QuantumStateUtils.getProbability(amplitudes, state);}

    public double fidelity(QuantumState other) {return QuantumStateUtils.fidelity(this, other);}

    public double vonNeumannEntropy() {return QuantumStateUtils.vonNeumannEntropy(amplitudes);}

    public Complex[] getAmplitudes() { return amplitudes.clone(); }
    public Complex getAmplitude(int state) { return QuantumStateUtils.getAmplitude(amplitudes, state); }
    public int getNumQubits() { return numQubits; }
    public int getDimension() { return amplitudes.length; }
    public boolean isNormalized() { return isNormalized; }
    public Complex getAlpha() { return QuantumStateUtils.getAlpha(this); }
    public Complex getBeta() { return QuantumStateUtils.getBeta(this); }

    @Override
    public QuantumState clone() {return new QuantumState(amplitudes, numQubits, true);}

    @Override
    public String toString() {return QuantumStateUtils.toString(amplitudes, numQubits);}
}