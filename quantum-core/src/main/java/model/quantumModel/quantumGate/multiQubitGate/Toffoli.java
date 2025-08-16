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
import model.quantumModel.quantumGate.Control.ControlGate;
import model.quantumModel.quantumGate.QuantumGate;

public class Toffoli extends QuantumGate {
    private final Matrix matrix = buildToffoliMatrix();
    private int numOfQubits = 3;
    private ControlGate firstControl;
    private ControlGate secondControl;

    public Toffoli(ControlGate firstControl, ControlGate secondControl) {
        super(buildToffoliMatrix(), 3, "Toffoli", "[T]");
        this.firstControl = firstControl;
        this.secondControl = secondControl;
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

    public Matrix getMatrix() {
        return matrix;
    }

    public String getName() {
        return "Toffoli";
    }

    public ControlGate getFirstControl() {
        return firstControl;
    }

    public void setFirstControl(ControlGate newFirstControl) {
        this.firstControl = newFirstControl;
    }

    public ControlGate getSecondControl() {
        return secondControl;
    }

    public void setSecondControl(ControlGate newSecondControl) {
        this.secondControl = newSecondControl;
    }

    public String getSymbol() {
        return "[T]";
    }

    public int getNumOfQubits() {
        return numOfQubits;
    }

    public void setNumOfQubits(int numOfQubits) {
        this.numOfQubits = numOfQubits;
    }
}
