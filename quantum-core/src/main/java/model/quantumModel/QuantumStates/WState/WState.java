package model.quantumModel.QuantumStates.WState;

import model.mathModel.Complex;
import model.quantumModel.State;

public class WState extends State {

    public WState(int nQubits) {
        super(createWStateAmplitudes(nQubits), nQubits);
        if (nQubits < 2) throw new IllegalArgumentException("W State requires at least 2 qubits");
    }

    private static Complex[] createWStateAmplitudes(int nQubits) {
        int size = (int) Math.pow(2, nQubits);
        Complex[] amplitudes = new Complex[size];
        double value = 1.0 / Math.sqrt(nQubits);
        for (int i = 0; i < size; i++) {
            amplitudes[i] = new Complex(0, 0);
        }
        for (int i = 0; i < nQubits; i++) {
            int index = 1 << i;
            amplitudes[index] = new Complex(value, 0);
        }
        return amplitudes;
    }
}
