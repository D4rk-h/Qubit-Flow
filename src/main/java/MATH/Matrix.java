package MATH;

public class Matrix {
    private final double[][] data;
    private final int rows;
    private final int cols;
    /**
     * Constructs a new matrix object with a given number of rows and columns, initialized to 0
    * @param rows: The number of rows that matrix has
    * @param cols: The number of columns that matrix has
    * @throws IllegalArgumentException When rows or cols have negative values
    **/
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
     * Checks if a Matrix is squared by comparing rows and cols
     *
     * @return True if rows and columns are the same number and False otherwise
     */
    public boolean isSquared(){
        return rows == cols;
    }
}
