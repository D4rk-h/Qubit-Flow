package QuantumCore.QuantumStatesFactory.BellStates;

import QuantumCore.Core.State;
import MathCore.Complex;

public class BellState extends State {
    public BellState(Complex[] amplitudes) {
        super(amplitudes, 2);
    }

    public Complex[] createAmplitude(boolean beta00, boolean beta01, boolean beta10, boolean beta11){
        Complex[] amplitudes = new Complex[4];
        double value = 1/Math.sqrt(2);
        amplitudes[0] = beta00 ? new Complex(value, 0): new Complex(0,0);
        amplitudes[1] = beta01 ? new Complex(value, 0): new Complex(0,0);
        amplitudes[2] = beta10 ? new Complex(value, 0): new Complex(0,0);
        amplitudes[3] = beta11 ? new Complex(value, 0): new Complex(0,0);
        return amplitudes;
    }
}
