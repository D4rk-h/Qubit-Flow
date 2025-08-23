package api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;

import java.io.Serializable;
import java.util.Objects;

public class QuantumGateDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("name")
    private final String name;
    @JsonProperty("numQubits")
    private final int numQubits;
    @JsonProperty("matrix")
    private final ComplexMatrixDto matrix;

    public QuantumGateDto() {
        this.name = "";
        this.numQubits = 0;
        this.matrix = new ComplexMatrixDto();
    }

    @JsonCreator
    public QuantumGateDto(
            @JsonProperty("name") String name,
            @JsonProperty("numQubits") int numQubits,
            @JsonProperty("matrix") ComplexMatrixDto matrix) {
        this.name = name != null ? name : "";
        this.numQubits = numQubits;
        this.matrix = matrix != null ? matrix : new ComplexMatrixDto();
    }

    public static QuantumGateDto from(QuantumGate gate) {
        if (gate == null) {return new QuantumGateDto();}
        return new QuantumGateDto(
                gate.getName(),
                gate.getNumQubits(),
                ComplexMatrixDto.from(gate.getMatrix())
        );
    }

    public static QuantumGate toQuantumGate(QuantumGateDto dto) {
        Matrix m = new Matrix(dto.matrix.getRows(), dto.matrix.getCols());
        for (int i=0;i<dto.matrix.getRows();i++) {
            for (int j=0;j<dto.matrix.getCols();j++) {
                m.set(i, j, dto.matrix.getElements()[i][j].toComplex());
            }
        }
        return new QuantumGate(m, dto.numQubits, dto.name);
    }

    public String getName() { return name; }
    public int getNumQubits() { return numQubits; }
    public ComplexMatrixDto getMatrix() { return matrix; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantumGateDto that = (QuantumGateDto) o;
        return numQubits == that.numQubits &&
                Objects.equals(name, that.name) &&
                Objects.equals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, numQubits, matrix);
    }

    @Override
    public String toString() {
        return "QuantumGateDto{" +
                "name='" + name + '\'' +
                ", numQubits=" + numQubits +
                '}';
    }
}
