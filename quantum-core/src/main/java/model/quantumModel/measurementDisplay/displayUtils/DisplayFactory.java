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

package model.quantumModel.measurementDisplay.displayUtils;

import model.quantumModel.QuantumState;
import model.quantumModel.measurementDisplay.Display;
import model.quantumModel.measurementDisplay.amplitude.Amplitude;
import model.quantumModel.measurementDisplay.amplitude.AmplitudeFormat;
import model.quantumModel.measurementDisplay.blochSphere.BlochSphereDisplay;
import model.quantumModel.measurementDisplay.blochSphere.BlochVisualizationConfig;
import model.quantumModel.measurementDisplay.chance.Chance;
import model.quantumModel.measurementDisplay.chance.ChanceFormat;
import model.quantumModel.measurementDisplay.density.Density;

public class DisplayFactory {

    public static Display createAmplitudeDisplay(QuantumState state, int fromWire, int toWire, int fromDepth, int toDepth, AmplitudeFormat format) {
        Amplitude amplitudeDisplay = new Amplitude(state, true, format);
        return new Display(amplitudeDisplay, fromWire, toWire, fromDepth, toDepth);
    }

    public static Display createProbabilityDisplay(QuantumState state, int fromWire, int toWire, int fromDepth, int toDepth, ChanceFormat format) {
        Chance probabilityDisplay = new Chance(state, format);
        return new Display(probabilityDisplay, fromWire, toWire, fromDepth, toDepth);
    }

    public static Display createDensityDisplay(QuantumState state, int fromWire, int toWire, int fromDepth, int toDepth, boolean showOnlyDiagonal) {
        if (new Density(state, showOnlyDiagonal).isCompatibleWith(state)) {throw new IllegalArgumentException("Density display is only compatible with less than 6 qubits systems");}
        Density densityDisplay = new Density(state, showOnlyDiagonal);
        return new Display(densityDisplay, fromWire, toWire, fromDepth, toDepth);
    }

    public static Display createBlochSphereDisplay(QuantumState state, int fromWire, int toWire, int fromDepth, int toDepth, BlochVisualizationConfig config) {
        BlochSphereDisplay blochDisplay = new BlochSphereDisplay(state, config);
        if (blochDisplay.isCompatibleWith(state)) {throw new IllegalArgumentException("Bloch sphere display only works with single-qubit states");}
        return new Display(blochDisplay, fromWire, toWire, fromDepth, toDepth);
    }

    public static Display createDefaultDisplay(DisplayCategory category, QuantumState state, int fromWire, int toWire, int fromDepth, int toDepth) {
        return switch(category) {
            case AMPLITUDE -> createAmplitudeDisplay(state, fromWire, toWire, fromDepth, toDepth, AmplitudeFormat.RECTANGULAR);
            case PROBABILITY -> createProbabilityDisplay(state, fromWire, toWire, fromDepth, toDepth, ChanceFormat.PERCENTAGE);
            case DENSITY -> createDensityDisplay(state, fromWire, toWire, fromDepth, toDepth, false);
            case BLOCH_SPHERE -> createBlochSphereDisplay(state, fromWire, toWire, fromDepth, toDepth, new BlochVisualizationConfig(true, 1.0, true, true));
        };
    }
}
