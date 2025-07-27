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

package model.quantumModel.quantumCircuit;

import model.quantumModel.measurementDisplay.blochSphere.BlochSphere;
import model.quantumModel.QuantumGate;
import model.quantumModel.quantumGate.ControlledGate.ControlledGate;
import model.quantumModel.QuantumState;
import model.quantumModel.quantumState.BasicQuantumState;

import java.util.List;
import java.util.stream.Collectors;

public class QuantumCircuitCLIDisplay {
        private static final String WIRE_SEGMENT = "———";
        private static final String CONTROL_SYMBOL = "⬤";
        private static final String EMPTY_WIRE = "   ";
        private static final QuantumCircuitCLIDisplayUtils utils = new QuantumCircuitCLIDisplayUtils();

        public String formatCircuit(QuantumCircuit circuit) {
            List<List<Object>> circuitGrid = circuit.getCircuit();
            return circuitGrid.stream()
                    .map(this::formatWire)
                    .collect(Collectors.joining("\n"));
        }

        private String formatWire(List<Object> wire) {
            StringBuilder wireBuilder = new StringBuilder();
            wireBuilder.append(formatInitialState(wire.get(0)));
            for (int position = 1; position < wire.size(); position++) {
                wireBuilder.append(formatCircuitElement(wire.get(position)));
            }
            return wireBuilder.toString();
        }

        private String formatInitialState(Object element) {
            if (element instanceof BasicQuantumState) {
                return ((BasicQuantumState) element).getSymbol();
            }
            if (element instanceof QuantumState) {
                QuantumState state = (QuantumState) element;
                if (state.getNumQubits() == 1) {
                    return singleQubitDetector(state);
                } else {
                    return "|ψ⟩";
                }
            }
            return "|0⟩";
        }

        private String formatCircuitElement(Object element) {
            if (element == null) {
                return WIRE_SEGMENT;
            }
            if (element instanceof QuantumGate) {
                return gateFormatter((QuantumGate) element);
            }
            if (element instanceof ControlledGate) {
                return CONTROL_SYMBOL;
            }
            if (element instanceof BlochSphere) {
                return "[B]";
            }
            return EMPTY_WIRE;
        }

        private String gateFormatter(QuantumGate gate) {
            return utils.formatGate(gate);
        }

        private String singleQubitDetector(QuantumState state) {
            return utils.detectSingleQubitState(state);
        }

        public String formatCircuitWithLabels(QuantumCircuit circuit) {
            StringBuilder result = new StringBuilder();
            List<List<Object>> circuitGrid = circuit.getCircuit();
            result.append("Quantum Circuit (").append(circuit.getnQubits()).append(" qubits):\n");
            result.append("═".repeat(50)).append("\n");
            for (int i = 0; i < circuitGrid.size(); i++) {
                result.append(String.format("q%d: ", i));
                result.append(formatWire(circuitGrid.get(i)));
                result.append("\n");
            }
            result.append("═".repeat(50));
            return result.toString();
        }
}
