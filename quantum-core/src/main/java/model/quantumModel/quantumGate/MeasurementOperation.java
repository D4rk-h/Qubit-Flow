package model.quantumModel.quantumGate;

import model.quantumModel.quantumState.QuantumState;
import model.quantumModel.quantumState.quantumStateUtils.MeasurementResult;

public class MeasurementOperation extends GateOperation {
    private MeasurementResult measurementResult;
    private QuantumState stateBeforeMeasurement;

    public MeasurementOperation(MeasurementGate measurementGate, int... targetQubits) {
        super(measurementGate, targetQubits);
    }

    @Override
    public void applyTo(QuantumState state) {
        if (getGate() instanceof MeasurementGate measurementGate) {
            stateBeforeMeasurement = state.clone();
            measurementResult = measurementGate.measure(state);
        } else {
            super.applyTo(state);
        }
    }

    public MeasurementResult getMeasurementResult() {
        return measurementResult;
    }

    public QuantumState getStateBeforeMeasurement() {
        return stateBeforeMeasurement != null ? stateBeforeMeasurement.clone() : null;
    }

    public boolean isMeasurement() {
        return getGate() instanceof MeasurementGate;
    }

    @Override
    public String toString() {
        if (isMeasurement()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Measure(");
            int[] qubits = getTargetQubits();
            for (int i = 0; i < qubits.length; i++) {
                if (i > 0) sb.append(",");
                sb.append(qubits[i]);
            }
            sb.append(")");
            if (measurementResult != null) {
                sb.append(" -> ").append(measurementResult.outcome());
            }
            return sb.toString();
        }
        return super.toString();
    }
}

