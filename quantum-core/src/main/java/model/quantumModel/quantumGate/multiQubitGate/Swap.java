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

package model.quantumModel.quantumGate.multiQubitGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

public class Swap extends QuantumGate {
    public Swap(int[] targetQubits) {
        super(buildMatrix(), 2, "Swap");
    }

    private static Matrix buildMatrix() {
        Complex[][] swap = new Complex[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                swap[i][j] = Complex.ZERO;
            }
        }
        swap[0][0] = Complex.ONE;
        swap[1][2] = Complex.ONE;
        swap[2][1] = Complex.ONE;
        swap[3][3] = Complex.ONE;
        return new Matrix(swap);
    }
}