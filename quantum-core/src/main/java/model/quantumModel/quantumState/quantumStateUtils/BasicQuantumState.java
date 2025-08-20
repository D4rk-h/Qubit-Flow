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

public enum BasicQuantumState {
    ZERO(Complex.ONE, Complex.ZERO, "|0⟩"),
    ONE(Complex.ZERO, Complex.ONE, "|1⟩"),
    PLUS(Complex.ONE.scale(1/Math.sqrt(2)), Complex.ONE.scale(1/Math.sqrt(2)), "|+⟩"),
    MINUS(Complex.ONE.scale(1/Math.sqrt(2)), new Complex(-1/Math.sqrt(2), 0), "|-⟩"),
    PLUS_I(Complex.ONE.scale(1/Math.sqrt(2)), Complex.I.scale(1/Math.sqrt(2)), "|+i⟩"),
    MINUS_I(Complex.ONE.scale(1/Math.sqrt(2)), Complex.MINUS_I.scale(1/Math.sqrt(2)), "|-i⟩");

    private final Complex alpha;
    private final Complex beta;
    private final String symbol;

    BasicQuantumState(Complex alpha, Complex beta, String symbol) {
        this.alpha = alpha;
        this.beta = beta;
        this.symbol = symbol;
        double normalized = alpha.magnitudeSquared() + beta.magnitudeSquared();
        if (Math.abs(normalized - 1.0) > Complex.EPSILON) {throw new IllegalArgumentException("State " + symbol + " isn't normalized -> norm = " + normalized);}
    }

    public QuantumState toQuantumState() {
        return new QuantumState(alpha, beta);
    }

    public String getSymbol() {return symbol;}

    public Complex getAlpha() {return alpha;}

    public Complex getBeta() {return beta;}

    public boolean isComputationalBasis() {return this == ZERO || this == ONE;}

    public boolean isSuperposition() {return this == PLUS || this == MINUS;}

    public boolean isCircularBasis() {return this == PLUS_I || this == MINUS_I;}

    public double[] getProbabilities() {return new double[]{alpha.magnitudeSquared(), beta.magnitudeSquared()};}

    public BasicQuantumState getOrthogonal() {
        return switch (this) {
            case ZERO -> ONE;
            case ONE -> ZERO;
            case PLUS -> MINUS;
            case MINUS -> PLUS;
            case PLUS_I -> MINUS_I;
            case MINUS_I -> PLUS_I;
        };
    }

    public static BasicQuantumState fromBlochAngles(double theta, double phi) {
        Complex alpha = new Complex(Math.cos(theta / 2), 0);
        Complex beta = Complex.exponential(phi).scale(Math.sin(theta / 2));
        BasicQuantumState closest = ZERO;
        double minDistance = Double.MAX_VALUE;
        for (BasicQuantumState state : values()) {
            double distance = alpha.subtract(state.alpha).magnitudeSquared() +
                    beta.subtract(state.beta).magnitudeSquared();
            if (distance < minDistance) {
                minDistance = distance;
                closest = state;
            }
        }
        return closest;
    }

    public static BasicQuantumState fromQuantumState(QuantumState state) {
        if (state.getNumQubits() != 1) {throw new IllegalArgumentException("Can only convert single-qubit states");}
        for (BasicQuantumState basicState : values()) {
            if (state.getAlpha().isApproximatelyEqual(basicState.alpha) &&
                    state.getAlpha().isApproximatelyEqual(basicState.beta)) {
                return basicState;
            }
        }
        throw new IllegalArgumentException("State does not match any basic quantum state");
    }
}
