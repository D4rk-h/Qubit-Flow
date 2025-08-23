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

package api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.mathModel.Matrix;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

class ComplexMatrixDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("rows")
    private final int rows;
    @JsonProperty("cols")
    private final int cols;
    @JsonProperty("elements")
    private final ComplexDto[][] elements;

    public ComplexMatrixDto() {
        this.rows = 0;
        this.cols = 0;
        this.elements = new ComplexDto[0][0];
    }

    @JsonCreator
    public ComplexMatrixDto(
            @JsonProperty("rows") int rows,
            @JsonProperty("cols") int cols,
            @JsonProperty("elements") ComplexDto[][] elements) {
        this.rows = rows;
        this.cols = cols;
        this.elements = elements != null ? deepCopy(elements) : new ComplexDto[0][0];
    }

    public static ComplexMatrixDto from(Matrix matrix) {
        ComplexDto[][] resultComplexDto = new ComplexDto[matrix.getRows()][matrix.getCols()];
        for (int i=0;i<matrix.getRows();i++) {
            for (int j=0;j< matrix.getCols();j++) {
                resultComplexDto[i][j] = ComplexDto.from(matrix.getData()[i][j]);
            }
        }
        return new ComplexMatrixDto(matrix.getRows(), matrix.getCols(), resultComplexDto);
    }

    private ComplexDto[][] deepCopy(ComplexDto[][] source) {
        ComplexDto[][] copy = new ComplexDto[source.length][];
        for (int i = 0; i < source.length; i++) copy[i] = source[i].clone();
        return copy;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public ComplexDto[][] getElements() { return deepCopy(elements); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexMatrixDto that = (ComplexMatrixDto) o;
        return rows == that.rows &&
                cols == that.cols &&
                Arrays.deepEquals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, cols);
        result = 31 * result + Arrays.deepHashCode(elements);
        return result;
    }

    @Override
    public String toString() {
        return "ComplexMatrixDto{" +
                "rows=" + rows +
                ", cols=" + cols +
                '}';
    }
}
