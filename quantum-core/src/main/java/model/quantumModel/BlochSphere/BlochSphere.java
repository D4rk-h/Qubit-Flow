package model.quantumModel.BlochSphere;

import model.quantumModel.MeasurementReport;
import model.quantumModel.State;

public class BlochSphere {
    private final BlochSpace center;
    private final double radius;
    private State currentState;
    private VisualConfig visualConfig;
    private State nextState = null;
    private MeasurementReport measurementReport;

    public BlochSphere(BlochSpace center, double radius, State currentState, State nextState, VisualConfig visualConfig, MeasurementReport measurementReport) {
        this.center = center;
        this.radius = radius;
        this.currentState = currentState;
        this.nextState = nextState;
        this.visualConfig = visualConfig;
        this.measurementReport = measurementReport;
    }

    public BlochSpace getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getNextState() {
        return nextState;
    }

    public void setNextState(State nextState) {
        this.nextState = nextState;
    }

    public MeasurementReport getMeasurementReport() {
        return measurementReport;
    }

    public void setMeasurementReport(MeasurementReport measurementReport) {
        this.measurementReport = measurementReport;
    }

    public VisualConfig getVisualConfig() {
        return visualConfig;
    }

    public void setVisualConfig(VisualConfig visualConfig) {
        this.visualConfig = visualConfig;
    }
}
