package model.quantumModel.QuantumStates.BellStates.PSIState;

import model.mathModel.Complex;
import model.quantumModel.QuantumStates.BellStates.BellState;

public class PSIminus extends BellState {
    public PSIminus() {
        super(createPsiMinusAmplitudes());
    }

    private static Complex[] createPsiMinusAmplitudes(){
        Complex[] amplitudes = new Complex[4];
        double value = 1.0 / Math.sqrt(2);
        amplitudes[0] = new Complex(0, 0);
        amplitudes[1] = new Complex(value, 0);
        amplitudes[2] = new Complex(-value, 0);
        amplitudes[3] = new Complex(0, 0);
        return amplitudes;
    }

}
