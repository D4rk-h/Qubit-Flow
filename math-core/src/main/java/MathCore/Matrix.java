package MathCore;

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


    /**
     * Constructs a new matrix from a 2D double array.
     * @param data: The 2D double array acting as a Matrix
     * @throws IllegalArgumentException: If array is empty or has negative dimensions
     */
    public Matrix(double[][] data){
        if (data == null ||data.length == 0 || data[0].length == 0){
            throw new IllegalArgumentException("Matrix is empty.");
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


    /**
     * Get the number of columns of the matrix
     * @return an integer number of cols
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows of the matrix
     * @return an integer number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Set a value in the position of a certain element of the matrix given two indexes
     *
     * @param row element row index
     * @param col element col index
     * @param newValue new value that the certain element position being accessed will have
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public void set(int row, int col, double newValue){
        if (row < 0 || row >= rows || col < 0 || col>= cols){
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        data[row][col] = newValue;
    }

    /**
     * Get a certain element of the matrix given two indexes
     *
     * @param row element row index
     * @param col element col index
     * @throws IndexOutOfBoundsException if index is out of bounds
     * @return a double, the element that is where the indexes are pointing
     */
    public double get(int row, int col){
        if (row < 0 || row >= rows || col < 0 || col>= cols){
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return data[row][col];
    }

    /**
     * Add to a matrix another. Note: Matrix can be summed only if their dimensions are equal
     *
     * @param other A matrix that will be added to the matrix that its being applied this function
     * @throws IllegalArgumentException when dimensions of the matrices are not the same
     * @return a new Matrix result of the addition from origin Matrix and other Matrix
     */
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

    /**
     *  From an origin matrix subtracts another. Note: Matrix can be subtracted only if their dimensions are equal
     *
     * @param other A matrix that will be subtracted to the matrix that its being applied this function
     * @throws IllegalArgumentException when dimensions of the matrices are not the same
     * @return a new Matrix result of the subtraction from origin Matrix and other Matrix
     */
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

    /**
     * Multiply two matrices. Note: A matrix multiplication: (a b) x (1 2)= (a1+b3, a2+b4)
     *                                                       (c d)   (3 4)= (c1+d3, c2+d4)
     * @param other Another matrix that will be multiplied by origin matrix
     * @return a Matrix object result of the multiplication
     */
    public Matrix multiply(Matrix other) {
        if (this.cols != other.rows){
            throw new IllegalArgumentException("Cannot Subtract Matrices of distinct dimensions");
        }
        Matrix result = new Matrix(rows, cols);
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
        Complex[] result = new Complex[rows];
        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                result[i] = result[i].add(complexData[i][j].multiply(vector[j]));
            }
        }
        return result;
    }
    /**
     * Multiplies a Matrix by a scalar number
     *
     * @param lambda: An integer number that multiplies a Matrix
     * @return a Matrix result of the multiplication of the Matrix by a scalar
     */
    public Matrix multiply(double lambda){
        Matrix result = new Matrix(rows, cols);
        int i;
        for (i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                result.data[i][j] = lambda * this.data[i][j];
            }
        }
        return result;
    }


    /**
     * Multiplies a Matrix by a scalar number
     *
     * @param lambda: A Complex number that multiplies a Matrix
     * @return a Matrix result of the multiplication of the Matrix by a Complex scalar number
     */
    public Matrix multiply(Complex lambda) {
        Matrix result = new Matrix(rows, cols);
        int i;
        for (i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.complexData[i][j] = new Complex(lambda.getRealPart() * this.data[i][j], lambda.getImaginaryPart() * this.data[i][j]);
            }
        }
        return result;
    }


    /**
     * Sets the rows to columns and vice versa
     *
     * @return : Transposed matrix (rows by columns and columns by rows)
     * @trows: InvalidT
     */
    public Matrix transpose() {
        if (this.rows <= 0 || this.cols <= 0) {
            throw new IllegalArgumentException("Matrix is empty...");
        }
        int i;
        int j;
        Matrix result = new Matrix(rows, cols);
        for (i=0;i<this.rows;i++) {
            for (j=0;j<this.cols;j++){
                result.data[j][i] = this.data[i][j];
            }
        }
        return result;
    }

    /**
     * Calculates the determinant of the matrix.
     * Uses Laplace (cofactor) expansion.
     *
     * @return The determinant of the matrix.
     * @throws IllegalStateException if the matrix is not square.
     */
    public double determinant() {
        if (rows != cols) {
            throw new IllegalStateException("Determinant can only be calculated for square matrices. Got " + rows + "x" + cols);
        }
        if (rows == 0) {
            return 1.0;
        }
        return calculateDeterminant(this.data);
    }

    /**
     * Recursive helper function to calculate the determinant.
     *
     * @param matrixData The 2D array representing the current matrix or submatrix.
     * @return The determinant of the given matrix data.
     */
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

    /**
     * Creates a submatrix by removing a certain row or column
     *
     * @param originalData The matrix data to create the submatrix
     * @param rowToRemove  The index of the row to remove
     * @param colToRemove  The index of the column to remove
     * @return A new 2D array representing the submatrix
     */
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

    /**
     * Checks if a Matrix is squared by comparing rows and cols
     *
     * @return True if rows and columns are the same number and False otherwise
     */
    public boolean isSquared(){
        return rows == cols;
    }

    /**
     * Calculates the inverse of the matrix.
     * Uses the formula: inverse(A) = (1/det(A)) * adj(A)
     * where adj(A) is the adjugate matrix (transpose of the cofactor matrix).
     *
     * @return A new Matrix object representing the inverse.
     * @throws IllegalStateException if the matrix is not square or is singular (determinant is zero).
     */
    public Matrix inverse() {
        if (rows != cols) {
            throw new IllegalStateException("Matrix must be square to calculate inverse. Got " + rows + "x" + cols);
        }
        int n = rows;
        if (n == 0) {
            throw new IllegalStateException("Cannot invert a 0x0 matrix.");
        }
        double det = determinant();
        if (Math.abs(det) < EPSILON) {
            throw new IllegalStateException("Matrix is singular (determinant is approximately zero), cannot calculate inverse.");
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
