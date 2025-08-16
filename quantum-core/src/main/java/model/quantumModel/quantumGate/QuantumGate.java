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

import model.mathModel.Matrix;
import model.quantumModel.quantumPort.QuantumGatePort;
import model.quantumModel.quantumState.QuantumState;

public class QuantumGate implements QuantumGatePort {
    private final Matrix matrix;
    private int numQubits;
    private final String name;
    private final String symbol;

    public QuantumGate(Matrix matrix, int numQubits, String name, String symbol) {
        this.matrix = matrix;
        this.numQubits = numQubits;
        this.name = name;
        this.symbol = symbol;
        validateGate();
    }

    private void validateGate() {
        int expectedSize = (int) Math.pow(2, numQubits);
        if (matrix.getRows() != expectedSize || matrix.getCols() != expectedSize) {
            throw new IllegalArgumentException("Matrix size must be " + expectedSize + "x" + expectedSize);
        }
    }

    @Override
    public QuantumState apply(QuantumState state) {
        if (this.numQubits == state.getNumQubits()) return state.applyGate(this);
        else throw new IllegalArgumentException("Gate size doesn't match system size. Use expandToSystem() first.");
    }

    @Override
    public QuantumGate expandGateDimension(int circuitDimension, int whichQubitsToApply) {
        if (circuitDimension <= 0 || whichQubitsToApply <= 0) throw new IllegalArgumentException("Dimension and target qubits must have positive values");
        Matrix result = this.getMatrix().extendToMultiQubitGate(circuitDimension, whichQubitsToApply);
        return new QuantumGate(result, circuitDimension, this.name, this.symbol);
    }

    public void setNumQubits(int numQubits) {this.numQubits = numQubits;}
    public String getSymbol() {return symbol;}
    public Matrix getMatrix() { return matrix; }
    public String getName() { return name; }
    public int getNumQubits() { return numQubits; }
}