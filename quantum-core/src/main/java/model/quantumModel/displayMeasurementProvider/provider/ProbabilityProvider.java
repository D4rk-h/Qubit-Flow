package model.quantumModel.displayMeasurementProvider.provider;

import model.quantumModel.displayMeasurementProvider.DisplayMeasurementPort;
import model.quantumModel.displayMeasurementProvider.displayTypes.Probability;
import model.quantumModel.quantumState.QuantumState;

public class ProbabilityProvider implements DisplayMeasurementPort {
    @Override
    public String getDisplayType() {
        return "Probability";
    }

    @Override
    public Object extractData(QuantumState state) {
        return new Probability(state.getProbabilities());
    }

    @Override
    public boolean isCompatibleWith(int qubitCount) {
        return true;
    }
}
