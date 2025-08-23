package api.dto.builder;

import api.dto.GateOperationDto;
import api.dto.QuantumGateDto;
import model.quantumModel.quantumGate.GateOperation;

public class GateOperationDtoBuilder {
    private String gateName;
    private QuantumGateDto gate;
    private int[] targetQubits;
    private String operationString;

    public GateOperationDtoBuilder() {
        this.gateName = "";
        this.gate = new QuantumGateDto();
        this.targetQubits = new int[0];
        this.operationString = "";
    }

    public GateOperationDtoBuilder gateName(String gateName) {
        this.gateName = gateName != null ? gateName : "";
        return this;
    }

    public GateOperationDtoBuilder gate(QuantumGateDto gate) {
        this.gate = gate != null ? gate : new QuantumGateDto();
        return this;
    }

    public GateOperationDtoBuilder targetQubits(int... targetQubits) {
        this.targetQubits = targetQubits != null ? targetQubits.clone() : new int[0];
        return this;
    }

    public GateOperationDtoBuilder operationString(String operationString) {
        this.operationString = operationString != null ? operationString : "";
        return this;
    }

    public GateOperationDtoBuilder fromGateOperation(GateOperation operation) {
        if (operation != null) {
            return this.gateName(operation.getGate().getName())
                    .gate(QuantumGateDto.from(operation.getGate()))
                    .targetQubits(operation.getTargetQubits())
                    .operationString(operation.toString());
        }
        return this;
    }

    public GateOperationDto build() {
        if (operationString.isEmpty() && !gateName.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(gateName).append("(");
            for (int i = 0; i < targetQubits.length; i++) {
                if (i > 0) sb.append(",");
                sb.append(targetQubits[i]);
            }
            sb.append(")");
            operationString = sb.toString();
        }

        return new GateOperationDto(gateName, gate, targetQubits, operationString);
    }

    public static GateOperationDtoBuilder builder() {return new GateOperationDtoBuilder();}
}
