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

package model.quantumModel.measurementDisplay.density;

import model.mathModel.Complex;
import model.mathModel.Matrix;
import model.quantumModel.quantumState.QuantumState;
import model.quantumModel.measurementDisplay.displayUtils.DisplayCategory;
import model.quantumModel.measurementDisplay.displayUtils.DisplayPort;

public class Density implements DisplayPort {
    private final boolean showOnlyDiagonal;

    public Density(boolean showOnlyDiagonal) {
        this.showOnlyDiagonal = showOnlyDiagonal;
    }

    @Override
    public String getDisplaySymbol() {
        return DisplayCategory.DENSITY.getSymbol();
    }

    @Override
    public String getDisplayName() {
        return "Density Matrix Display";
    }

    @Override
    public Object renderContent(QuantumState state) {
        if (!isCompatibleWith(state.getNumQubits())) throw new IllegalArgumentException("Too much qubits, matrix is not legible");
        Matrix densityMatrix = calculateDensityMatrix(state);
        return showOnlyDiagonal ? extractDiagonal(densityMatrix) : densityMatrix;
    }

    @Override
    public boolean isCompatibleWith(int qubitCount) {
        return qubitCount < 8;
    }

    private Matrix calculateDensityMatrix(QuantumState state) {
        Complex[] amplitudes = state.getAmplitudes();
        int size = amplitudes.length;
        Complex[][] density = new Complex[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                density[i][j] = amplitudes[i].multiply(amplitudes[j].conjugate());
            }
        }
        return new Matrix(density);
    }

    private Matrix extractDiagonal(Matrix matrix) {
        Matrix result = new Matrix(matrix.getRows(), matrix.getCols());
        for (int i=0;i<matrix.getRows();i++) {
            for (int j=0;j<matrix.getCols();j++) {
                if (i==j){
                    result.set(i, j, (Complex) matrix.get(i, j));
                }
                result.set(i, j, null);
            }
        }
        return result;
    }
}
