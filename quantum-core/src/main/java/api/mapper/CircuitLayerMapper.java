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

package api.mapper;

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