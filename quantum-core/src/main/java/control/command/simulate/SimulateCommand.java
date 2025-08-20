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

package control.command.simulate;

import model.mathModel.Complex;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumState.QuantumState;
import model.quantumModel.quantumState.quantumStateUtils.MeasurementResult;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


public class SimulateCommand implements SimulationCommand {
    private final QuantumCircuit circuit;
    private final QuantumState initialState;
    private final AtomicReference<QuantumState> finalState;
    private final AtomicBoolean isRunning;
    private final AtomicBoolean isPaused;
    private CompletableFuture<Void> simulationFuture;

    public SimulateCommand(QuantumCircuit circuit, QuantumState initialState) {
        this.circuit = validateCircuit(circuit);
        this.initialState = validateInitialState(initialState, circuit);
        this.finalState = new AtomicReference<>();
        this.isRunning = new AtomicBoolean(false);
        this.isPaused = new AtomicBoolean(false);
    }

    @Override
    public void execute() {
        if (isRunning.get()) throw new IllegalStateException("Simulation is already running");
        isRunning.set(true);
        isPaused.set(false);
        simulationFuture = CompletableFuture.runAsync(this::runSimulation);
    }

    @Override
    public void pause() {
        if (isRunning.get() && !isPaused.get()) {
            isPaused.set(true);
            System.out.println("Simulation paused");
        }
    }

    @Override
    public void resume() {
        if (isRunning.get() && isPaused.get()) {
            isPaused.set(false);
            System.out.println("Simulation resumed");
        }
    }

    @Override
    public void abort() {
        if (isRunning.get()) {
            isRunning.set(false);
            isPaused.set(false);
            if (simulationFuture != null) simulationFuture.cancel(true);
            System.out.println("Simulation aborted");
        }
    }

    @Override
    public boolean isRunning() {return isRunning.get();}

    @Override
    public boolean isPaused() {return isPaused.get();}

    public QuantumState getFinalState() {return finalState.get();}

    public MeasurementResult measure() {
        QuantumState state = finalState.get();
        if (state == null) throw new IllegalStateException("No simulation results available for measurement");
        return state.measure();
    }

    public Map<Integer, Integer> measureMultiple(int numMeasurements) {
        QuantumState state = finalState.get();
        if (state == null) throw new IllegalStateException("No simulation results available for measurement");
        return state.measureMultiple(numMeasurements);
    }

    private void runSimulation() {
        try {
            QuantumState state = initialState.clone();
            circuit.executeOn(state);
            finalState.set(state);
            logSimulationResults(state);
        } catch (Exception e) {
            System.err.println("Simulation error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            isRunning.set(false);
            isPaused.set(false);
        }
    }

    private void logSimulationResults(QuantumState state) {
        System.out.println("Simulation completed successfully");
        System.out.println("Final state: " + state.toString());
        double[] probabilities = state.getProbabilities();
        System.out.println("Measurement probabilities:");
        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] > Complex.EPSILON) {
                String binaryState = String.format("%" + circuit.getNQubits() + "s",
                        Integer.toBinaryString(i)).replace(' ', '0');
                System.out.printf("|%s⟩: %.2f%%\n", binaryState, probabilities[i] * 100);
            }
        }
    }

    private QuantumCircuit validateCircuit(QuantumCircuit circuit) {
        if (circuit == null) throw new IllegalArgumentException("Circuit cannot be null");
        return circuit;
    }

    private QuantumState validateInitialState(QuantumState state, QuantumCircuit circuit) {
        QuantumState validatedState = state != null ? state : QuantumState.zero(circuit.getNQubits());
        if (validatedState.getNumQubits() != circuit.getNQubits()) {
            throw new IllegalArgumentException(
                    "Initial state must have same number of qubits as circuit. \n" +
                            "Expected: " + circuit.getNQubits() + ", Got: " + validatedState.getNumQubits()
            );
        }
        return validatedState;
    }

    public void waitForCompletion() {
        if (simulationFuture != null) {
            try {
                simulationFuture.get();
            } catch (Exception e) {
                System.err.println("Error waiting for simulation: " + e.getMessage());
            }
        }
    }
}