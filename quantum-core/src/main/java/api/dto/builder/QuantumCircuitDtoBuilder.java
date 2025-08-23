package api.dto.builder;

import api.dto.CircuitLayerDto;
import api.dto.QuantumCircuitDto;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.util.ArrayList;
import java.util.List;

public class QuantumCircuitDtoBuilder {
    private int nQubit;
    private List<CircuitLayerDto> layers;
    private int depth;
    private int totalGateCount;

    public QuantumCircuitDtoBuilder() {
        this.layers = new ArrayList<>();
        this.depth = 0;
        this.totalGateCount = 0;
    }

    public QuantumCircuitDtoBuilder nQubit(int nQubit) {
        if (nQubit < 1 || nQubit > 10) throw new IllegalArgumentException("nQubit must be between 1 and 10");
        this.nQubit = nQubit;
        return this;
    }

    public QuantumCircuitDtoBuilder layers(List<CircuitLayerDto> layers) {
        this.layers = layers != null ? new ArrayList<>(layers) : new ArrayList<>();
        return this;
    }

    public QuantumCircuitDtoBuilder addLayer(CircuitLayerDto layer) {
        if (layer != null) this.layers.add(layer);
        return this;
    }

    public QuantumCircuitDtoBuilder depth(int depth) {
        this.depth = depth;
        return this;
    }

    public QuantumCircuitDtoBuilder totalGateCount(int totalGateCount) {
        this.totalGateCount = totalGateCount;
        return this;
    }

    public QuantumCircuitDtoBuilder fromQuantumCircuit(QuantumCircuit circuit) {
        if (circuit != null) {
            return this.nQubit(circuit.getNQubits())
                    .depth(circuit.getDepth())
                    .totalGateCount(circuit.getTotalGateCount());
        }
        return this;
    }

    public QuantumCircuitDto build() {
        if (depth == 0) {
            depth = layers.size();
        }
        if (totalGateCount == 0) {
            totalGateCount = layers.stream()
                    .mapToInt(CircuitLayerDto::getOperationCount)
                    .sum();
        }
        return new QuantumCircuitDto(nQubit, layers, depth, totalGateCount);
    }

    public static QuantumCircuitDtoBuilder builder() {return new QuantumCircuitDtoBuilder();}
}