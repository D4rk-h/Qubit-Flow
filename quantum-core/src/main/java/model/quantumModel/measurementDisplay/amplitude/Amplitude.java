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

package model.quantumModel.measurementDisplay.amplitude;

import model.mathModel.Complex;
import model.quantumModel.quantumState.QuantumState;
import model.quantumModel.measurementDisplay.displayUtils.DisplayCategory;
import model.quantumModel.measurementDisplay.displayUtils.DisplayPort;

public class Amplitude implements DisplayPort {
    private final QuantumState targetState;
    private final boolean showPhase;
    private final AmplitudeFormat format;

    public Amplitude(QuantumState targetState, boolean showPhase, AmplitudeFormat format) {
        this.targetState = targetState;
        this.showPhase = showPhase;
        this.format = format;
    }

    @Override
    public String getDisplaySymbol() {
        return DisplayCategory.AMPLITUDE.getSymbol();
    }

    @Override
    public String getDisplayName() {
        return "Amplitude Display";
    }

    @Override
    public Object renderContent() {
        Complex[] amplitudes = targetState.getAmplitudes();
        return switch(format) {
            case RECTANGULAR -> formatRectangular(amplitudes);
            case POLAR -> formatPolar(amplitudes);
        };
    }

    @Override
    public boolean isCompatibleWith(QuantumState state) {
        return true;
    }

    private Object formatRectangular(Complex[] amplitudes) {
        //todo implementation of rect format
        return amplitudes;
    }

    private Object formatPolar(Complex[] amplitudes) {
        // todo implementation of polar format
        return amplitudes;
    }
}
