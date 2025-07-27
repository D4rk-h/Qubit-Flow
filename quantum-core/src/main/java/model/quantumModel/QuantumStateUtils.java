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

package model.quantumModel;
import model.mathModel.Complex;

public class QuantumStateUtils {
    private static final double EPSILON = 1e-10;

    public boolean isNormalized(Complex[] amplitudes, QuantumState quantumState) {
        double sum = 0.0;
        for (Complex amplitude : amplitudes) {
            sum += amplitude.magnitude() * amplitude.magnitude();
        }
        return Math.abs(sum - 1.0) < EPSILON;
    }

    public QuantumState tensorProduct(QuantumState first, QuantumState second) {
        int newNumQubits = first.getNumQubits() + second.getNumQubits();
        int newSize = (int) Math.pow(2, newNumQubits);
        Complex[] newAmplitudes = new Complex[newSize];
        int index = 0;
        for (int i = 0; i < first.getAmplitudes().length; i++) {
            for (int j = 0; j < second.getAmplitudes().length; j++) {
                newAmplitudes[index] = first.getAmplitudes()[i].multiply(second.getAmplitudes()[j]);
                index ++;
            }
        }
        return new QuantumState(newAmplitudes, newNumQubits);
    }

    public void validateNormalization(Complex[] amplitudes,  QuantumState quantumState) {
        if (!isNormalized(amplitudes, quantumState)) {
            throw new IllegalArgumentException("State is not normalized");
        }
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

    private void collapseOptimized(int measuredState, Complex[] amplitudes) {
        if (measuredState < 0 || measuredState >= amplitudes.length) {
            throw new IllegalArgumentException("Invalid measured state: " + measuredState);
        }
        Complex one = new Complex(1.0, 0.0);
        Complex zero = new Complex(0.0, 0.0);
        for (int i = 0; i < amplitudes.length; i++) {
            amplitudes[i] = (i == measuredState) ? one : zero;
        }
    }
}
