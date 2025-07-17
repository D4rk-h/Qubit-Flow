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

package model.quantumModel.QuantumGates.ControlledGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.QuantumGate;

public class ToffoliGate extends QuantumGate {
    public ToffoliGate() {super(buildToffoliMatrix(), 3, "Toffoli");}

    private static Matrix buildToffoliMatrix() {
        Complex[][] toffoliG = new Complex[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                toffoliG[i][j] = new Complex(0, 0);
            }
        }
        toffoliG[0][0] = new Complex(1, 0);
        toffoliG[1][2] = new Complex(1, 0);
        toffoliG[2][2] = new Complex(1, 0);
        toffoliG[3][3] = new Complex(1, 0);
        toffoliG[4][4] = new Complex(1, 0);
        toffoliG[5][5] = new Complex(1, 0);
        toffoliG[6][7] = new Complex(1, 0);
        toffoliG[7][6] = new Complex(1, 0);
        return new Matrix(toffoliG);
    }
}