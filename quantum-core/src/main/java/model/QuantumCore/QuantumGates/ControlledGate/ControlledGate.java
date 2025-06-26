package model.QuantumCore.QuantumGates.ControlledGate;

import model.QuantumCore.QuantumGate;
import model.QuantumCore.Qubit;

public class ControlledGate {
    private Object[] controlledGate = new Object[3];
    private Qubit[] controlQubit;
    private Qubit[] objetiveQubit;
    private QuantumGate gate;

    public ControlledGate(Qubit[] controlQubit, Qubit[] objetiveQubit, QuantumGate gate){
        Object[] conjugate = new Object[2];
        conjugate[0] = controlQubit;
        conjugate[1] = objetiveQubit;
        conjugate[2] = gate;
        setControlledGate(conjugate);
    }

    public Qubit[] getObjetiveQubit() {return objetiveQubit;}

    public void setObjetiveQubit(Qubit[] objetiveQubit) {this.objetiveQubit = objetiveQubit;}

    public Object[] getControlledGate() {
        return controlledGate;
    }

    public void setControlledGate(Object[] controlledGate) {
        this.controlledGate = controlledGate;
    }

    public Qubit[] getControlQubit() {
        return controlQubit;
    }

    public void setControlQubit(Qubit[] controlQubit) {
        this.controlQubit = controlQubit;
    }

    public QuantumGate getGate() {
        return gate;
    }

    public void setGate(QuantumGate gate) {
        this.gate = gate;
    }
}
