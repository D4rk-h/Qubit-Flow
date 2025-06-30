package model.quantumModel;
import model.mathModel.Complex;
import model.quantumModel.QuantumPorts.QubitPort;
import java.util.Random;

public class Qubit implements QubitPort {
    private State state;

    public Qubit() {
        this.state = new State(1);
    }

    public Qubit(Complex alpha, Complex beta) {
        this.state = new State(new Complex[]{alpha, beta}, 1);
    }

    public Complex getAlpha() { return state.alpha(); }
    public Complex getBeta() { return state.beta(); }
    public State getState() { return state; }

    @Override
    public void applyGate(QuantumGate gate) {
        this.state = gate.apply(this.state);
    }

    @Override
    public int measure() {
        Random random = new Random();
        double P0 = state.alpha().magnitude() * state.alpha().magnitude();
        double random_double = random.nextDouble(0.0, 1.0);
        if (random_double < P0) {
            this.state = new State(new Complex(1.0, 0.0), new Complex(0.0, 0.0));
            return 0;
        } else {
            this.state = new State(new Complex(0.0, 0.0), new Complex(1.0, 0.0));
            return 1;
        }
    }
}
