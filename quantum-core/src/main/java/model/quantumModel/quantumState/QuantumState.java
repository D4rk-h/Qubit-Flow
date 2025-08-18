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

    public void applyGate(QuantumGate gate) {
        Complex[] newAmplitudes = QuantumStateUtils.applyGate(this, gate);
        System.arraycopy(newAmplitudes, 0, this.amplitudes, 0, newAmplitudes.length);
    }

    public void applyHadamard(int targetQubit) {
        QuantumStateUtils.validateQubitIndex(targetQubit, this.numQubits);
        int mask = 1 << targetQubit;
        double factor = 1.0 / Math.sqrt(2.0);
        for (int i = 0; i < amplitudes.length; i += 2 * mask) {
            for (int j = 0; j < mask; j++) {
                Complex a0 = amplitudes[i + j];
                Complex a1 = amplitudes[i + j + mask];
                amplitudes[i + j] = a0.add(a1).scale(factor);
                amplitudes[i + j + mask] = a0.subtract(a1).scale(factor);
            }
        }
    }

    public void applyNot(int targetQubit) {
        QuantumStateUtils.validateQubitIndex(targetQubit, this.numQubits);
        int mask = 1 << targetQubit;
        for (int i = 0; i < amplitudes.length; i += 2 * mask) {
            for (int j = 0; j < mask; j++) {
                Complex temp = amplitudes[i + j];
                amplitudes[i + j] = amplitudes[i + j + mask];
                amplitudes[i + j + mask] = temp;
            }
        }
    }

    public void applyY(int targetQubit) {
        QuantumStateUtils.validateQubitIndex(targetQubit, this.numQubits);
        int mask = 1 << targetQubit;
        for (int i = 0; i < amplitudes.length; i += 2 * mask) {
            for (int j = 0; j < mask; j++) {
                Complex a0 = amplitudes[i + j];
                Complex a1 = amplitudes[i + j + mask];
                amplitudes[i + j] = a1.multiply(Complex.I);
                amplitudes[i + j + mask] = a0.multiply(Complex.MINUS_I);
            }
        }
    }

    public void applyZ(int targetQubit) {
        QuantumStateUtils.validateQubitIndex(targetQubit, this.numQubits);
        int mask = 1 << targetQubit;
        for (int i = 0; i < amplitudes.length; i++) {
            if ((i & mask) != 0) amplitudes[i] = amplitudes[i].scale(-1.0);
        }
    }

    public void applyT(int targetQubit) {
        QuantumStateUtils.validateQubitIndex(targetQubit, this.numQubits);
        int mask = 1 << targetQubit;
        Complex tPhase = Complex.exponential(Math.PI / 4);
        for (int i = 0; i < amplitudes.length; i++) {
            if ((i & mask) != 0) amplitudes[i] = amplitudes[i].multiply(tPhase);
        }
    }

    public void applyPhase(int targetQubit) {
        QuantumStateUtils.validateQubitIndex(targetQubit, this.numQubits);
        int mask = 1 << targetQubit;
        for (int i = 0; i < amplitudes.length; i++) {
            if ((i & mask) != 0) amplitudes[i] = amplitudes[i].multiply(Complex.I);
        }
    }

    public void applySwap(int qubit1, int qubit2) {
        QuantumStateUtils.validateQubitIndex(qubit1, this.numQubits);
        QuantumStateUtils.validateQubitIndex(qubit2, this.numQubits);
        if (qubit1 == qubit2) throw new IllegalArgumentException("Cannot swap a qubit with itself");
        int mask1 = 1 << qubit1;
        int mask2 = 1 << qubit2;
        for (int i = 0; i < amplitudes.length; i++) {
            boolean bit1 = (i & mask1) != 0;
            boolean bit2 = (i & mask2) != 0;
            if (bit1 != bit2) {
                int swappedState = i ^ mask1 ^ mask2;
                if (i < swappedState) {
                    Complex temp = amplitudes[i];
                    amplitudes[i] = amplitudes[swappedState];
                    amplitudes[swappedState] = temp;
                }
            }
        }
    }

    public void applyCNOT(int controlQubit, int targetQubit) {
        QuantumStateUtils.validateQubitIndex(controlQubit, this.numQubits);
        QuantumStateUtils.validateQubitIndex(targetQubit, this.numQubits);
        if (controlQubit == targetQubit) throw new IllegalArgumentException("Control and target qubits must be different");
        int controlMask = 1 << controlQubit;
        int targetMask = 1 << targetQubit;
        for (int i = 0; i < amplitudes.length; i++) {
            if ((i & controlMask) != 0) {
                int flippedState = i ^ targetMask;
                if (i < flippedState) {
                    Complex temp = amplitudes[i];
                    amplitudes[i] = amplitudes[flippedState];
                    amplitudes[flippedState] = temp;
                }
            }
        }
    }

    public void applyToffoli(int control1, int control2, int target) {
        QuantumStateUtils.validateQubitIndex(control1, this.numQubits);
        QuantumStateUtils.validateQubitIndex(control2, this.numQubits);
        QuantumStateUtils.validateQubitIndex(target, this.numQubits);
        if (control1 == control2 || control1 == target || control2 == target) throw new IllegalArgumentException("All qubits must be different for Toffoli gate");
        int control1Mask = 1 << control1;
        int control2Mask = 1 << control2;
        int targetMask = 1 << target;
        for (int i = 0; i < amplitudes.length; i++) {
            if ((i & control1Mask) != 0 && (i & control2Mask) != 0) {
                int flippedState = i ^ targetMask;
                if (i < flippedState) {
                    Complex temp = amplitudes[i];
                    amplitudes[i] = amplitudes[flippedState];
                    amplitudes[flippedState] = temp;
                }
            }
        }
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