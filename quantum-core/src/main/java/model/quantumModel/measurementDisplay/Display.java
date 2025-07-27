package model.quantumModel.measurementDisplay;

public record Display(
        Object display,
        int fromWire,
        int toWire,
        int fromDepth,
        int toDepth
)
{}
//Todo: create Amplitude, Chance, and Density model classes as Display son classes.
