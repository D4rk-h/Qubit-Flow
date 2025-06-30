package model.quantumModel;

public record MeasurementReport (
        double measure,
        State collapsedState
){
}
