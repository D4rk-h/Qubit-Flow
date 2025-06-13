package QuantumCore.QuantumStatesFactory.BellStates.PHIState;

import MathCore.Complex;
import QuantumCore.QuantumStatesFactory.BellStates.BellState;

public class PHIplus extends BellState {
    public PHIplus() {
        super(createPhiPlusAmplitudes());
    }

    private static Complex[] createPhiPlusAmplitudes(){
        Complex[] amplitudes = new Complex[4];
        double value = 1.0 / Math.sqrt(2);
        amplitudes[0] = new Complex(value, 0);
        amplitudes[1] = new Complex(0, 0);
        amplitudes[2] = new Complex(0, 0);
        amplitudes[3] = new Complex(value, 0);
        return amplitudes;
    }

}
