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
import model.quantumModel.quantumState.QuantumState;
import java.time.Instant;
import java.util.Objects;

public record MeasurementResult(
        int outcome,
        QuantumState collapsedState,
        double probability,
        Instant timestamp
) {
    public MeasurementResult {
        Objects.requireNonNull(collapsedState, "Collapsed state cannot be null");
        if (probability < 0.0 || probability > 1.0) {throw new IllegalArgumentException("Probability must be between 0 and 1; got: " + probability);}
        if (timestamp == null) {timestamp = Instant.now();}
    }

    public MeasurementResult(int outcome, QuantumState collapsedState, double probability) {
        this(outcome, collapsedState, probability, Instant.now());
    }

    public boolean isDefiniteOutcome() {return Math.abs(probability - 1.0) < Complex.EPSILON;}

    public boolean isUnlikelyOutcome() {return probability < 0.1;}

    public String getBinaryRepresentation() {
        int numQubits = collapsedState.getNumQubits();
        return String.format("%" + numQubits + "s", Integer.toBinaryString(outcome)).replace(' ', '0');
    }

    public double getInformationContent() {return probability > 0 ? -Math.log(probability) : Double.POSITIVE_INFINITY;}

    @Override
    public String toString() {
        return String.format("MeasurementResult{outcome=%s, probability=%.4f, timestamp=%s}",
                getBinaryRepresentation(), probability, timestamp);
    }
}