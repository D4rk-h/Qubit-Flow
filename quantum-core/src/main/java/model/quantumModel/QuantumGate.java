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

package model.quantumModel;

import model.mathModel.Matrix;
import model.quantumModel.quantumPort.QuantumGatePort;
import model.quantumModel.quantumState.QuantumState;

public class QuantumGate implements QuantumGatePort {
    private final Matrix matrix;
    private final int numQubits;
    private final String name;


    public QuantumGate(Matrix matrix, int numQubits, String name) {
        this.matrix = matrix;
        this.numQubits = numQubits;
        this.name = name;
        int expectedSize = (int) Math.pow(2, numQubits);
        if (matrix.getRows() != expectedSize || matrix.getCols() != expectedSize) {
            throw new IllegalArgumentException("Matrix size must be " + expectedSize + "x" + expectedSize);
        }
    }

    @Override
    public QuantumState apply(QuantumState state) {
        return state.applyGate(this);
    }

    public Matrix getMatrix() {
        return matrix;
    }
    public String getName() {
        return name;
    }
    public int getNumQubits() {
        return numQubits;
    }
}