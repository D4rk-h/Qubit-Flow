// Copyright 2025 D4rk-h
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package model.quantumModel.measurementDisplay.blochSphere;

import model.quantumModel.quantumState.QuantumState;
import model.quantumModel.quantumState.quantumStateUtils.MeasurementResult;

public class BlochSphere {
    private final BlochSpace center;
    private final double radius;
    private QuantumState currentState;
    private BlochVisualizationConfig visualConfig;
    private QuantumState nextState = null;
    private MeasurementResult measurementReport;

    public BlochSphere(BlochSpace center, double radius, QuantumState currentState, QuantumState nextState, BlochVisualizationConfig visualConfig, MeasurementResult measurementReport) {
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

    public QuantumState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(QuantumState currentState) {
        this.currentState = currentState;
    }

    public QuantumState getNextState() {
        return nextState;
    }

    public void setNextState(QuantumState nextState) {
        this.nextState = nextState;
    }

    public MeasurementResult getMeasurementReport() {
        return measurementReport;
    }

    public void setMeasurementReport(MeasurementResult measurementReport) {
        this.measurementReport = measurementReport;
    }

    public BlochVisualizationConfig getVisualConfig() {
        return visualConfig;
    }

    public void setVisualConfig(BlochVisualizationConfig visualConfig) {
        this.visualConfig = visualConfig;
    }
}
