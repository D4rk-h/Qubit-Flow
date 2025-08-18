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

package control.util;

import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumGate.GateOperation;
import model.quantumModel.quantumState.QuantumState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CircuitAnalyzer {

    public static GateOperation findLastGateOnQubit(QuantumCircuit circuit, int qubit) {
        List<CircuitLayer> layers = circuit.getLayers();
        for (int i = layers.size() - 1; i >= 0; i--) {
            CircuitLayer layer = layers.get(i);
            List<GateOperation> operations = layer.getOperations();
            for (int j = operations.size() - 1; j >= 0; j--) {
                GateOperation operation = operations.get(j);
                if (operationTargetsQubit(operation, qubit)) {
                    return operation;
                }
            }
        }
        return null;
    }

    public static List<GateOperation> getGatesOnQubit(QuantumCircuit circuit, int qubit) {
        List<GateOperation> result = new ArrayList<>();
        for (CircuitLayer layer : circuit.getLayers()) {
            for (GateOperation operation : layer.getOperations()) {
                if (operationTargetsQubit(operation, qubit)) {
                    result.add(operation);
                }
            }
        }
        return result;
    }

    public static int getTotalOperations(QuantumCircuit circuit) {
        return circuit.getLayers().stream()
                .mapToInt(layer -> layer.getOperations().size())
                .sum();
    }

    public static boolean canExecuteOn(QuantumCircuit circuit, QuantumState state) {
        return circuit.getNQubits() == state.getNumQubits();
    }

    public static List<GateOperation> getCompatibleOperations(
            QuantumCircuit circuit, int maxQubits) {
        return circuit.getLayers().stream()
                .flatMap(layer -> layer.getOperations().stream())
                .filter(op -> allQubitsValid(op.getTargetQubits(), maxQubits))
                .collect(Collectors.toList());
    }

    public static CircuitStats analyzeCircuit(QuantumCircuit circuit) {
        return new CircuitStats(
                circuit.getNQubits(),
                circuit.getDepth(),
                circuit.getTotalGateCount(),
                getTotalOperations(circuit),
                getLayerDistribution(circuit)
        );
    }

    private static boolean operationTargetsQubit(GateOperation operation, int qubit) {
        return Arrays.stream(operation.getTargetQubits()).anyMatch(q -> q == qubit);
    }

    private static boolean allQubitsValid(int[] qubits, int maxQubits) {
        return Arrays.stream(qubits).allMatch(q -> q < maxQubits);
    }

    private static List<Integer> getLayerDistribution(QuantumCircuit circuit) {
        return circuit.getLayers().stream()
                .map(layer -> layer.getOperations().size())
                .collect(Collectors.toList());
    }
}