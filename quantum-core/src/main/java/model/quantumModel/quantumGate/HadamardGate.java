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

public class HadamardGate extends QuantumGate {
    public HadamardGate() {
        super(buildHadamard(), 1, "Hadamard");
    }

    private static Matrix buildHadamard(){
        Complex[][] hadamard = new Complex[2][2];
        double value = 1 / Math.sqrt(2);
        hadamard[0][0] = Complex.ONE.scale(value);
        hadamard[0][1] = Complex.ONE.scale(value);
        hadamard[1][0] = Complex.ONE.scale(value);
        hadamard[1][1] = Complex.ONE.scale(-value);
        return new Matrix(hadamard);
    }
}
