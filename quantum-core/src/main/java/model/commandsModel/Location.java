package model.commandsModel;

import model.quantumModel.QuantumCircuit.QuantumCircuit;

public record Location (
        QuantumCircuit circuit,
        int wire,
        int depth
)
{}
