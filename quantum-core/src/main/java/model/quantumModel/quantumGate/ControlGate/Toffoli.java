package model.quantumModel.quantumGate.ControlGate;

import model.mathModel.Complex;
import model.mathModel.Matrix;

public class Toffoli {
    private final Matrix matrix = buildToffoliMatrix();
    private final String name = "Toffoli";
    private final String symbol = "[T]";
    private ControlGate firstControl;
    private ControlGate secondControl;
    private int numOfQubits = 3;

    private static Matrix buildToffoliMatrix() {
        Complex[][] toffoli = new Complex[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                toffoli[i][j] = Complex.ZERO;
            }
        }
        toffoli[0][0] = Complex.ONE;
        toffoli[1][1] = Complex.ONE;
        toffoli[2][2] = Complex.ONE;
        toffoli[3][3] = Complex.ONE;
        toffoli[4][4] = Complex.ONE;
        toffoli[5][5] = Complex.ONE;
        toffoli[7][6] = Complex.ONE;
        toffoli[6][7] = Complex.ONE;
        return new Matrix(toffoli);
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public String getName() {
        return name;
    }

    public ControlGate getFirstControl() {
        return firstControl;
    }

    public void setFirstControl(ControlGate newFirstControl) {
        this.firstControl = newFirstControl;
    }

    public ControlGate getSecondControl() {
        return secondControl;
    }

    public void setSecondControl(ControlGate newSecondControl) {
        this.secondControl = newSecondControl;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getNumOfQubits() {
        return numOfQubits;
    }

    public void setNumOfQubits(int numOfQubits) {
        this.numOfQubits = numOfQubits;
    }
}
