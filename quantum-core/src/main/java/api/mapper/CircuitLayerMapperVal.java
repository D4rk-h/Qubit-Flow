package api.mapper;

import api.dto.CircuitLayerDto;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumGate.GateOperation;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

class CircuitLayerMapperVal {

    public static CircuitLayerDto toDtoWithValidation(CircuitLayer layer) {
        if (layer == null) throw new IllegalArgumentException("CircuitLayer cannot be null");
        Set<Integer> calculatedUsedQubits = layer.getOperations().stream()
                .flatMap(op -> Arrays.stream(op.getTargetQubits()).boxed())
                .collect(Collectors.toSet());
        if (!calculatedUsedQubits.equals(layer.getUsedQubits())) throw new IllegalStateException("UsedQubits don't match operation target qubits");
        return CircuitLayerMapper.toDto(layer);
    }

    public static CircuitLayer fromDtoWithValidation(CircuitLayerDto dto) {
        if (dto == null) throw new IllegalArgumentException("CircuitLayerDto cannot be null");
        if (dto.getOperations() == null) throw new IllegalArgumentException("Operations list cannot be null");
        if (dto.getUsedQubits() == null) throw new IllegalArgumentException("UsedQubits set cannot be null");
        CircuitLayer layer = CircuitLayerMapper.fromDto(dto);
        for (GateOperation operation : layer.getOperations()) {
            if (layer.hasConflictWith(operation.getTargetQubits())) throw new IllegalArgumentException("Conflicting operations detected in layer");
        }
        return layer;
    }
}