package model.commandsModel;

public record Display(
        Object display,
        int fromWire,
        int toWire,
        int fromDepth,
        int toDepth
)
{}
