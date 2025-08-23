
package api.dto;

import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumCircuit.circuitModel.CircuitLayer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuantumCircuitDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("n_qubit")
    private final int nQubit;
    @JsonProperty("layers")
    private final List<CircuitLayerDto> layers;
    @JsonProperty("depth")
    private final int depth;
    @JsonProperty("total_gate_count")
    private final int totalGateCount;

    public QuantumCircuitDto() {
        this.nQubit = 0;
        this.layers = new ArrayList<>();
        this.depth = 0;
        this.totalGateCount = 0;
    }

    @JsonCreator
    public QuantumCircuitDto(
            @JsonProperty("n_qubit") int nQubit,
            @JsonProperty("layers") List<CircuitLayerDto> layers,
            @JsonProperty("depth") int depth,
            @JsonProperty("total_gate_count") int totalGateCount) {
        this.nQubit = nQubit;
        this.layers = layers != null ? new ArrayList<>(layers) : new ArrayList<>();
        this.depth = depth;
        this.totalGateCount = totalGateCount;
    }

    public static QuantumCircuitDto from(QuantumCircuit circuit) {
        if (circuit == null) return new QuantumCircuitDto();
        List<CircuitLayerDto> layerDtos = circuit.getLayers().stream()
                .map(CircuitLayerDto::from)
                .collect(Collectors.toList());
        return new QuantumCircuitDto(
                circuit.getNQubits(),
                layerDtos,
                circuit.getDepth(),
                circuit.getTotalGateCount()
        );
    }

    public QuantumCircuit toQuantumCircuit() {
        QuantumCircuit circuit = new QuantumCircuit(nQubit);
        List<CircuitLayer> circuitLayers = layers.stream()
                .map(CircuitLayerDto::toCircuitLayer)
                .collect(Collectors.toList());
        circuit.setLayers(circuitLayers);
        return circuit;
    }

    public Stream<CircuitLayerDto> layerStream() {
        return layers.stream();
    }

    public int getNQubit() { return nQubit; }
    public List<CircuitLayerDto> getLayers() { return new ArrayList<>(layers); }
    public int getDepth() { return depth; }
    public int getTotalGateCount() { return totalGateCount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantumCircuitDto that = (QuantumCircuitDto) o;
        return nQubit == that.nQubit &&
                depth == that.depth &&
                totalGateCount == that.totalGateCount &&
                Objects.equals(layers, that.layers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nQubit, layers, depth, totalGateCount);
    }

    @Override
    public String toString() {
        return "QuantumCircuitDto{" +
                "nQubit=" + nQubit +
                ", depth=" + depth +
                ", totalGateCount=" + totalGateCount +
                ", layerCount=" + layers.size() +
                '}';
    }
}