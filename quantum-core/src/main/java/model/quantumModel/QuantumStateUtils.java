package model.quantumModel;
import model.mathModel.Complex;

public class QuantumStateUtils {
    private static final double EPSILON = 1e-10;

    public boolean isNormalized(Complex[] amplitudes, QuantumState quantumState) {
        double sum = 0.0;
        for (Complex amplitude : amplitudes) {
            sum += amplitude.magnitude() * amplitude.magnitude();
        }
        return Math.abs(sum - 1.0) < EPSILON;
    }

    public QuantumState tensorProduct(QuantumState first, QuantumState second) {
        int newNumQubits = first.getNumQubits() + second.getNumQubits();
        int newSize = (int) Math.pow(2, newNumQubits);
        Complex[] newAmplitudes = new Complex[newSize];
        int index = 0;
        for (int i = 0; i < first.getAmplitudes().length; i++) {
            for (int j = 0; j < second.getAmplitudes().length; j++) {
                newAmplitudes[index] = first.getAmplitudes()[i].multiply(second.getAmplitudes()[j]);
                index ++;
            }
        }
        return new QuantumState(newAmplitudes, newNumQubits);
    }

    public void validateNormalization(Complex[] amplitudes,  QuantumState quantumState) {
        if (!isNormalized(amplitudes, quantumState)) {
            throw new IllegalArgumentException("State is not normalized");
        }
    }
}
