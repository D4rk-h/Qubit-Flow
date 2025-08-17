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

package model.quantumModel.displayMeasurementProvider.provider;

import model.quantumModel.displayMeasurementProvider.DisplayMeasurementPort;
import model.quantumModel.displayMeasurementProvider.displayTypes.DensityMatrix;
import model.quantumModel.quantumState.QuantumState;

public class DensityMatrixProvider implements DisplayMeasurementPort {
    @Override
    public String getDisplayType() {
        return "Density Matrix";
    }

    @Override
    public Object extractData(QuantumState state) {
        //todo: find the density matrix of a state
        return new DensityMatrix(null);
    }

    @Override
    public boolean isCompatibleWith(int qubitCount) {
        return qubitCount < 8;
    }
}
