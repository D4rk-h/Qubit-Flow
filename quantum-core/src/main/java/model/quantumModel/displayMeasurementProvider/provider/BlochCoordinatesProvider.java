package model.quantumModel.displayMeasurementProvider.provider;

import model.quantumModel.displayMeasurementProvider.DisplayMeasurementPort;
import model.quantumModel.displayMeasurementProvider.displayTypes.BlochSphere;
import model.quantumModel.quantumState.QuantumState;

public class BlochCoordinatesProvider implements DisplayMeasurementPort {
    @Override
    public String getDisplayType() {
        return "Bloch Coordinates";
    }

    @Override
    public Object extractData(QuantumState state) {
        // todo develop bloch coordinates calculation
        return new BlochSphere(0,0,0);
    }

    @Override
    public boolean isCompatibleWith(int qubitCount) {
        return qubitCount < 8;
    }
}
