package model;

import model.MathCore.Complex;
import model.QuantumCore.QuantumGate;
import model.QuantumCore.Qubit;
import model.QuantumCore.State;
import model.QuantumCore.QuantumGates.TGate;

public class Main {
    public static void main(String[] args) {
        Complex num1 = new Complex(1, 0);
        Complex num2 = new Complex(0, 0);
        State state = new State(num1, num2);
        Qubit qubit = new Qubit();
        QuantumGate gate = new TGate() {};
        qubit.setState(state);
        System.out.println(qubit.measure());
        qubit.applyGate(gate);
        System.out.println(qubit.measure());
    }
}
