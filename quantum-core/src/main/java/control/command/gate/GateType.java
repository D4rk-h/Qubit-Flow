package control.command.gate;

/**
 * Enum representing different quantum gate types
 * Follows Open/Closed Principle - easy to extend
 */
public enum GateType {
    HADAMARD(1, "H", "hadamard"),
    PAULI_X(1, "X", "not", "pauli-x"),
    PAULI_Y(1, "Y", "pauli-y"),
    PAULI_Z(1, "Z", "pauli-z"),
    T_GATE(1, "T"),
    S_GATE(1, "S", "phase"),
    CNOT(2, "CNOT", "CX", "cx"),
    SWAP(2, "SWAP"),
    TOFFOLI(3, "TOFFOLI", "CCX", "ccx");

    private final int requiredQubits;
    private final String[] aliases;

    GateType(int requiredQubits, String... aliases) {
        this.requiredQubits = requiredQubits;
        this.aliases = aliases;
    }

    public int getRequiredQubits() {
        return requiredQubits;
    }

    public boolean isValidForQubits(int numQubits) {
        return numQubits == requiredQubits;
    }

    public static GateType fromString(String gateString) {
        String normalized = gateString.toUpperCase().replace("-", "_");

        // Try direct enum match first
        try {
            return GateType.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            // Try alias matching
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