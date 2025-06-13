package QuantumCore.Core;
import MathCore.Complex;
import QuantumCore.QuantumPorts.QubitPort;
import java.util.Random;

public class Qubit implements QubitPort {
    private State state;

    public Qubit() {
        this.state = new State(new Complex(1.0, 0.0), new Complex(0.0, 0.0));
    }

    public Qubit(Complex alpha, Complex beta) {
        if (Math.abs(alpha.magnitude() * alpha.magnitude() + beta.magnitude() * beta.magnitude() - 1.0)>1e-9){
            throw new IllegalArgumentException("Amplitudes must be normalized");
        }
        state = new State(alpha, beta);
    }

    public State getState() {return state;}

    public void setState(State other){this.state=other;}

    @Override
    public void applyGate(QuantumGate gate) {
        state = gate.apply(this.state);
    }

    @Override
    public int measure() {
        Random random = new Random();
        double P0 = state.alpha().magnitude() * state.alpha().magnitude();
        double random_double = random.nextDouble(0.0, 1.0);
        if (random_double < P0) {
            state = new State(new Complex(1.0, 0.0), new Complex(0.0, 0.0));
            return 0;
        } else {
            state = new State(new Complex(0.0, 0.0), new Complex(1.0, 0.0));
            return 1;
        }
    }
}
