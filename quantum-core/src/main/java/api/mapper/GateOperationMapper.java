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
        switch (gateName) {
            case "Hadamard":
                return QuantumGates.hadamard();
            case "NOT (Pauli-X)":
                return QuantumGates.not();
            case "Pauli-Y":
                return QuantumGates.y();
            case "Pauli-Z":
                return QuantumGates.z();
            case "T (π/8)":
                return QuantumGates.t();
            case "Phase":
                return QuantumGates.phase();
            case "CNOT":
                return QuantumGates.cnot();
            case "SWAP":
                return QuantumGates.swap();
            case "Toffoli":
                return QuantumGates.toffoli();
            default:
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
