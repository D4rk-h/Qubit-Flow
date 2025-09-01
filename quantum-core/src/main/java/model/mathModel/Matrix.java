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

import model.mathModel.qpca.EigenDecomposition;
import model.mathModel.qpca.GivensRotation;
import model.mathModel.qpca.QRResult;
import model.mathModel.qpca.SVDResult;

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

    public Matrix exponential() { // Taylor series (small matrices)
        if (!isSquared()) throw new IllegalArgumentException("Matrix should be squared");
        Matrix result = createIdentityMatrix(this.rows);
        Matrix term = createIdentityMatrix(this.rows);
        int maxIter = 49;
        double tolerance = 1e-12;
        for (int k=0;k<maxIter;k++) { // term= Aˆk / k!
            term = term.multiply(this).multiply(1.0/k);
            Matrix oldResult = result.copy();
            result = result.add(term);
            if (frobeniusNorm(result.sub(oldResult)) < tolerance) break;
        }
        return result;
    }

    public Matrix exponential(Complex scalar) {return this.multiply(scalar).exponential();}

    public Matrix exponentialHermitian() {
        if (!isHermitian()) throw new IllegalArgumentException("Matrix should be Hermitian");
        EigenDecomposition eigen = this.eigenDecomposition();
        Matrix Q = eigen.getEigenvectors();
        Complex[] eigenvalues = eigen.getEigenvalues();
        Matrix expLambda = new Matrix(this.rows, this.cols);
        for (int i = 0;i<this.rows;i++) {
            Complex expEigen = complexExponential(eigenvalues[i]);
            expLambda.set(i, i, expEigen);
        }
        return Q.multiply(expLambda).multiply(Q.adjoint());
    }

    private Complex complexExponential(Complex z) {
        double expReal = Math.exp(z.getRealPart());
        double cosImag = Math.cos(z.getImaginaryPart());
        double sinImag = Math.sin(z.getImaginaryPart());
        return new Complex(expReal * cosImag, expReal * sinImag);
    }

    public Matrix multiply(Matrix other) {
        if (this.cols != other.rows)throw new IllegalArgumentException("Cannot Multiply Matrices of distinct dimensions");
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
        if (other == null) throw new IllegalArgumentException("Other matrix cannot be null");
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

    public EigenDecomposition eigenDecomposition() {
        if (!isSquared()) throw new IllegalArgumentException("Eigendecomposition requires square matrix");
        int n = this.rows;
        Matrix A = this.copy();
        Matrix Q_total = createIdentityMatrix(n);
        for (int iter = 0; iter < 100; iter++) {
            QRResult qr = A.qrDecomposition();
            A = qr.R().multiply(qr.Q());
            Q_total = Q_total.multiply(qr.Q());
            if (isUpperTriangular(A, 1e-10)) break;
        }
        Complex[] eigenvalues = new Complex[n];
        for (int i = 0; i < n; i++) {
            eigenvalues[i] = A.get(i, i);
        }
        return new EigenDecomposition(eigenvalues, Q_total);
    }

    public QRResult qrDecomposition() {
        int m = this.rows;
        int n = this.cols;
        Matrix Q = new Matrix(m, n);
        Matrix R = new Matrix(n, n);
        for (int j = 0; j < n; j++) {
            Complex[] col = new Complex[m];
            for (int i = 0; i < m; i++) col[i] = this.get(i, j);
            for (int k = 0; k < j; k++) {
                Complex dot = Complex.ZERO;
                for (int i = 0; i < m; i++) {
                    dot = dot.add(Q.get(i, k).conjugate().multiply(col[i]));
                }
                R.set(k, j, dot);
                for (int i = 0; i < m; i++) {
                    col[i] = col[i].subtract(Q.get(i, k).multiply(dot));
                }
            }
            double norm = 0;
            for (Complex c : col) norm += c.magnitudeSquared();
            norm = Math.sqrt(norm);
            R.set(j, j, new Complex(norm, 0));
            for (int i = 0; i < m; i++) Q.set(i, j, col[i].scale(1.0 / norm));
        }
        return new QRResult(Q, R);
    }

    private boolean isUpperTriangular(Matrix matrix, double tolerance) {
        for (int i = 1; i < matrix.rows; i++) {
            for (int j = 0; j < i; j++) {
                if (matrix.get(i, j).magnitude() > tolerance) {
                    return false;
                }
            }
        }
        return true;
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

    public double conditionNumber() {
        SVDResult svd = singularValueDecomposition();
        Complex[] singularValues = svd.singularValues();
        if (singularValues.length == 0) throw new IllegalStateException("No singular values found");
        double maxSV = 0;
        double minSV = Double.MAX_VALUE;
        for (Complex sv : singularValues) {
            double magnitude = sv.magnitude();
            maxSV = Math.max(maxSV, magnitude);
            if (magnitude > 1e-15) minSV = Math.min(minSV, magnitude);
        }
        if (minSV == Double.MAX_VALUE || minSV < 1e-15) return Double.POSITIVE_INFINITY;
        return maxSV / minSV;
    }

    public SVDResult singularValueDecomposition() { //Golub-Reinsch algorithm
        int m = this.rows;
        int n = this.cols;
        Matrix A = this.copy();
        Matrix U = createIdentityMatrix(m);
        Matrix V = createIdentityMatrix(n);
        bidiagonalize(A, U, V);
        diagonalizeBidiagonal(A, U, V);
        Complex[] singularValues = extractAndSortSingularValues(A, U, V);
        return new SVDResult(U, singularValues, V);
    }

    private void bidiagonalize(Matrix A, Matrix U, Matrix V) {
        int m = A.rows;
        int n = A.cols;
        int minDim = Math.min(m, n);
        for (int k = 0; k < minDim; k++) {
            if (k < m - 1) {
                Complex[] x = new Complex[m - k];
                for (int i = k; i < m; i++) x[i - k] = A.get(i, k);
                Complex[] v = computeHouseholderVector(x);
                if (v != null) {
                    applyHouseholderLeft(A, v, k, k);
                    applyHouseholderLeft(U, v, k, 0);
                }
            }
            if (k < n - 2) {
                Complex[] y = new Complex[n - k - 1];
                for (int j = k + 1; j < n; j++) {
                    y[j - k - 1] = A.get(k, j);
                }
                Complex[] v = computeHouseholderVector(y);
                if (v != null) {
                    applyHouseholderRight(A, v, k + 1, k);
                    applyHouseholderRight(V, v, k + 1, 0);
                }
            }
        }
    }

    private Complex[] computeHouseholderVector(Complex[] x) {
        if (x.length <= 1) return null;
        Complex alpha = x[0];
        double norm = 0;
        for (Complex c : x) norm += c.magnitudeSquared();
        norm = Math.sqrt(norm);
        if (norm < 1e-15) return null;
        Complex sign = alpha.magnitude() > 1e-15 ?
                new Complex(alpha.getRealPart() / alpha.magnitude(), alpha.getImaginaryPart() / alpha.magnitude()) :
                Complex.ONE;
        Complex u1 = alpha.add(sign.scale(norm));
        Complex[] v = new Complex[x.length];
        v[0] = u1;
        for (int i = 1; i < x.length; i++) v[i] = x[i];
        double vNorm = 0;
        for (Complex c : v) vNorm += c.magnitudeSquared();
        vNorm = Math.sqrt(vNorm);
        if (vNorm < 1e-15) return null;
        for (int i = 0; i < v.length; i++) v[i] = v[i].scale(1.0 / vNorm);
        return v;
    }

    private void applyHouseholderLeft(Matrix A, Complex[] v, int startRow, int startCol) {
        int m = A.rows;
        int n = A.cols;
        for (int j = startCol; j < n; j++) {
            Complex dot = Complex.ZERO;
            for (int i = 0; i < v.length && startRow + i < m; i++) {
                dot = dot.add(v[i].conjugate().multiply(A.get(startRow + i, j)));
            }
            Complex factor = dot.scale(2.0);
            for (int i = 0; i < v.length && startRow + i < m; i++) {
                Complex oldValue = A.get(startRow + i, j);
                Complex newValue = oldValue.subtract(v[i].multiply(factor));
                A.set(startRow + i, j, newValue);
            }
        }
    }

    private void applyHouseholderRight(Matrix A, Complex[] v, int startCol, int startRow) {
        int m = A.rows;
        int n = A.cols;
        for (int i = startRow; i < m; i++) {
            Complex dot = Complex.ZERO;
            for (int j = 0; j < v.length && startCol + j < n; j++) {
                dot = dot.add(A.get(i, startCol + j).multiply(v[j]));
            }
            Complex factor = dot.scale(2.0);
            for (int j = 0; j < v.length && startCol + j < n; j++) {
                Complex oldValue = A.get(i, startCol + j);
                Complex newValue = oldValue.subtract(factor.multiply(v[j].conjugate()));
                A.set(i, startCol + j, newValue);
            }
        }
    }

    private void diagonalizeBidiagonal(Matrix B, Matrix U, Matrix V) {
        int n = Math.min(B.rows, B.cols);
        double tolerance = 1e-15;
        int maxIterations = 100;
        for (int iter = 0; iter < maxIterations; iter++) {
            boolean converged = true;
            for (int i = 0; i < n - 1; i++) {
                if (i < B.cols - 1 && B.get(i, i + 1).magnitude() > tolerance) {
                    converged = false;
                    break;
                }
                if (i + 1 < B.rows && B.get(i + 1, i).magnitude() > tolerance) {
                    converged = false;
                    break;
                }
            }
            if (converged) break;
            qrIterationBidiagonal(B, U, V);
        }
    }

    private void qrIterationBidiagonal(Matrix B, Matrix U, Matrix V) {
        int n = Math.min(B.rows, B.cols);
        for (int i = 0; i < n - 1; i++) {
            Complex a = B.get(i, i);
            Complex b = (i < B.cols - 1) ? B.get(i, i + 1) : Complex.ZERO;
            Complex c = (i + 1 < B.rows) ? B.get(i + 1, i) : Complex.ZERO;
            Complex d = (i + 1 < B.rows && i + 1 < B.cols) ? B.get(i + 1, i + 1) : Complex.ZERO;
            if (b.magnitude() > 1e-15) {
                GivensRotation rotation = computeGivensRotation(a, b);
                applyGivensRight(B, rotation, i, i + 1);
                applyGivensRight(V, rotation, i, i + 1);
            }
            if (c.magnitude() > 1e-15) {
                GivensRotation rotation = computeGivensRotation(a, c);
                applyGivensLeft(B, rotation, i, i + 1);
                applyGivensLeft(U, rotation, i, i + 1);
            }
        }
    }

    private GivensRotation computeGivensRotation(Complex a, Complex b) {
        if (b.magnitude() < 1e-15) return new GivensRotation(Complex.ONE, Complex.ZERO);
        if (a.magnitude() < 1e-15) return new GivensRotation(Complex.ZERO, Complex.ONE);
        Complex r = Complex.ZERO;
        for (Complex x : new Complex[]{a, b}) r = r.add(new Complex(x.magnitudeSquared(), 0));
        r = new Complex(Math.sqrt(r.getRealPart()), 0);
        Complex cos = a.divide(r);
        Complex sin = b.divide(r);
        return new GivensRotation(cos, sin);
    }

    private void applyGivensLeft(Matrix A, GivensRotation rotation, int i, int j) {
        int n = A.cols;
        for (int k = 0; k < n; k++) {
            Complex ai = A.get(i, k);
            Complex aj = A.get(j, k);
            A.set(i, k, rotation.cos().multiply(ai).add(rotation.sin().multiply(aj)));
            A.set(j, k, rotation.cos().multiply(aj).subtract(rotation.sin().conjugate().multiply(ai)));
        }
    }

    private void applyGivensRight(Matrix A, GivensRotation rotation, int i, int j) {
        int m = A.rows;
        for (int k = 0; k < m; k++) {
            Complex ai = A.get(k, i);
            Complex aj = A.get(k, j);
            A.set(k, i, rotation.cos().multiply(ai).add(rotation.sin().multiply(aj)));
            A.set(k, j, rotation.cos().multiply(aj).subtract(rotation.sin().conjugate().multiply(ai)));
        }
    }

    private Complex[] extractAndSortSingularValues(Matrix B, Matrix U, Matrix V) {
        int n = Math.min(B.rows, B.cols);
        Complex[] singularValues = new Complex[n];
        for (int i = 0; i < n; i++) {
            double sv = Math.abs(B.get(i, i).getRealPart());
            singularValues[i] = new Complex(sv, 0);
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (singularValues[i].getRealPart() < singularValues[j].getRealPart()) {
                    // Swap singular values
                    Complex temp = singularValues[i];
                    singularValues[i] = singularValues[j];
                    singularValues[j] = temp;
                    swapColumns(U, i, j);
                    swapColumns(V, i, j);
                }
            }
        }
        return singularValues;
    }

    private void swapColumns(Matrix matrix, int col1, int col2) {
        for (int i = 0; i < matrix.rows; i++) {
            Complex temp = matrix.get(i, col1);
            matrix.set(i, col1, matrix.get(i, col2));
            matrix.set(i, col2, temp);
        }
    }

    private Matrix computeU(Matrix A, Matrix V, Complex[] singularValues) {
        int m = A.rows;
        int n = Math.min(A.cols, singularValues.length);
        Matrix U = new Matrix(m, n);
        for (int j = 0; j < n; j++) {
            if (singularValues[j].magnitude() > 1e-15) {
                Complex[] v_j = new Complex[A.cols];
                for (int i = 0; i < A.cols; i++) v_j[i] = V.get(i, j);
                Complex[] u_j = A.multiplyVector(v_j);
                Complex invSigma = Complex.ONE.divide(singularValues[j]);
                for (int i = 0; i < m; i++) U.set(i, j, u_j[i].multiply(invSigma));
            }
        }
        return U;
    }

    public double conditionNumberEstimate() {
        if (!isSquared()) throw new IllegalArgumentException("Condition number estimation requires square matrix");
        double normA = frobeniusNorm();
        Matrix inv;
        try {inv = this.inverse();
        } catch (IllegalStateException e) {return Double.POSITIVE_INFINITY;}
        double normInvA = inv.frobeniusNorm();
        return normA * normInvA;
    }

    public Complex determinant() {
        if (rows != cols) {throw new IllegalStateException("Determinant can only be calculated for square matrices. Got " + rows + "x" + cols);}
        if (rows == 0) {return Complex.ONE;}
        return calculateDeterminant(this.data);
    }

    private Complex calculateDeterminant(Complex[][] matrixData) {
        int n = matrixData.length;
        if (n == 1) return matrixData[0][0];
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

    private double frobeniusNorm(Matrix matrix) { // Frobenius normalization of M
        double sum = 0;
        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.cols; j++) sum += matrix.get(i, j).magnitudeSquared();
        }
        return Math.sqrt(sum);
    }

    public double frobeniusNorm() {
        double sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum += this.get(i, j).magnitudeSquared();
            }
        }
        return Math.sqrt(sum);
    }

    public double spectralNorm() {
        SVDResult svd = singularValueDecomposition();
        Complex[] singularValues = svd.singularValues();
        double maxSV = 0;
        for (Complex sv : singularValues) maxSV = Math.max(maxSV, sv.magnitude());
        return maxSV;
    }

    public boolean isWellConditioned() {
        double cond = conditionNumber();
        return cond < 1e12;
    }

    public int rank() {
        SVDResult svd = singularValueDecomposition();
        Complex[] singularValues = svd.singularValues();
        int rank = 0;
        double tolerance = 1e-12;
        for (Complex sv : singularValues) if (sv.magnitude() > tolerance) rank++;
        return rank;
    }

    public boolean isHermitian() {
        if (!isSquared()) return false;
        Matrix adjoint = this.adjoint();
        double tolerance = 1e-10;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!this.get(i, j).isApproximatelyEqual(adjoint.get(i, j), tolerance)) return false;
            }
        }
        return true;
    }

    public Matrix timeEvolution(double time) { //quantum time evolution under Schrödinger equ
        Complex imaginaryTime = new Complex(0, -time);
        return this.multiply(imaginaryTime).exponentialHermitian();
    }
}
