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

package model.quantumModel.quantumGate.ControlledGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.QuantumGate;

public class ToffoliGate extends QuantumGate {
    public ToffoliGate() {
        super(buildToffoliMatrix(), 3, "Toffoli");
    }

    public ToffoliGate(int control1, int control2, int target) {
        super(buildToffoliMatrix(), 3, "Toffoli", new int[]{control1, control2, target});
    }

    private static Matrix buildToffoliMatrix() {
        Complex[][] toffoli = new Complex[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                toffoli[i][j] = Complex.ZERO;
            }
        }
        toffoli[0][0] = Complex.ONE;
        toffoli[1][1] = Complex.ONE;
        toffoli[2][2] = Complex.ONE;
        toffoli[3][3] = Complex.ONE;
        toffoli[4][4] = Complex.ONE;
        toffoli[5][5] = Complex.ONE;
        toffoli[7][6] = Complex.ONE;
        toffoli[6][7] = Complex.ONE;
        return new Matrix(toffoli);
    }
}
