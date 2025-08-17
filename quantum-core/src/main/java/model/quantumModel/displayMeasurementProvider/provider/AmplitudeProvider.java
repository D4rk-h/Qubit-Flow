package model.quantumModel.displayMeasurementProvider.provider;

import model.quantumModel.displayMeasurementProvider.DisplayMeasurementPort;
import model.quantumModel.displayMeasurementProvider.displayTypes.Amplitude;
import model.quantumModel.quantumState.QuantumState;

public class AmplitudeProvider implements DisplayMeasurementPort {

    @Override
    public String getDisplayType() {
        return "Amplitude";
    }

    @Override
    public Object extractData(QuantumState state) {
        return new Amplitude(state.getAmplitudes());
    }

    @Override
    public boolean isCompatibleWith(int qubitCount) {
        return true;
    }
}