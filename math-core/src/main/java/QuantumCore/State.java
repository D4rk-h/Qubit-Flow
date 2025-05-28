package QuantumCore;
import MathCore.Complex;

public class State {
    private Complex alpha;
    private Complex beta;

    public State(Complex alpha, Complex beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    public Complex beta() {
        return this.beta;
    }
    public Complex alpha() {
        return this.alpha;
    }
    public void setBeta(Complex beta) {
        this.beta = beta;
    }
    public void setAlpha(Complex alpha) {
        this.alpha = alpha;
    }
}
