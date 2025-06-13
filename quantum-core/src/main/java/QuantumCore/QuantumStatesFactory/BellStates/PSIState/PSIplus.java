package QuantumCore.QuantumStatesFactory.BellStates.PSIState;

import MathCore.Complex;
import QuantumCore.QuantumStatesFactory.BellStates.BellState;

public class PSIplus extends BellState {
    public PSIplus() {
        super(createPsiPlusAmplitudes());
    }

    private static Complex[] createPsiPlusAmplitudes(){
        Complex[] amplitudes = new Complex[4];
        double value = 1.0 / Math.sqrt(2);
        amplitudes[0] = new Complex(0, 0);
        amplitudes[1] = new Complex(value, 0);
        amplitudes[2] = new Complex(value, 0);
        amplitudes[3] = new Complex(0, 0);
        return amplitudes;
    }

}
