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