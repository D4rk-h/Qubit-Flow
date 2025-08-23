package api.service;

import api.dto.CircuitLayerDto;
import api.dto.GateOperationDto;
import api.dto.QuantumCircuitDto;
import api.dto.mapper.CircuitLayerMapper;
import api.dto.mapper.GateOperationMapper;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import model.quantumModel.quantumGate.GateOperation;

import java.util.List;
import java.util.stream.Collectors;

public class CircuitService {

    public CircuitLayer createNewLayer() {return new CircuitLayer();}

    public void addGateOperation(CircuitLayer layer, GateOperation operation) {
        if (layer.hasConflictWith(operation.getTargetQubits())) {
            throw new IllegalArgumentException("Qubit conflict detected");
        }
        layer.addOperation(operation);
    }

    public static CircuitLayerDto toDto(CircuitLayer layer) {
        return CircuitLayerMapper.toDto(layer);
    }

    public static QuantumCircuitDto toDto(QuantumCircuit circuit) {
        if (circuit == null) {return new QuantumCircuitDto();}
        List<CircuitLayerDto> layersDto = circuit.getLayers().stream()
                .map(CircuitLayerMapper::toDto)
                .collect(Collectors.toList());
        return new QuantumCircuitDto(circuit.getNQubits(), layersDto, circuit.getDepth(), circuit.getTotalGateCount() );
    }

    public static QuantumCircuit fromDto(QuantumCircuitDto dto) {
        if (dto == null) throw new IllegalArgumentException("QuantumCircuitDto cannot be null");
        QuantumCircuit circuit = new QuantumCircuit(dto.getNQubit());
        List<CircuitLayer> layers = dto.getLayers().stream()
                .map(CircuitLayerMapper::fromDto)
                .collect(Collectors.toList());
        circuit.setLayers(layers);
        return circuit;
    }

    public static CircuitLayer fromDto(CircuitLayerDto dto) {return CircuitLayerMapper.fromDto(dto);}

    public static GateOperation fromDto(GateOperationDto dto) {return GateOperationMapper.fromDto(dto);}


    public boolean validateCircuit(QuantumCircuit circuit) {
        if (circuit == null) throw new IllegalArgumentException("Circuit cannot be null");
        if (circuit.getNQubits() < 1 || circuit.getNQubits() > 10) throw new IllegalStateException("Circuit must have between 1 and 10 qubits");
        for (CircuitLayer layer : circuit.getLayers()) {
            for (GateOperation operation : layer.getOperations()) {
                for (int qubit : operation.getTargetQubits()) {
                    if (qubit < 0 || qubit >= circuit.getNQubits()) {
                        throw new IllegalStateException("Qubit index " + qubit + " out of bounds for " + circuit.getNQubits() + "-qubit circuit");
                    }
                }
            }
        }
        return true;
    }

    public CircuitStats getCircuitStats(QuantumCircuit circuit) {
        if (circuit == null) {return new CircuitStats(0, 0, 0, List.of());}
        int depth = circuit.getDepth();
        int totalGates = circuit.getTotalGateCount();
        int qubits = circuit.getNQubits();
        List<String> gateTypes = circuit.getLayers().stream()
                .flatMap(layer -> layer.getOperations().stream())
                .map(op -> op.getGate().getName())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        return new CircuitStats(qubits, depth, totalGates, gateTypes);
    }
}