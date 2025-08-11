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

import model.quantumModel.measurementDisplay.Display;
import model.quantumModel.measurementDisplay.amplitude.Amplitude;
import model.quantumModel.measurementDisplay.amplitude.AmplitudeFormat;
import model.quantumModel.measurementDisplay.blochSphere.BlochSphereDisplay;
import model.quantumModel.measurementDisplay.blochSphere.BlochVisualizationConfig;
import model.quantumModel.measurementDisplay.chance.Chance;
import model.quantumModel.measurementDisplay.chance.ChanceFormat;
import model.quantumModel.measurementDisplay.density.Density;

public class DisplayFactory {

    public static Display createAmplitudeDisplay(int fromWire, int toWire, AmplitudeFormat format) {
        Amplitude amplitudeDisplay = new Amplitude(format);
        return new Display(amplitudeDisplay, fromWire, toWire);
    }

    public static Display createProbabilityDisplay(int fromWire, int toWire, ChanceFormat format) {
        Chance probabilityDisplay = new Chance(format);
        return new Display(probabilityDisplay, fromWire, toWire);
    }

    public static Display createDensityDisplay(int fromWire, int toWire, boolean showOnlyDiagonal) {
        Density densityDisplay = new Density(showOnlyDiagonal);
        return new Display(densityDisplay, fromWire, toWire);
    }

    public static Display createBlochSphereDisplay(int wire, BlochVisualizationConfig config) {
        if (wire < 0) throw new IllegalArgumentException("Wire index cannot be negative");
        BlochSphereDisplay blochDisplay = new BlochSphereDisplay(config);
        return new Display(blochDisplay, wire);
    }

    public static Display createDefaultDisplay(DisplayCategory category, int fromWire, int toWire) {
        return switch(category) {
            case AMPLITUDE -> createAmplitudeDisplay(fromWire, toWire, AmplitudeFormat.RECTANGULAR);
            case PROBABILITY -> createProbabilityDisplay(fromWire, toWire, ChanceFormat.PERCENTAGE);
            case DENSITY -> createDensityDisplay(fromWire, toWire, false);
            case BLOCH_SPHERE -> {
                if (fromWire != toWire) throw new IllegalArgumentException("Bloch sphere display only works with single qubits");
                yield createBlochSphereDisplay(fromWire, new BlochVisualizationConfig(true, 1.0, true, true));
            }
        };
    }
    public static Display createDefaultDisplay(DisplayCategory category, int wire) {
        return createDefaultDisplay(category, wire, wire);
    }
}
