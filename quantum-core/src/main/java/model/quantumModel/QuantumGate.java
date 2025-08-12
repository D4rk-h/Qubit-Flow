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

import java.util.HashSet;
import java.util.Set;

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
        for (int i = 0; i < numQubits; i++) targets[i] = i;
        return targets;
    }

    private void validateGate() {
        int expectedSize = (int) Math.pow(2, numQubits);
        if (matrix.getRows() != expectedSize || matrix.getCols() != expectedSize) {
            throw new IllegalArgumentException("Matrix size must be " + expectedSize + "x" + expectedSize);
        }
        if (targetQubits.length != numQubits) throw new IllegalArgumentException("Number of target qubits must match gate's n-qubit");
        Set<Integer> uniqueTargets = new HashSet<>();
        for (int target : targetQubits) {
            if (!uniqueTargets.add(target)) throw new IllegalArgumentException("Duplicate target qubit: " + target);
        }
    }

    public QuantumGate expandToSystem(int systemSize, int[] qubitPositions) {
        Matrix expandedMatrix = buildExpandedMatrix(systemSize);
        return new QuantumGate(expandedMatrix, systemSize, name + "_expanded", qubitPositions);
    }

    private Matrix buildExpandedMatrix(int systemSize) {
        return this.matrix.tensorPower(systemSize);
    }

    private int extractBits(int state, int[] positions) {
        int result = 0;
        for (int i = 0; i < positions.length; i++) if ((state & (1 << positions[i])) != 0) result |= (1 << i);
        return result;
    }

    private int replaceBits(int originalState, int newBits, int[] positions) {
        int result = originalState;
        for (int pos : positions) result &= ~(1 << pos);
        for (int i = 0; i < positions.length; i++) {
            if ((newBits & (1 << i)) != 0) result |= (1 << positions[i]);
        }
        return result;
    }

    private boolean areOtherBitsEqual(int state1, int state2, int[] positions) {
        int mask = 0;
        for (int pos : positions) mask |= (1 << pos);
        return (state1 & ~mask) == (state2 & ~mask);
    }

    @Override
    public QuantumState apply(QuantumState state) {
        if (this.numQubits == state.getNumQubits()) return state.applyGate(this);
        else throw new IllegalArgumentException("Gate size doesn't match system size. Use expandToSystem() first.");
    }

    public Matrix getMatrix() { return matrix; }
    public String getName() { return name; }
    public int getNumQubits() { return numQubits; }
    public int[] getTargetQubits() { return targetQubits.clone(); }
}