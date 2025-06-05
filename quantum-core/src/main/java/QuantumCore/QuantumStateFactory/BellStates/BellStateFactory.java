package QuantumCore.QuantumStateFactory.BellStates;
import QuantumCore.QuantumStateFactory.BellStates.PHIState.PHIminus;
import QuantumCore.QuantumStateFactory.BellStates.PHIState.PHIplus;
import QuantumCore.QuantumStateFactory.BellStates.PSIState.PSIminus;
import QuantumCore.QuantumStateFactory.BellStates.PSIState.PSIplus;

public class BellStateFactory {
    private enum BellStateType {
        PHI_PLUS,
        PHI_MINUS,
        PSI_MINUS,
        PSI_PLUS
    }

    public BellState create(BellStateType type) {
        switch(type) {
            case PHI_PLUS: return new PHIplus();
            case PHI_MINUS: return new PHIminus();
            case PSI_MINUS: return new PSIminus();
            case PSI_PLUS: return new PSIplus();
            default: throw new IllegalArgumentException("Invalid Bell State");
        }
    }
}
