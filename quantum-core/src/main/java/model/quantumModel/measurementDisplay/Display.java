package model.quantumModel.measurementDisplay;
// Todo: fix display implementation on QuantumCircuit methods

public record Display(
    Object display,
    int fromWire,
    int toWire,
    int fromDepth,
    int toDepth
    )
{}