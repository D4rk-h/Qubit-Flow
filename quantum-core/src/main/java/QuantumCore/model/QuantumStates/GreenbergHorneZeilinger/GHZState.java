package QuantumCore.model.QuantumStates.GreenbergHorneZeilinger;

import MathCore.Complex;
import QuantumCore.model.State;

public class GHZState extends State {

    public GHZState(int nQubits) {
        super(createGHZAmplitudes(nQubits), nQubits);
        if (nQubits < 2) throw new IllegalArgumentException("GHZ State requires at least 2 qubits");
    }

    public static Complex[] createGHZAmplitudes(int nQubits) {
        int n = (int) Math.pow(2, nQubits);
        Complex[] amplitudes = new Complex[n];
        double value = 1.0/Math.sqrt(2);
        for (int i=0;i<n;i++) {
            amplitudes[i] = new Complex(0, 0);
        }
        amplitudes[0] = new Complex(value, 0);
        amplitudes[n - 1] = new Complex(value, 0);
        return amplitudes;
    }
}
