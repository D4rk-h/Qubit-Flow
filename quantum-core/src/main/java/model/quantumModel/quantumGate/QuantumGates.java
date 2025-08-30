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
import model.quantumModel.quantumGate.singleQubitGate.*;

public class QuantumGates {
    public static QuantumGate hadamard() { return new HadamardGate(); }
    public static QuantumGate not() { return new NotGate(); }
    public static QuantumGate z() { return new ZGate(); }
    public static QuantumGate y() { return new YGate(); }
    public static QuantumGate t() { return new TGate(); }
    public static QuantumGate s() { return new SGate(); }
    public static QuantumGate rx() {return new RXGate();}
    public static QuantumGate ry() {return new RYGate();}
    public static QuantumGate rz() {return new RZGate();}
    public static QuantumGate sDagger() {return new SDagger();}
    public static QuantumGate tDagger() {return new TDaggerGate();}
    public static QuantumGate xRoot() {return new XRootGate();}
    public static QuantumGate u() {return new UnitaryGate();}
    public static QuantumGate phase() {return new Phase();}

    public static QuantumGate measurement() {return new MeasurementGate();}

    public static QuantumGate cnot() {return new QuantumGate(buildCNotMatrix(), 2, "CNOT");}

    public static QuantumGate swap() {return new QuantumGate(buildSwapMatrix(), 2, "Swap");}

    public static QuantumGate toffoli() {
        return new QuantumGate(buildToffoliMatrix(), 3, "Toffoli");
    }

    public static QuantumGate controlled(QuantumGate gate) {
        return new QuantumGate(
                buildControlledMatrix(gate.getMatrix()), gate.getNumQubits() + 1, "C-" + gate.getName()
        );
    }

    private static Matrix buildCNotMatrix(){
        Complex[][] controlledX = new Complex[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) controlledX[i][j] = Complex.ZERO;
        }
        controlledX[0][0] = Complex.ONE;
        controlledX[1][1] = Complex.ONE;
        controlledX[2][3] = Complex.ONE;
        controlledX[3][2] = Complex.ONE;
        return new Matrix(controlledX);
    }

    private static Matrix buildSwapMatrix() {
        Complex[][] swap = new Complex[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) swap[i][j] = Complex.ZERO;
        }
        swap[0][0] = Complex.ONE;
        swap[1][2] = Complex.ONE;
        swap[2][1] = Complex.ONE;
        swap[3][3] = Complex.ONE;
        return new Matrix(swap);
    }

    private static Matrix buildToffoliMatrix() {
        Complex[][] toffoli = new Complex[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) toffoli[i][j] = Complex.ZERO;
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

    private static Matrix buildControlledMatrix(Matrix baseMatrix) {
        int baseSize = baseMatrix.getRows();
        int controlledSize = baseSize * 2;
        Complex[][] controlledData = new Complex[controlledSize][controlledSize];
        for (int i = 0; i < controlledSize; i++) {
            for (int j = 0; j < controlledSize; j++) controlledData[i][j] = Complex.ZERO;
        }
        for (int i = 0; i < baseSize; i++) controlledData[i][i] = Complex.ONE;
        for (int i = 0; i < baseSize; i++) {
            for (int j = 0; j < baseSize; j++) controlledData[i + baseSize][j + baseSize] = baseMatrix.get(i, j);
        }
        return new Matrix(controlledData);
    }

}
