package QuantumCore.model;

public record MeasurementReport (
        double measure,
        State collapsedState
){
}
