package api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumGate.GateOperation;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class CircuitLayerDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("used_qubits")
    private final Set<Integer> usedQubits;
    @JsonProperty("operations")
    private final List<GateOperationDto> operations;
    @JsonProperty("operation_count")
    private final int operationCount;

    public CircuitLayerDto() {
        this.usedQubits = new HashSet<>();
        this.operations = new ArrayList<>();
        this.operationCount = 0;
    }

    @JsonCreator
    public CircuitLayerDto(
            @JsonProperty("usedQubits") Set<Integer> usedQubits,
            @JsonProperty("operations") List<GateOperationDto> operations,
            @JsonProperty("operationCount") int operationCount) {
        this.usedQubits = usedQubits != null ? new HashSet<>(usedQubits) : new HashSet<>();
        this.operations = operations != null ? new ArrayList<>(operations) : new ArrayList<>();
        this.operationCount = operationCount;
    }

    public static CircuitLayerDto from(CircuitLayer layer) {
        if (layer == null) {
            return new CircuitLayerDto();
        }
        List<GateOperationDto> operationDtos = layer.getOperations().stream()
                .map(GateOperationDto::from)
                .collect(Collectors.toList());
        return new CircuitLayerDto(
                layer.getUsedQubits(),
                operationDtos,
                layer.getOperationCount()
        );
    }

    public CircuitLayer toCircuitLayer() {
        CircuitLayer layer = new CircuitLayer();
        List<GateOperation> gateOperations = operations.stream()
                .map(GateOperationDto::toGateOperation)
                .collect(Collectors.toList());
        layer.setOperations(gateOperations);
        layer.setUsedQubits(new HashSet<>(usedQubits));
        return layer;
    }

    public Set<Integer> getUsedQubits() { return new HashSet<>(usedQubits); }
    public List<GateOperationDto> getOperations() { return new ArrayList<>(operations); }
    public int getOperationCount() { return operationCount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CircuitLayerDto that = (CircuitLayerDto) o;
        return operationCount == that.operationCount &&
                Objects.equals(usedQubits, that.usedQubits) &&
                Objects.equals(operations, that.operations);
    }

    @Override
    public int hashCode() {return Objects.hash(usedQubits, operations, operationCount);}

    @Override
    public String toString() {
        return "CircuitLayerDto{" +
                "operationCount=" + operationCount +
                ", usedQubits=" + usedQubits +
                ", operations=" + operations.size() +
                '}';
    }
}