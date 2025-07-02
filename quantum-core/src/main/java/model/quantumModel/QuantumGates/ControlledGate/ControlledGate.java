package model.quantumModel.QuantumGates.ControlledGate;

import model.quantumModel.QuantumState;

public class ControlledGate {
    private QuantumState[] control;
    private int controlQubits;
    private Object[] target;

    public ControlledGate(QuantumState[] control, int controlQubits, Object[] target) {
        this.control = control;
        this.controlQubits = controlQubits;
        this.target = target;
    }

    public void addTarget(Object newTarget){
        setTarget(new Object[]{target, newTarget});
    }

    public QuantumState[] getControl() {
        return control;
    }

    public QuantumState getControlIndex(int index) {
        return control[index];
    }

    public void setControl(QuantumState[] control) {
        this.control = control;
    }

    public Object[] getTarget() {
        return target;
    }

    public Object getTargetIndex(int index) {
        return target[index];
    }

    public void setTarget(Object[] target) {
        this.target = target;
    }

    public int getControlQubits() {
        return controlQubits;
    }

    public void setControlQubits(int controlQubits) {
        this.controlQubits = controlQubits;
    }
}