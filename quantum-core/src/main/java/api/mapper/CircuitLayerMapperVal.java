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