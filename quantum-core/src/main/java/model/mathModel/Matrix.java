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

package model.mathModel;

public class Matrix {
    private final int rows;
    private final int cols;
    private Complex[][] data;

    public Matrix(Complex[][] data){
        this.data = data;
        this.rows = data.length;
        this.cols = data[0].length;
    }

    public Matrix(int rows, int cols){
        if (rows <= 0 || cols <= 0) {throw new IllegalArgumentException("The matrix dimension cannot be built by negative numbers or 0");}
        this.rows = rows;
        this.cols = cols;
        this.data = new Complex[rows][cols];
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public Complex[][] getData(){return this.data;}

    public void set(int row, int col, Complex newValue) {
        if (row < 0 || row >= rows || col < 0 || col>= cols){throw new IndexOutOfBoundsException("Index out of bounds");}
        data[row][col] = newValue;
    }

    public Complex get(int row, int col){
        if (row < 0 || row >= rows || col < 0 || col>= cols){throw new IndexOutOfBoundsException("Index out of bounds");}
        return data[row][col];
    }

    public Matrix add(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols){throw new IllegalArgumentException("Cannot Sum Matrices of distinct dimensions");}
        Matrix result = new Matrix(rows, cols);
        for (int i=0;i<rows;i++){
            for (int j=0;j<cols;j++){
                result.data[i][j] = data[i][j].add(other.data[i][j]);
            }
        }
        return result;
    }

    public Matrix sub(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols){throw new IllegalArgumentException("Cannot Subtract Matrices of distinct dimensions");}
        Matrix result = new Matrix(rows, cols);
        for (int i=0;i<rows;i++){
            for (int j=0;j<cols;j++){
                result.data[i][j] = data[i][j].subtract(other.data[i][j]);
            }
        }
        return result;
    }

    public Matrix multiply(Matrix other) {
        if (this.cols != other.rows){
            throw new IllegalArgumentException("Cannot Subtract Matrices of distinct dimensions");
        }
        Matrix result = new Matrix(this.rows, other.cols);
        for (int i=0;i<this.rows;i++){
            for (int j=0;j<other.cols;j++){
                Complex sum = Complex.ZERO;
                for (int k=0;k<this.cols;k++) {
                    sum = sum.add(data[i][k].multiply(other.data[k][j]));
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    public Matrix adjoint() {
        Matrix transposed = this.transpose();
        for (int i=0; i<transposed.getData().length;i++) {
            for (int j=0; j<transposed.getData()[i].length; j++) {
                transposed.set(i, j, transposed.getData()[i][j].conjugate());
            }
        }
        return transposed;
    }

    public Complex[] multiplyVector(Complex[] vector) {
        if (vector.length != cols) {throw new IllegalArgumentException("Vector length must match matrix columns");}
        Complex[] result = new Complex[rows];
        for (int i=0;i<rows;i++) {
            result[i] = new Complex(0, 0);
            for (int j=0;j<cols;j++) {
                result[i] = result[i].add(data[i][j].multiply(vector[j]));
            }
        }
        return result;
    }

    public Matrix multiply(double lambda){
        Matrix result = new Matrix(rows, cols);
        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                result.data[i][j] = this.data[i][j].scale(lambda);
            }
        }
        return result;
    }

    public Matrix multiply(Complex lambda) {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i][j] = this.data[i][j].multiply(lambda);
            }
        }
        return result;
    }

    public Matrix transpose() {
        if (this.rows <= 0 || this.cols <= 0) {throw new IllegalArgumentException("MathCore.model.Matrix is empty...");}
        Matrix result = new Matrix(rows, cols);
        for (int i=0;i<this.rows;i++) {
            for (int j=0;j<this.cols;j++){
                result.data[j][i] = this.data[i][j];
            }
        }
        return result;
    }

    public Matrix conjugate() {
        Matrix result = new Matrix(this.rows, this.cols);
        if (this.data.length > 0){
            for (int i=0;i<this.data.length;i++) {
                for (int j=0;j<this.data[i].length;j++) {
                    result.data[i][j] = this.data[i][j].conjugate();
                }
            }
        }
        return result;
    }

    public Complex determinant() {
        if (rows != cols) {throw new IllegalStateException("Determinant can only be calculated for square matrices. Got " + rows + "x" + cols);}
        if (rows == 0) {return Complex.ONE;}
        return calculateDeterminant(this.data);
    }

    private Complex calculateDeterminant(Complex[][] matrixData) {
        int n = matrixData.length;
        if (n == 1) {return matrixData[0][0];}
        if (n == 2) {
            Complex term1 = matrixData[0][0].multiply(matrixData[1][1]);
            Complex term2 = matrixData[0][1].multiply(matrixData[1][0]);
            return term1.subtract(term2);
        }
        Complex det = Complex.ZERO;
        for (int j = 0; j < n; j++) {
            Complex[][] submatrix = createSubmatrix(matrixData, 0, j);
            Complex minorDet = calculateDeterminant(submatrix);
            Complex sign = (j % 2 == 0) ? Complex.ONE : Complex.ONE.scale(-1.0);
            Complex cofactor = sign.multiply(matrixData[0][j]).multiply(minorDet);
            det = det.add(cofactor);
        }
        return det;
    }

    private Complex[][] createSubmatrix(Complex[][] originalData, int rowToRemove, int colToRemove) {
        int n = originalData.length;
        Complex[][] submatrix = new Complex[n - 1][n - 1];
        int subRow = 0;
        for (int r = 0; r < n; r++) {
            if (r == rowToRemove) {continue;}
            int subCol = 0;
            for (int c = 0; c < n; c++) {
                if (c == colToRemove) {continue;}
                submatrix[subRow][subCol] = originalData[r][c];
                subCol++;
            }
            subRow++;
        }
        return submatrix;
    }

    public boolean isSquared(){return rows == cols;}

    public Matrix inverse() {
        if (rows != cols) {throw new IllegalStateException("Matrix must be square to calculate inverse. Got " + rows + "x" + cols);}
        int n = rows;
        if (n == 0) {throw new IllegalStateException("Cannot invert a 0x0 matrix.");}
        Complex det = determinant();
        if (det.magnitude() < Complex.EPSILON) {throw new IllegalStateException("Matrix is singular (determinant is approximately zero), cannot calculate inverse.");}
        if (n == 1) {
            Complex[][] inverseData = new Complex[1][1];
            inverseData[0][0] = Complex.ONE.divide(data[0][0]);
            return new Matrix(inverseData);
        }
        Complex[][] cofactorData = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Complex[][] minor = createSubmatrix(this.data, i, j);
                Complex minorDet = calculateDeterminant(minor);
                Complex sign = ((i + j) % 2 == 0) ? Complex.ONE : Complex.ONE.scale(-1.0);
                cofactorData[i][j] = sign.multiply(minorDet);
            }
        }
        Complex[][] adjugateData = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjugateData[j][i] = cofactorData[i][j];
            }
        }
        Complex[][] inverseData = new Complex[n][n];
        Complex invDet = Complex.ONE.divide(det);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseData[i][j] = invDet.multiply(adjugateData[i][j]);
            }
        }
        return new Matrix(inverseData);
    }
}
