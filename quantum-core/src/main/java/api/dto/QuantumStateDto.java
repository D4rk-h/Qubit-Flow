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

package api.dto;

import model.mathModel.Complex;
import model.quantumModel.quantumState.QuantumState;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class QuantumStateDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("amplitudes")
    private final ComplexDto[] amplitudes;
    @JsonProperty("numQubits")
    private final int numQubits;
    @JsonProperty("normalized")
    private final boolean normalized;

    public QuantumStateDto() {
        this.amplitudes = new ComplexDto[0];
        this.numQubits = 0;
        this.normalized = false;
    }

    @JsonCreator
    public QuantumStateDto(
            @JsonProperty("amplitudes") ComplexDto[] amplitudes,
            @JsonProperty("numQubits") int numQubits,
            @JsonProperty("normalized") boolean normalized) {
        this.amplitudes = amplitudes != null ? amplitudes.clone() : new ComplexDto[0];
        this.numQubits = numQubits;
        this.normalized = normalized;
    }

    public static QuantumStateDto from(QuantumState quantumState) {
        if (quantumState == null) return new QuantumStateDto();
        Complex[] sourceAmplitudes = quantumState.getAmplitudes();
        ComplexDto[] dtoAmplitudes = new ComplexDto[sourceAmplitudes.length];
        for (int i = 0; i < sourceAmplitudes.length; i++) dtoAmplitudes[i] = ComplexDto.from(sourceAmplitudes[i]);
        return new QuantumStateDto(
                dtoAmplitudes,
                quantumState.getNumQubits(),
                quantumState.isNormalized()
        );
    }

    public QuantumState toQuantumState() {
        Complex[] complexAmplitudes = new Complex[amplitudes.length];
        for (int i = 0; i < amplitudes.length; i++) complexAmplitudes[i] = amplitudes[i].toComplex();
        return new QuantumState(complexAmplitudes, numQubits);
    }

    public ComplexDto[] getAmplitudes() {return amplitudes.clone();}

    public int getNumQubits() {return numQubits;}

    public boolean isNormalized() {return normalized;}

    public int getDimension() {return amplitudes.length;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantumStateDto that = (QuantumStateDto) o;
        return numQubits == that.numQubits &&
                normalized == that.normalized &&
                Arrays.equals(amplitudes, that.amplitudes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(numQubits, normalized);
        result = 31 * result + Arrays.hashCode(amplitudes);
        return result;
    }

    @Override
    public String toString() {
        return "QuantumStateDTO{" +
                "numQubits=" + numQubits +
                ", normalized=" + normalized +
                ", dimension=" + amplitudes.length +
                ", amplitudes=" + Arrays.toString(amplitudes) +
                '}';
    }
}
