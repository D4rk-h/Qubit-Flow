package api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.quantumModel.quantumGate.GateOperation;
import model.quantumModel.quantumGate.QuantumGate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class GateOperationDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("gateName")
    private final String gateName;
    @JsonProperty("gateMatrix")
    private final QuantumGateDto gate;
    @JsonProperty("targetQubits")
    private final int[] targetQubits;
    @JsonProperty("operationString")
    private final String operationString;

    public GateOperationDto() {
        this.gateName = "";
        this.gate = new QuantumGateDto();
        this.targetQubits = new int[0];
        this.operationString = "";
    }

    @JsonCreator
    public GateOperationDto(
            @JsonProperty("gateName") String gateName,
            @JsonProperty("gateMatrix") QuantumGateDto gate,
            @JsonProperty("targetQubits") int[] targetQubits,
            @JsonProperty("operationString") String operationString) {
        this.gateName = gateName != null ? gateName : "";
        this.gate = gate != null ? gate : new QuantumGateDto();
        this.targetQubits = targetQubits != null ? targetQubits.clone() : new int[0];
        this.operationString = operationString != null ? operationString : "";
    }

    public static GateOperationDto from(GateOperation operation) {
        if (operation == null) {
            return new GateOperationDto();
        }

        return new GateOperationDto(
                operation.getGate().getName(),
                QuantumGateDto.from(operation.getGate()),
                operation.getTargetQubits(),
                operation.toString()
        );
    }

    public GateOperation toGateOperation() {
        QuantumGate quantumGate = QuantumGateDto.toQuantumGate(gate);
        return new GateOperation(quantumGate, targetQubits);
    }

    public String getGateName() { return gateName; }
    public QuantumGateDto getGate() { return gate; }
    public int[] getTargetQubits() { return targetQubits.clone(); }
    public String getOperationString() { return operationString; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GateOperationDto that = (GateOperationDto) o;
        return Objects.equals(gateName, that.gateName) &&
                Objects.equals(gate, that.gate) &&
                Arrays.equals(targetQubits, that.targetQubits) &&
                Objects.equals(operationString, that.operationString);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(gateName, gate, operationString);
        result = 31 * result + Arrays.hashCode(targetQubits);
        return result;
    }

    @Override
    public String toString() {
        return "GateOperationDto{" +
                "gateName='" + gateName + '\'' +
                ", targetQubits=" + Arrays.toString(targetQubits) +
                ", operationString='" + operationString + '\'' +
                '}';
    }
}
