package api.dto.builder;

import api.dto.CircuitLayerDto;
import api.dto.GateOperationDto;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CircuitLayerDtoBuilder {
    private Set<Integer> usedQubits;
    private List<GateOperationDto> operations;
    private int operationCount;

    public CircuitLayerDtoBuilder() {
        this.usedQubits = new HashSet<>();
        this.operations = new ArrayList<>();
        this.operationCount = 0;
    }

    public CircuitLayerDtoBuilder usedQubits(Set<Integer> usedQubits) {
        this.usedQubits = usedQubits != null ? new HashSet<>(usedQubits) : new HashSet<>();
        return this;
    }

    public CircuitLayerDtoBuilder addUsedQubit(int qubit) {
        this.usedQubits.add(qubit);
        return this;
    }

    public CircuitLayerDtoBuilder operations(List<GateOperationDto> operations) {
        this.operations = operations != null ? new ArrayList<>(operations) : new ArrayList<>();
        return this;
    }

    public CircuitLayerDtoBuilder addOperation(GateOperationDto operation) {
        if (operation != null) {
            this.operations.add(operation);
            for (int qubit : operation.getTargetQubits()) {
                this.usedQubits.add(qubit);
            }
        }
        return this;
    }

    public CircuitLayerDtoBuilder operationCount(int operationCount) {
        this.operationCount = operationCount;
        return this;
    }

    public CircuitLayerDtoBuilder fromCircuitLayer(CircuitLayer layer) {
        if (layer != null) {
            return this.usedQubits(layer.getUsedQubits())
                    .operationCount(layer.getOperationCount());
        }
        return this;
    }

    public CircuitLayerDto build() {
        if (operationCount == 0) operationCount = operations.size();
        return new CircuitLayerDto(usedQubits, operations, operationCount);
    }

    public static CircuitLayerDtoBuilder builder() {return new CircuitLayerDtoBuilder();}
}