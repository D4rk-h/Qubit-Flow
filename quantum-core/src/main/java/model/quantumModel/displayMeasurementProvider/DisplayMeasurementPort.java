package model.quantumModel.displayMeasurementProvider;

import model.quantumModel.quantumState.QuantumState;

public interface DisplayMeasurementPort {
    String getDisplayType();
    Object extractData(QuantumState state);
    boolean isCompatibleWith(int qubitCount);
}
