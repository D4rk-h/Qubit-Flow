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

package control.command.gate;

import model.quantumModel.quantumGate.QuantumGate;
import model.quantumModel.quantumGate.QuantumGates;

public class GateFactory {
    public static QuantumGate createGate(GateType gateType) {
        return switch (gateType) {
            case HADAMARD -> QuantumGates.hadamard();
            case PAULI_X -> QuantumGates.not();
            case PAULI_Y -> QuantumGates.y();
            case PAULI_Z -> QuantumGates.z();
            case T_GATE -> QuantumGates.t();
            case S_GATE -> QuantumGates.s();
            case CNOT -> QuantumGates.cnot();
            case SWAP -> QuantumGates.swap();
            case TOFFOLI -> QuantumGates.toffoli();
            case MEASUREMENT -> QuantumGates.measurement();
        };
    }
}