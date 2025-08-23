package api.dto.mapper;

import api.dto.CircuitLayerDto;
import api.dto.GateOperationDto;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumGate.GateOperation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CircuitLayerMapper {

    public static CircuitLayerDto toDto(CircuitLayer layer) {
        if (layer == null) return new CircuitLayerDto();
        List<GateOperationDto> operationsDto = layer.getOperations().stream()
                .map(GateOperationMapper::toDto)
                .collect(Collectors.toList());
        return new CircuitLayerDto(
                new HashSet<>(layer.getUsedQubits()),
                operationsDto,
                operationsDto.size()
        );
    }

    public static CircuitLayer fromDto(CircuitLayerDto dto) {
        if (dto == null) return new CircuitLayer();
        CircuitLayer layer = new CircuitLayer();
        List<GateOperation> operations = dto.getOperations().stream()
                .map(GateOperationMapper::fromDto)
                .collect(Collectors.toList());
        layer.setOperations(operations);
        layer.setUsedQubits(new HashSet<>(dto.getUsedQubits()));
        return layer;
    }

    public static List<CircuitLayerDto> toDtoList(List<CircuitLayer> layers) {
        if (layers == null) return new ArrayList<>();
        return layers.stream()
                .map(CircuitLayerMapper::toDto)
                .collect(Collectors.toList());
    }

    public static List<CircuitLayer> fromDtoList(List<CircuitLayerDto> dtos) {
        if (dtos == null) return new ArrayList<>();
        return dtos.stream()
                .map(CircuitLayerMapper::fromDto)
                .collect(Collectors.toList());
    }
}