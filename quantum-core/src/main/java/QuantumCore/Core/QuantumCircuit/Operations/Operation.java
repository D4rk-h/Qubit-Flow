package QuantumCore.Core.QuantumCircuit.Operations;

import QuantumCore.Core.QuantumCircuit.QuantumCircuit;

public abstract class Operation {
    public abstract <T> QuantumCircuit add(T element);
    public abstract <T> QuantumCircuit delete(T element);
}
