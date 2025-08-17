package model.quantumModel.displayMeasurementProvider.provider;

import model.quantumModel.displayMeasurementProvider.DisplayMeasurementPort;
import model.quantumModel.displayMeasurementProvider.displayTypes.BlochSphere;
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
