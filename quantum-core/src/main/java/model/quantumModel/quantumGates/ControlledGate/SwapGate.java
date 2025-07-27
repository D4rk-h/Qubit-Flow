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

package model.quantumModel.quantumGates.ControlledGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.QuantumGate;

public class SwapGate extends QuantumGate {
    public SwapGate() {super(buildSwapMatrix(), 2, "Swap");}

    private static Matrix buildSwapMatrix() {
        Complex[][] swapG = new Complex[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                swapG[i][j] = new Complex(0, 0);
            }
        }
        swapG[0][0] = new Complex(1, 0);
        swapG[1][2] = new Complex(1, 0);
        swapG[2][1] = new Complex(1, 0);
        swapG[3][3] = new Complex(1, 0);
        return new Matrix(swapG);
    }
}