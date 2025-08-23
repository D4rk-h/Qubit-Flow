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

package api.utils;

import api.dto.CircuitLayerDto;
import api.dto.GateOperationDto;
import api.dto.QuantumCircuitDto;
import api.dto.builder.QuantumCircuitDtoBuilder;

import java.util.ArrayList;
import java.util.List;

public class QuantumCircuitDtoUtils {

    public static boolean isValid(QuantumCircuitDto dto) {
        if (dto == null) return false;
        if (dto.getNQubit() < 1 || dto.getNQubit() > 10) return false;
        if (dto.getDepth() != dto.getLayers().size()) return false;
        int calculatedGateCount = dto.getLayers().stream()
                .mapToInt(CircuitLayerDto::getOperationCount)
                .sum();
        return dto.getTotalGateCount() == calculatedGateCount;
    }

    public static QuantumCircuitDto createEmpty(int nQubits) {
        return QuantumCircuitDtoBuilder.builder()
                .nQubit(nQubits)
                .build();
    }

    public static QuantumCircuitDto merge(QuantumCircuitDto first, QuantumCircuitDto second) {
        if (first == null) return second;
        if (second == null) return first;
        if (first.getNQubit() != second.getNQubit()) {
            throw new IllegalArgumentException("Cannot merge circuits with different qubit counts");
        }
        List<CircuitLayerDto> mergedLayers = new ArrayList<>(first.getLayers());
        mergedLayers.addAll(second.getLayers());
        return QuantumCircuitDtoBuilder.builder()
                .nQubit(first.getNQubit())
                .layers(mergedLayers)
                .build();
    }

    public static CircuitSummary getSummary(QuantumCircuitDto dto) {
        if (dto == null) {
            return new CircuitSummary(0, 0, 0, new ArrayList<>());
        }

        List<String> gateTypes = dto.getLayers().stream()
                .flatMap(layer -> layer.getOperations().stream())
                .map(GateOperationDto::getGateName)
                .distinct()
                .sorted()
                .toList();

        return new CircuitSummary(
                dto.getNQubit(),
                dto.getDepth(),
                dto.getTotalGateCount(),
                gateTypes
        );
    }
}