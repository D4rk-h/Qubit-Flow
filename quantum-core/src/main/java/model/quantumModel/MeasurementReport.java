package model.quantumModel;

public record MeasurementReport (
        double measure,
        QuantumState collapsedState
){
}
