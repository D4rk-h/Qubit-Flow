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

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumPort.QuantumGatePort;
import model.quantumModel.quantumState.QuantumState;

public class QuantumGate implements QuantumGatePort {
    private final Matrix matrix;
    private final int numQubits;
    private final String name;
    private final int[] targetQubits;

    public QuantumGate(Matrix matrix, int numQubits, String name) {
        this(matrix, numQubits, name, createDefaultTargets(numQubits));
    }

    public QuantumGate(Matrix matrix, int numQubits, String name, int[] targetQubits) {
        this.matrix = matrix;
        this.numQubits = numQubits;
        this.name = name;
        this.targetQubits = targetQubits.clone();
        validateGate();
    }

    private static int[] createDefaultTargets(int numQubits) {
        int[] targets = new int[numQubits];
        for (int i = 0; i < numQubits; i++) {
            targets[i] = i;
        }
        return targets;
    }

    private void validateGate() {
        int expectedSize = (int) Math.pow(2, numQubits);
        if (matrix.getRows() != expectedSize || matrix.getCols() != expectedSize) {
            throw new IllegalArgumentException("Matrix size must be " + expectedSize + "x" + expectedSize);
        }
    }

    public QuantumGate expandToSystem(int systemSize, int[] qubitPositions) {
        if (qubitPositions.length != this.numQubits) {
            throw new IllegalArgumentException("Must specify position for each qubit in gate");
        }
        Matrix expandedMatrix = buildExpandedMatrix(systemSize, qubitPositions);
        return new QuantumGate(expandedMatrix, systemSize, name + "_expanded", qubitPositions);
    }

    private Matrix buildExpandedMatrix(int systemSize, int[] positions) {
        int totalSize = (int) Math.pow(2, systemSize);
        Complex[][] expandedMatrix = new Complex[totalSize][totalSize];
        for (int i = 0; i < totalSize; i++) {
            for (int j = 0; j < totalSize; j++) {
                expandedMatrix[i][j] = Complex.ZERO;
            }
        }
        for (int i = 0; i < totalSize; i++) {
            for (int j = 0; j < totalSize; j++) {
                int gateInputState = extractBits(i, positions);
                int gateOutputState = extractBits(j, positions);
                if (areOtherBitsEqual(i, j, positions)) {
                    Complex matrixElement = matrix.get(gateOutputState, gateInputState);
                    expandedMatrix[j][i] = matrixElement;
                }
            }
        }
        return new Matrix(expandedMatrix);
    }

    private int extractBits(int state, int[] positions) {
        int result = 0;
        for (int i = 0; i < positions.length; i++) {
            if ((state & (1 << positions[i])) != 0) {
                result |= (1 << i);
            }
        }
        return result;
    }

    private boolean areOtherBitsEqual(int state1, int state2, int[] positions) {
        int mask = 0;
        for (int pos : positions) {
            mask |= (1 << pos);
        }
        return (state1 & ~mask) == (state2 & ~mask);
    }

    @Override
    public QuantumState apply(QuantumState state) {
        if (this.numQubits == state.getNumQubits()) {return state.applyGate(this);}
        else {throw new IllegalArgumentException("Gate size doesn't match system size. Use expandToSystem() first.");}
    }

    public Matrix getMatrix() { return matrix; }
    public String getName() { return name; }
    public int getNumQubits() { return numQubits; }
    public int[] getTargetQubits() { return targetQubits.clone(); }
}