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
import model.quantumModel.QuantumGate;

public class PauliZGate extends QuantumGate {
    public PauliZGate() {
        super(buildPauliZ(), 1, "ZPauli-Z");
    }

    private static Matrix buildPauliZ() {
        Complex[][] pauliZ = new Complex[2][2];
        pauliZ[0][0] = new Complex(1, 0);
        pauliZ[0][1] = new Complex(0, 0);
        pauliZ[1][0] = new Complex(0, 0);
        pauliZ[1][1] = new Complex(-1, 0);
        return new Matrix(pauliZ);
    }
}