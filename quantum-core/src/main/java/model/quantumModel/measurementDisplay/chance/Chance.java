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

package model.quantumModel.measurementDisplay.chance;

import model.quantumModel.quantumState.QuantumState;
import model.quantumModel.measurementDisplay.displayUtils.DisplayCategory;
import model.quantumModel.measurementDisplay.displayUtils.DisplayPort;

public class Chance implements DisplayPort {
    private final ChanceFormat format;

    public Chance(ChanceFormat format) {
        this.format = format;
    }

    @Override
    public String getDisplaySymbol() {
        return DisplayCategory.PROBABILITY.getSymbol();
    }

    @Override
    public String getDisplayName() {
        return "Chance Display";
    }

    @Override
    public Object renderContent(QuantumState state) {
        double[] probabilities = state.getProbabilities();
        return switch(format) {
            case PERCENTAGE -> formatPercentage(probabilities);
            case DECIMAL -> formatDecimal(probabilities);
        };
    }

    @Override
    public boolean isCompatibleWith(int qubitCount) {
        return true;
    }

    private Object formatPercentage(double[] probabilities) {
        StringBuilder sb = new StringBuilder();
        for (double prob: probabilities) {
            sb.append(Math.round(prob) + "%");
        }
        return sb;
    }

    private Object formatDecimal(double[] probabilities) {
        double[] decimalProbabilities = new double[probabilities.length];
        for (int i=0;i<probabilities.length;i++){
            decimalProbabilities[i] = Math.round(probabilities[i]);
        }
        return decimalProbabilities;
    }
}
