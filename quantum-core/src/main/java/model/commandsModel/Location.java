package model.commandsModel;

import model.quantumModel.quantumCircuit.QuantumCircuit;

public record Location (
        QuantumCircuit circuit,
        int wire,
        int depth
)
{}
