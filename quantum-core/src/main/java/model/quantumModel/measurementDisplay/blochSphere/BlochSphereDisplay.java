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

import model.mathModel.Complex;
import model.quantumModel.quantumState.QuantumState;
import model.quantumModel.measurementDisplay.displayUtils.DisplayCategory;
import model.quantumModel.measurementDisplay.displayUtils.DisplayPort;

public class BlochSphereDisplay implements DisplayPort {
        private final QuantumState targetState;
        private final BlochVisualizationConfig config;

        public BlochSphereDisplay(QuantumState state, BlochVisualizationConfig config) {
            this.targetState = state;
            this.config = config;
        }

        @Override
        public String getDisplaySymbol() {
            return DisplayCategory.BLOCH_SPHERE.getSymbol();
        }

        @Override
        public String getDisplayName() {
            return "Bloch Sphere Display";
        }

        @Override
        public Object renderContent() {
            if (isCompatibleWith(targetState)) {throw new IllegalArgumentException("Bloch Sphere doesn't support Multi-qubit state");}
            BlochSpace coordinates = calculateBlochCoordinates(targetState);
            return new BlochSphere(
                    new BlochSpace(coordinates.x(), coordinates.y(), coordinates.z()),
                    1.0,
                    targetState,
                    null,
                    new BlochVisualizationConfig(config.isAnimated(), config.animationDuration(), true, true),
                    null
            );
        }

    @Override
    public boolean isCompatibleWith(QuantumState state) {
        return state.getNumQubits() != 1;
    }

    private BlochSpace calculateBlochCoordinates(QuantumState state) {
            Complex alpha = state.getAlpha();
            Complex beta = state.getBeta();
            double x = 2 * (alpha.getRealPart() * beta.getRealPart() + alpha.getImaginaryPart() * beta.getImaginaryPart());
            double y = 2 * (alpha.getRealPart() * beta.getImaginaryPart() - alpha.getImaginaryPart() * beta.getRealPart());
            double z = alpha.magnitudeSquared() - beta.magnitudeSquared();
            return new BlochSpace(x, y, z);
        }
}
