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

public class ControlledXGate extends QuantumGate {
    public ControlledXGate() {super(buildControlledXMatrix(), 2, "CNOT");}

    public ControlledXGate(int controlQubit, int targetQubit) {
        super(buildControlledXMatrix(), 2, "CNOT", new int[]{controlQubit, targetQubit});
    }

    private static Matrix buildControlledXMatrix(){
        Complex[][] controlledX = new Complex[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                controlledX[i][j] = Complex.ZERO;
            }
        }
        controlledX[0][0] = Complex.ONE;
        controlledX[1][1] = Complex.ONE;
        controlledX[2][3] = Complex.ONE;
        controlledX[3][2] = Complex.ONE;
        return new Matrix(controlledX);
    }
}