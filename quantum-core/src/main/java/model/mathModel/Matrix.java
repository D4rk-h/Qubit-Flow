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
            throw new IllegalArgumentException("Cannot Multiply Matrices of distinct dimensions");
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

    public Matrix tensorProduct(Matrix other) {
        if (other == null) {
            throw new IllegalArgumentException("Other matrix cannot be null");
        }
        int newRows = this.rows * other.rows;
        int newCols = this.cols * other.cols;
        Matrix result = new Matrix(newRows, newCols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                Complex aij = this.data[i][j];
                for (int k = 0; k < other.rows; k++) {
                    for (int l = 0; l < other.cols; l++) {
                        int resultRow = i * other.rows + k;
                        int resultCol = j * other.cols + l;
                        result.data[resultRow][resultCol] = aij.multiply(other.data[k][l]);
                    }
                }
            }
        }

        return result;
    }

    public Matrix extendToMultiQubitGate(int totalQubits, int targetQubit) {
        if (totalQubits < 1) throw new IllegalArgumentException("Total qubits must be at least 1");
        if (targetQubit < 0 || targetQubit >= totalQubits) throw new IllegalArgumentException("Target qubit must be between 0 and " + (totalQubits - 1));
        if (!isSquared() || !isPowerOfTwo(this.rows)) throw new IllegalArgumentException("Gate must be square and dimension must be power of 2");
        int gateQubits = (int) (Math.log(this.rows) / Math.log(2));
        if (targetQubit + gateQubits > totalQubits) throw new IllegalArgumentException("Gate doesn't fit at target position");
        Matrix result = createIdentityMatrix(1);
        for (int i = 0; i < totalQubits; i += gateQubits) {
            if (i == targetQubit) {
                result = result.tensorProduct(this);
                i += gateQubits - 1;
            } else {
                result = result.tensorProduct(createIdentityMatrix(2));
            }
        }
        return result;
    }

    public Matrix tensorPower(int copies) {
        if (copies < 1) throw new IllegalArgumentException("Number of copies must be at least 1");
        if (copies == 1) return this.copy();
        Matrix result = this;
        for (int i = 1; i < copies; i++) {
            result = result.tensorProduct(this);
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

    private boolean isPowerOfTwo(int n) {return n > 0 && (n & (n - 1)) == 0;}

    public static Matrix createIdentityMatrix(int size) {
        Matrix identity = new Matrix(size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                identity.data[i][j] = (i == j) ? Complex.ONE : Complex.ZERO;
            }
        }
        return identity;
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

    private boolean isSquared(){return rows == cols;}

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

    public Matrix copy() {
        Matrix copy = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                copy.data[i][j] = this.data[i][j];
            }
        }
        return copy;
    }

    @Override
    public String toString() {
        return toString(4);
    }

    public String toString(int precision) {
        if (rows == 0 || cols == 0) return "[]";
        int maxWidth = findMaxElementWidth(precision);
        StringBuilder result = new StringBuilder();
        String formatString = "%" + maxWidth + "s";
        String repeat = " ".repeat(maxWidth * cols + (cols - 1) * 2 + 2);
        result.append("┌").append(repeat).append("┐\n");
        for (int i = 0; i < rows; i++) {
            result.append("│ ");
            for (int j = 0; j < cols; j++) {
                String element = formatElement(this.data[i][j], precision);
                result.append(String.format(formatString, element));
                if (j < cols - 1) result.append("  ");
            }
            result.append(" │\n");
        }
        result.append("└").append(repeat).append("┘");
        return result.toString();
    }

    private int findMaxElementWidth(int precision) {
        int maxWidth = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String element = formatElement(this.data[i][j], precision);
                maxWidth = Math.max(maxWidth, element.length());
            }
        }
        return maxWidth;
    }

    private String formatElement(Complex element, int precision) {
        if (element == null) return "null";
        double real = element.getRealPart();
        double imag = element.getImaginaryPart();
        if (Math.abs(imag) < Math.pow(10, -precision)) {
            return formatDouble(real, precision);
        }
        if (Math.abs(real) < Math.pow(10, -precision)) {
            if (Math.abs(imag - 1.0) < Math.pow(10, -precision)) {
                return "i";
            } else if (Math.abs(imag + 1.0) < Math.pow(10, -precision)) {
                return "-i";
            } else {
                return formatDouble(imag, precision) + "i";
            }
        }
        String realPart = formatDouble(real, precision);
        String imagPart = formatDouble(Math.abs(imag), precision);
        String sign = imag >= 0 ? "+" : "-";
        if (Math.abs(Math.abs(imag) - 1.0) < Math.pow(10, -precision)) return realPart + sign + "i";
        else return realPart + sign + imagPart + "i";
    }

    private String formatDouble(double value, int precision) {
        if (Double.isNaN(value)) return "NaN";
        if (Double.isInfinite(value)) return value > 0 ? "∞" : "-∞";
        if (Math.abs(value) < Math.pow(10, -precision)) return "0";
        String format = "%." + precision + "f";
        String result = String.format(format, value);
        if (result.contains(".")) {result = result.replaceAll("0+$", "").replaceAll("\\.$", "");}
        return result;
    }

    public int dimension() {
        if (this.isSquared()) {
            return this.getRows();
        }
        return -1;
    }
}
