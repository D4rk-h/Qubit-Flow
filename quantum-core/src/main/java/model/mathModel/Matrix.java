package model.mathModel;

public class Matrix {
    private double[][] data;
    private final int rows;
    private static final double EPSILON = 1e-10;
    private final int cols;
    private Complex[][] complexData;
    private boolean isComplex = false;

    public Matrix(Complex[][] data){
        this.complexData = data;
        this.isComplex = true;
        this.rows = data.length;
        this.cols = data[0].length;
    }

    public Matrix(int rows, int cols){
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("The matrix dimension cannot be built by negative numbers or 0");
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matrix(double[][] data){
        if (data == null ||data.length == 0 || data[0].length == 0){
            throw new IllegalArgumentException("MathCore.model.Matrix is empty.");
        }
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i ++ ){
            if (data[i].length != cols){
                throw new IllegalArgumentException("Inconsistent matrix. e.g. 2x3, 2x1...");
            }
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public double[][] getData(){return this.data;}
    public Complex[][] getComplexData(){return this.complexData;}

    public void set(int row, int col, double newValue){
        if (row < 0 || row >= rows || col < 0 || col>= cols){
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        data[row][col] = newValue;
    }

    public Object get(int row, int col, boolean isComplex){
        if (row < 0 || row >= rows || col < 0 || col>= cols){
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        if (isComplex){return complexData[row][col];}
        return data[row][col];
    }

    public void set(int row, int col, Complex newValue){
        if (row < 0 || row >= rows || col < 0 || col>= cols){
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        complexData[row][col] = newValue;
    }

    public Matrix add(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols){
            throw new IllegalArgumentException("Cannot Sum Matrices of distinct dimensions");
        }
        Matrix result = new Matrix(rows, cols);
        for (int i=0;i<rows;i++){
            for (int j=0;j<cols;j++){
                result.data[i][j] = data[i][j] + other.data[i][j];
            }
        }
        return result;
    }

    public Matrix sub(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols){
            throw new IllegalArgumentException("Cannot Subtract Matrices of distinct dimensions");
        }
        Matrix result = new Matrix(rows, cols);
        for (int i=0;i<rows;i++){
            for (int j=0;j<cols;j++){
                result.data[i][j] = data[i][j] - other.data[i][j];
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
                double sum = 0;
                for (int k=0;k<this.cols;k++) {
                    sum += data[i][k] * other.data[k][j];
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    public Complex[] multiplyVector(Complex[] vector) {
        if (isComplex) {
            if (vector.length != cols) {
                throw new IllegalArgumentException("Vector length must match matrix columns");
            }
            Complex[] result = new Complex[rows];
            for (int i=0;i<rows;i++) {
                result[i] = new Complex(0, 0);
                for (int j=0;j<cols;j++) {
                    result[i] = result[i].add(complexData[i][j].multiply(vector[j]));
                }
            }
            return result;
        } else {
            throw new IllegalStateException("Cannot multiply complex vector with real matrix");
        }
    }

    public Matrix multiply(double lambda){
        Matrix result = new Matrix(rows, cols);
        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                result.data[i][j] = lambda * this.data[i][j];
            }
        }
        return result;
    }

    public Matrix multiply(Complex lambda) {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.complexData[i][j] = new Complex(lambda.getRealPart() * this.data[i][j], lambda.getImaginaryPart() * this.data[i][j]);
            }
        }
        return result;
    }

    public Matrix transpose() {
        if (this.rows <= 0 || this.cols <= 0) {
            throw new IllegalArgumentException("MathCore.model.Matrix is empty...");
        }
        Matrix result = new Matrix(rows, cols);
        for (int i=0;i<this.rows;i++) {
            for (int j=0;j<this.cols;j++){
                result.data[j][i] = this.data[i][j];
            }
        }
        return result;
    }

    public double determinant() {
        if (rows != cols) {
            throw new IllegalStateException("Determinant can only be calculated for square matrices. Got " + rows + "x" + cols);
        }
        if (rows == 0) {
            return 1.0;
        }
        return calculateDeterminant(this.data);
    }

    private double calculateDeterminant(double[][] matrixData) {
        int n = matrixData.length;
        if (n == 1) {
            return matrixData[0][0];
        }
        if (n == 2) {
            return matrixData[0][0] * matrixData[1][1] - matrixData[0][1] * matrixData[1][0];
        }
        double det = 0;
        for (int j = 0; j < n; j++) {
            double[][] submatrix = createSubmatrix(matrixData, 0, j);
            double cofactor = Math.pow(-1, j) * matrixData[0][j] * calculateDeterminant(submatrix);
            det += cofactor;
        }
        return det;
    }

    private double[][] createSubmatrix(double[][] originalData, int rowToRemove, int colToRemove) {
        int n = originalData.length;
        double[][] submatrix = new double[n - 1][n - 1];
        int subRow = 0;
        for (int r = 0; r < n; r++) {
            if (r == rowToRemove) {
                continue;
            }
            int subCol = 0;
            for (int c = 0; c < n; c++) {
                if (c == colToRemove) {
                    continue;
                }
                submatrix[subRow][subCol] = originalData[r][c];
                subCol++;
            }
            subRow++;
        }
        return submatrix;
    }

    public boolean isSquared(){
        return rows == cols;
    }

    public Matrix inverse() {
        if (rows != cols) {
            throw new IllegalStateException("MathCore.model.Matrix must be square to calculate inverse. Got " + rows + "x" + cols);
        }
        int n = rows;
        if (n == 0) {
            throw new IllegalStateException("Cannot invert a 0x0 matrix.");
        }
        double det = determinant();
        if (Math.abs(det) < EPSILON) {
            throw new IllegalStateException("MathCore.model.Matrix is singular (determinant is approximately zero), cannot calculate inverse.");
        }
        if (n == 1) {
            return new Matrix(new double[][]{{1.0 / data[0][0]}});
        }
        double[][] cofactorData = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double[][] minor = createSubmatrix(this.data, i, j);
                double minorDet = calculateDeterminant(minor);
                double sign = ((i + j) % 2 == 0) ? 1.0 : -1.0;
                cofactorData[i][j] = sign * minorDet;
            }
        }
        double[][] adjugateData = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjugateData[j][i] = cofactorData[i][j];
            }
        }
        double[][] inverseData = new double[n][n];
        double invDet = 1.0 / det;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseData[i][j] = invDet * adjugateData[i][j];
            }
        }
        return new Matrix(inverseData);
    }
}
