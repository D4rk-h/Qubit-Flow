package model.quantumModel.quantumState.quantumStateUtils;

import model.mathModel.Complex;
import model.quantumModel.quantumState.QuantumState;
import java.time.Instant;
import java.util.Objects;

public record MeasurementResult(
        int outcome,
        QuantumState collapsedState,
        double probability,
        Instant timestamp
) {
    public MeasurementResult {
        Objects.requireNonNull(collapsedState, "Collapsed state cannot be null");
        if (probability < 0.0 || probability > 1.0) {throw new IllegalArgumentException("Probability must be between 0 and 1; got: " + probability);}
        if (timestamp == null) {timestamp = Instant.now();}
    }

    public MeasurementResult(int outcome, QuantumState collapsedState, double probability) {
        this(outcome, collapsedState, probability, Instant.now());
    }

    public boolean isDefiniteOutcome() {return Math.abs(probability - 1.0) < Complex.EPSILON;}

    public boolean isUnlikelyOutcome() {return probability < 0.1;}

    public String getBinaryRepresentation() {
        int numQubits = collapsedState.getNumQubits();
        return String.format("%" + numQubits + "s", Integer.toBinaryString(outcome)).replace(' ', '0');
    }

    public double getInformationContent() {return probability > 0 ? -Math.log(probability) : Double.POSITIVE_INFINITY;}

    @Override
    public String toString() {
        return String.format("MeasurementResult{outcome=%s, probability=%.6f, timestamp=%s}",
                getBinaryRepresentation(), probability, timestamp);
    }
}