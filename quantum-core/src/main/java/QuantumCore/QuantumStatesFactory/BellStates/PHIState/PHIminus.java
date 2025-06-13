package QuantumCore.QuantumStatesFactory.BellStates.PHIState;

import MathCore.Complex;
import QuantumCore.QuantumStatesFactory.BellStates.BellState;

public class PHIminus extends BellState {
    public PHIminus() {
        super(createPhiMinusAmplitudes());
    }

    private static Complex[] createPhiMinusAmplitudes(){
        Complex[] amplitudes = new Complex[4];
        double value = 1.0 / Math.sqrt(2);
        amplitudes[0] = new Complex(value, 0);
        amplitudes[1] = new Complex(0, 0);
        amplitudes[2] = new Complex(0, 0);
        amplitudes[3] = new Complex(-value, 0);
        return amplitudes;
    }

}
