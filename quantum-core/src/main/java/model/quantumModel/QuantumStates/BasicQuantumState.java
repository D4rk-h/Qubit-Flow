package model.quantumModel.QuantumStates;

import model.mathModel.Complex;
import model.quantumModel.QuantumState;

public enum BasicQuantumState {
    ZERO(new Complex(1,0), new Complex(0,0), "|0⟩"),
    ONE(new Complex(0,0), new Complex(1,0), "|1⟩"),
    PLUS(new Complex(1/Math.sqrt(2),0), new Complex(1/Math.sqrt(2),0), "|+⟩"),
    MINUS(new Complex(1/Math.sqrt(2),0), new Complex(-1/Math.sqrt(2),0), "|-⟩"),
    I_STATE(new Complex(1/Math.sqrt(2),0), new Complex(0,1/Math.sqrt(2)), "|i⟩"),
    MINUS_I_STATE(new Complex(1/Math.sqrt(2),0), new Complex(0,-1/Math.sqrt(2)), "|-i⟩");

    private final Complex alpha, beta;
    private final String symbol;

    BasicQuantumState(Complex alpha, Complex beta, String symbol) {
        this.alpha = alpha;
        this.beta = beta;
        this.symbol = symbol;
    }

    public QuantumState toQuantumState() {
        return new QuantumState(alpha, beta);
    }

    public String getSymbol() { return symbol; }
}
