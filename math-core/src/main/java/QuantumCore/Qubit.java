package QuantumCore;
import MathCore.Complex;
import QuantumCore.QuantumPorts.QubitPort;

import java.util.Random;

public class Qubit implements QubitPort {
    private State state;

    public Qubit() {
        this.state = new State(new Complex(1.0, 0.0), new Complex(0.0, 0.0));
    }

    public Qubit(Complex alpha, Complex beta) {
        if (Math.abs(alpha.magnitude() + beta.magnitude() - 1.0)>1e-9){
            throw new IllegalArgumentException("Amplitudes must be normalized");
        }
        state = new State(alpha, beta);
    }

    public State getState() {
        return state;
    }

    public void applyGate(QuantumGate gate) {
        state = gate.apply(this.state);
    }

    public State measure() {
        Random random = new Random();
        double pAlpha = state.alpha().magnitude() * state.alpha().magnitude();
        double random_double = random.nextDouble(0.0, 1.0);
        if (random_double < pAlpha) {
            state.setAlpha(new Complex(1.0, 0.0));
            state.setBeta(new Complex(0.0, 0.0));
        } else {
            state.setAlpha(new Complex(0.0, 0.0));
            state.setBeta(new Complex(1.0, 0.0));
        }
        return state;
    }

    public String collapse() {
        if (state.alpha().getRealPart() == 1.0) return "|0⟩";
        if (state.beta().getRealPart() == 1.0) return "|1⟩";
        return null;
    }

}
