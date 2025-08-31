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

import api.dto.GateOperationDto;
import api.dto.QuantumGateDto;
import model.quantumModel.quantumGate.GateOperation;
import model.quantumModel.quantumGate.QuantumGate;
import model.quantumModel.quantumGate.QuantumGates;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GateOperationMapper {

    public static GateOperationDto toDto(GateOperation operation) {
        if (operation == null) {return new GateOperationDto("", null, new int[0], "");}
        return new GateOperationDto(
                operation.getGate().getName(),
                QuantumGateDto.from(operation.getGate()),
                operation.getTargetQubits().clone(),
                operation.toString()
        );
    }

    public static GateOperation fromDto(GateOperationDto dto) {
        if (dto == null) throw new IllegalArgumentException("GateOperationDto cannot be null");
        QuantumGate gate = createGateFromName(dto.getGateName());
        return new GateOperation(gate, dto.getTargetQubits());
    }

    private static QuantumGate createGateFromName(String gateName) {
        if (gateName.contains("Hadamard")) {
            return QuantumGates.hadamard();
        } else if (gateName.contains("NOT") || gateName.contains("Pauli-X")) {
            return QuantumGates.not();
        } else if (gateName.contains("Pauli-Y")) {
            return QuantumGates.y();
        } else if (gateName.contains("Pauli-Z")) {
            return QuantumGates.z();
        } else if (gateName.contains("T Dagger") || gateName.contains("-π/8")) {
            return QuantumGates.tDagger();
        } else if (gateName.contains("S")) {
            return QuantumGates.s();
        } else if (gateName.contains("T") && gateName.contains("π/8")) {
            return QuantumGates.t();
        } else if (gateName.contains("S Dagger")) {
            return QuantumGates.sDagger();
        } else if (gateName.contains("P")) {
            return QuantumGates.phase();
        } else if (gateName.contains("CNOT")) {
            return QuantumGates.cnot();
        } else if (gateName.contains("SWAP")) {
            return QuantumGates.swap();
        } else if (gateName.contains("Toffoli")) {
            return QuantumGates.toffoli();
        } else if (gateName.contains("RX")) {
            return QuantumGates.rx();
        } else if (gateName.contains("RY")) {
            return QuantumGates.ry();
        } else if (gateName.contains("RZ")) {
            return QuantumGates.rz();
        } else if (gateName.contains("√X")) {
            return QuantumGates.xRoot();
        } else if (gateName.contains("U")) {
            return QuantumGates.u();
        } else {
            throw new IllegalArgumentException("Unknown gate: " + gateName);
        }
    }

    public static List<GateOperationDto> toDtoList(List<GateOperation> operations) {
        if (operations == null) return new ArrayList<>();
        return operations.stream()
                .map(GateOperationMapper::toDto)
                .collect(Collectors.toList());
    }

    public static List<GateOperation> fromDtoList(List<GateOperationDto> dtos) {
        if (dtos == null) return new ArrayList<>();
        return dtos.stream()
                .map(GateOperationMapper::fromDto)
                .collect(Collectors.toList());
    }
}
