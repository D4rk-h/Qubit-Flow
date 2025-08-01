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

package model.quantumModel.quantumCircuit.quantumCircuitUtils.cliVisualization;

import model.mathModel.Complex;
import model.quantumModel.QuantumGate;
import model.quantumModel.QuantumState;

public class QuantumCircuitCLIDisplayUtils {
    private static boolean isApproximatelyEqual(Complex a, Complex b) {
        final double EPSILON = 1e-10;
        return Math.abs(a.getRealPart() - b.getRealPart()   ) < EPSILON &&
                Math.abs(a.getImaginaryPart() - b.getImaginaryPart()) < EPSILON;
    }

    public String detectSingleQubitState(QuantumState state) {
        Complex alpha = state.getAlpha();
        Complex beta = state.getBeta();
        if (isApproximatelyEqual(alpha, new Complex(1.0, 0.0)) &&
                isApproximatelyEqual(beta, new Complex(0.0, 0.0))) {
            return "|0⟩";
        }
        if (isApproximatelyEqual(alpha, new Complex(0.0, 0.0)) &&
                isApproximatelyEqual(beta, new Complex(1.0, 0.0))) {
            return "|1⟩";
        }
        if (isApproximatelyEqual(alpha, new Complex(1.0/Math.sqrt(2), 0.0)) &&
                isApproximatelyEqual(beta, new Complex(1.0/Math.sqrt(2), 0.0))) {
            return "|+⟩";
        }
        if (isApproximatelyEqual(alpha, new Complex(1.0/Math.sqrt(2), 0.0)) &&
                isApproximatelyEqual(beta, new Complex(-1.0/Math.sqrt(2), 0.0))) {
            return "|-⟩";
        }
        if (isApproximatelyEqual(alpha, new Complex(1.0/Math.sqrt(2), 0.0)) &&
                isApproximatelyEqual(beta, new Complex(0.0, 1.0/Math.sqrt(2)))) {
            return "|i⟩";
        }
        if (isApproximatelyEqual(alpha, new Complex(1.0/Math.sqrt(2), 0.0)) &&
                isApproximatelyEqual(beta, new Complex(0.0, -1.0/Math.sqrt(2)))) {
            return "|-i⟩";
        }
        return "|ψ⟩";
    }

    public String formatGate(QuantumGate  gate) {
        String gateName = gate.getName();
        switch (gateName.toUpperCase()) {
            case "HADAMARD":
            case "H":
                return "[H]";
            case "PAULI_X":
            case "X":
                return "[X]";
            case "PAULI_Y":
            case "Y":
                return "[Y]";
            case "PAULI_Z":
            case "Z":
                return "[Z]";
            case "CNOT":
                return "[⊕]";
            case "PHASE":
            case "S":
                return "[S]";
            case "T":
                return "[T]";
            default:
                return truncateGateName(gateName);
        }
    }

    private static String truncateGateName(String gateName) {
        if (gateName.length() <= 3) {return "[" + gateName + "]";}
        return "[" + gateName.substring(0, 1) + "]";
    }
}
