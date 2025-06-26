package QuantumCore;

import MathCore.Complex;
import QuantumCore.model.QuantumGate;
import QuantumCore.model.Qubit;
import QuantumCore.model.State;
import QuantumCore.model.QuantumGates.TGate;

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
