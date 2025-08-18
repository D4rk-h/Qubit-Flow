// Copyright 2025 D4rk-h
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package control;

import control.command.history.CommandHistory;
import control.command.ports.UndoableCommand;
import control.command.add.AddGateCommand;
import control.command.add.AddQubitCommand;
import control.command.exporter.CircuitExporter;
import control.command.exporter.ExportFormat;
import control.command.remover.RemoveGateCommand;
import control.command.remover.RemoveQubitCommand;
import control.command.gate.GateType;
import control.command.simulate.SimulateCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumGate.GateOperation;
import model.quantumModel.quantumGate.QuantumGate;
import model.quantumModel.quantumState.QuantumState;

public class Controller {
    private static final int MIN_QUBITS = 1;
    private static final int MAX_QUBITS = 10;

    private QuantumCircuit circuit;
    private QuantumState currentState;
    private final CommandHistory commandHistory;
    private final CircuitExporter exporter;

    public Controller(int numQubits) {
        validateQubitCount(numQubits);
        this.circuit = new QuantumCircuit(numQubits);
        this.currentState = QuantumState.zero(numQubits);
        this.commandHistory = new CommandHistory();
        this.exporter = new CircuitExporter();
        setupStateRestorer();
    }

    public void addGate(String gateType, int... qubits) {
        GateType type = GateType.fromString(gateType);
        UndoableCommand command = new AddGateCommand(circuit, type, qubits);
        commandHistory.executeCommand(command);
    }

    public void addHadamard(int qubit) {addGate("hadamard", qubit);}

    public void addPauliX(int qubit) {addGate("pauli-x", qubit);}

    public void addPauliY(int qubit) {addGate("pauli-y", qubit);}

    public void addPauliZ(int qubit) {addGate("pauli-z", qubit);}

    public void addTGate(int qubit) {addGate("t", qubit);}

    public void addSGate(int qubit) {addGate("s", qubit);}

    public void addCNOT(int control, int target) {addGate("cnot", control, target);}

    public void addSwap(int qubit1, int qubit2) {addGate("swap", qubit1, qubit2);}

    public void addToffoli(int control1, int control2, int target) {addGate("toffoli", control1, control2, target);}

    public void removeGate(GateOperation operation, int layerIndex) {
        UndoableCommand command = new RemoveGateCommand(circuit, operation, layerIndex);
        commandHistory.executeCommand(command);
    }

    public void addControlledGate(QuantumGate gate, int control, int... targets) {circuit.addControlled(gate, control, targets);}

    public void addQubit() {
        UndoableCommand command = new AddQubitCommand(circuit, currentState);
        commandHistory.executeCommand(command);
        AddQubitCommand addCmd = (AddQubitCommand) command;
        this.circuit = addCmd.getNewCircuit();
        this.currentState = addCmd.getNewState();
    }

    public void removeQubit() {
        UndoableCommand command = new RemoveQubitCommand(circuit, currentState);
        commandHistory.executeCommand(command);
        RemoveQubitCommand removeCmd = (RemoveQubitCommand) command;
        this.circuit = removeCmd.getNewCircuit();
        this.currentState = removeCmd.getNewState();
    }

    public void addQubits(int count) {
        if (count <= 0) throw new IllegalArgumentException("Count must be positive");
        for (int i = 0; i < count; i++) addQubit();
    }

    public void setQubitCount(int targetQubits) {
        validateQubitCount(targetQubits);
        int current = getQubitCount();
        if (targetQubits > current) addQubits(targetQubits - current);
        else if (targetQubits < current) for (int i = current; i > targetQubits; i--) removeQubit();
    }

    public void createNewCircuit(int numQubits) {
        validateQubitCount(numQubits);
        this.circuit = new QuantumCircuit(numQubits);
        this.currentState = QuantumState.zero(numQubits);
        this.commandHistory.clear();
    }

    public void clearCircuit() {
        this.circuit = new QuantumCircuit(circuit.getNQubits());
        this.currentState = QuantumState.zero(circuit.getNQubits());
        this.commandHistory.clear();
    }

    public void executeCircuit() {circuit.executeOn(currentState);}

    public void resetState() {this.currentState = QuantumState.zero(circuit.getNQubits());}

    public void setInitialState(QuantumState state) {
        if (state.getNumQubits() != circuit.getNQubits()) {
            throw new IllegalArgumentException(
                    "State must have same number of qubits as circuit. \n" +
                            "Expected: " + circuit.getNQubits() + ", Got: " + state.getNumQubits()
            );
        }
        this.currentState = state;
    }

    public SimulateCommand simulate() {
        SimulateCommand simulateCommand = new SimulateCommand(circuit, currentState.clone());
        simulateCommand.execute();
        QuantumState finalState = simulateCommand.getFinalState();
        if (finalState != null) this.currentState = finalState;
        return simulateCommand;
    }

    public boolean undo() {return commandHistory.undo();}

    public boolean redo() {return commandHistory.redo();}

    public boolean canUndo() {return commandHistory.canUndo();}

    public boolean canRedo() {return commandHistory.canRedo();}

    public String getLastCommandType() {return commandHistory.getLastCommandType();}

    public int getHistorySize() {return commandHistory.getUndoStackSize();}

    public void exportCircuit(ExportFormat format) {exporter.export(circuit, format);}

    public void exportToJSON() {exportCircuit(ExportFormat.JSON);}

    public void exportToQASM() {exportCircuit(ExportFormat.QASM);}

    public void exportToQISKIT() {exportCircuit(ExportFormat.QISKIT);}

    public QuantumCircuit getCircuit() {return circuit;}

    public QuantumState getCurrentState() {return currentState;}

    public int getCircuitDepth() {return circuit.getDepth();}

    public int getTotalGateCount() {return circuit.getTotalGateCount();}

    public int getQubitCount() {return circuit.getNQubits();}

    public String getCircuitInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Quantum Circuit Information:\n");
        info.append("- Qubits: ").append(getQubitCount()).append("\n");
        info.append("- Depth (layers): ").append(getCircuitDepth()).append("\n");
        info.append("- Total gates: ").append(getTotalGateCount()).append("\n");
        info.append("- History size: ").append(getHistorySize()).append("\n");
        info.append("- Last command: ").append(getLastCommandType()).append("\n");
        return info.toString();
    }

    private void validateQubitCount(int numQubits) {
        if (numQubits < MIN_QUBITS || numQubits > MAX_QUBITS) {
            throw new IllegalArgumentException(
                    "Number of qubits must be between " + MIN_QUBITS + " and " + MAX_QUBITS +
                            ", got: " + numQubits
            );
        }
    }

    private void setupStateRestorer() {
        this.commandHistory.setStateRestorer((circuitObj, stateObj) -> {
            this.circuit = (QuantumCircuit) circuitObj;
            this.currentState = (QuantumState) stateObj;
        });
    }
}