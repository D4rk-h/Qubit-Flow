package model.QuantumCore;

public record MeasurementReport (
        double measure,
        State collapsedState
){
}
