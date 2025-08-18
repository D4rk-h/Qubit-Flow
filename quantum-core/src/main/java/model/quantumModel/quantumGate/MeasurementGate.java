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

package model.quantumModel.quantumGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumState.QuantumState;
import model.quantumModel.quantumState.quantumStateUtils.MeasurementResult;

public class MeasurementGate extends QuantumGate {
    private final int[] targetQubits;
    private MeasurementResult lastResult;
    private QuantumState stateBeforeMeasurement;

    public MeasurementGate(int... targetQubits) {
        super(createIdentityMatrix(targetQubits.length), targetQubits.length, "Measurement");
        this.targetQubits = targetQubits.clone();
    }

    private static Matrix createIdentityMatrix(int numQubits) {
        int size = 1 << numQubits;
        Matrix matrix = new Matrix(size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix.set(i, j, (i == j) ? Complex.ONE : Complex.ZERO);
            }
        }
        return matrix;
    }

    public MeasurementResult measure(QuantumState state) {
        stateBeforeMeasurement = state.clone();
        if (targetQubits.length == 1) {
            lastResult = state.measureQubit(targetQubits[0]);
        } else {
            lastResult = state.measure();
        }
        return lastResult;
    }

    public int[] getTargetQubits() {
        return targetQubits.clone();
    }

    public MeasurementResult getLastResult() {
        return lastResult;
    }

    public QuantumState getStateBeforeMeasurement() {
        return stateBeforeMeasurement != null ? stateBeforeMeasurement.clone() : null;
    }

    public boolean isMeasurement() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Measurement(");
        for (int i = 0; i < targetQubits.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(targetQubits[i]);
        }
        sb.append(")");
        if (lastResult != null) {
            sb.append(" -> ").append(lastResult.outcome());
        }
        return sb.toString();
    }
}
