package control.commands;

import model.quantumModel.QuantumCircuit.QuantumCircuit;
import model.quantumModel.Qubit;

import java.util.List;

public class SimulateCommand implements QuantumCommand{
    private QuantumCircuit circuit;
    private List<Qubit> qubits;
    private SimulationParameters parameters;
    private SimulationResult lastResult;

    @Override
    public CommandResult execute() {
        return null;
    }

    @Override
    public void undo() {}

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public String getDescription() {
        return "";
    }
}
