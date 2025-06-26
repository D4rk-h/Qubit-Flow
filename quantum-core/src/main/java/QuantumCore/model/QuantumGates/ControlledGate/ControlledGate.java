package QuantumCore.model.QuantumGates.ControlledGate;

import QuantumCore.model.QuantumGate;
import QuantumCore.model.Qubit;

public class ControlledGate {
    private Object[] controlledGate = new Object[2];
    private Qubit controlQubit;
    private QuantumGate gate;

    public ControlledGate(Qubit controlQubit, QuantumGate gate){
        Object[] conjugate = new Object[2];
        conjugate[0] = controlQubit;
        conjugate[1] = gate;
        setControlledGate(conjugate);
    }

    public Object[] getControlledGate() {
        return controlledGate;
    }

    public void setControlledGate(Object[] controlledGate) {
        this.controlledGate = controlledGate;
    }

    public Qubit getControlQubit() {
        return controlQubit;
    }

    public void setControlQubit(Qubit controlQubit) {
        this.controlQubit = controlQubit;
    }

    public QuantumGate getGate() {
        return gate;
    }

    public void setGate(QuantumGate gate) {
        this.gate = gate;
    }
}
