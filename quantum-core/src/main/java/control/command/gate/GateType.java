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

public enum GateType {
    HADAMARD(1, "H", "hadamard"),
    PAULI_X(1, "X", "not", "pauli-x"),
    PAULI_Y(1, "Y", "pauli-y"),
    PAULI_Z(1, "Z", "pauli-z"),
    T_GATE(1, "T", "t"),
    S_GATE(1, "S", "s"),
    RX_GATE(1, "RX", "rx"),
    RY_GATE(1, "RY", "ry"),
    RZ_GATE(1, "RZ", "rz"),
    T_DAGGER(1, "T - Dagger", "t dagger"),
    S_DAGGER(1, "S - Dagger", "s dagger"),
    X_ROOT(1, "√X", "sx"),
    U_GATE(1, "U", "u"),
    PHASE(1, "P", "phase", "p"),
    CNOT(2, "CNOT", "CX", "cx"),
    SWAP(2, "SWAP"),
    TOFFOLI(3, "TOFFOLI", "CCX", "ccx"),
    MEASUREMENT(1, "MEASUREMENT", "measurement");

    private final int requiredQubits;
    private final String[] aliases;

    GateType(int requiredQubits, String... aliases) {
        this.requiredQubits = requiredQubits;
        this.aliases = aliases;
    }

    public int getRequiredQubits() {return requiredQubits;}

    public boolean isValidForQubits(int numQubits) {return numQubits == requiredQubits;}

    public static GateType fromString(String gateString) {
        String normalized = gateString.toUpperCase().replace("-", "_");
        try {return GateType.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            for (GateType type : values()) {
                for (String alias : type.aliases) {
                    if (alias.equalsIgnoreCase(gateString)) {
                        return type;
                    }
                }
            }
            throw new IllegalArgumentException("Unknown gate type: " + gateString);
        }
    }
}