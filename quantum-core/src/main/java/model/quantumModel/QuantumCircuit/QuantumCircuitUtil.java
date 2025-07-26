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

package model.quantumModel.QuantumCircuit;

import model.quantumModel.QuantumGate;
import model.quantumModel.QuantumGates.HadamardGate;
import model.quantumModel.QuantumGates.PauliXGate;
import model.quantumModel.QuantumGates.PauliYGate;
import model.quantumModel.QuantumGates.PauliZGate;
import model.quantumModel.QuantumState;
import model.quantumModel.QuantumStates.BellStates.PHIState.PHIminus;
import model.quantumModel.QuantumStates.BellStates.PHIState.PHIplus;
import model.quantumModel.QuantumStates.BellStates.PSIState.PSIminus;
import model.quantumModel.QuantumStates.BellStates.PSIState.PSIplus;
import model.quantumModel.QuantumStates.GreenbergHorneZeilinger.GHZState;
import model.quantumModel.QuantumStates.WState.WState;

import java.util.ArrayList;
import java.util.List;

public class QuantumCircuitUtil {
    public void extend(List<List<Object>> circuit, int i, int j) {
        while (circuit.size() <= i) {
            circuit.add(new ArrayList<>());
        }
        while (circuit.get(i).size() <= j) {
            circuit.get(i).add(null);
        }
    }

    public void seekToMergeZ(List<Object> wire) {
        for (int i=0; i<wire.size();i++) {
            if (wire.get(i) instanceof QuantumGate){
                if (wire.get(i) instanceof HadamardGate && wire.get(i + 1) instanceof PauliXGate && wire.get(i + 2) instanceof HadamardGate) {
                    wire.set(i, new PauliZGate());
                    wire.set(i + 1, null);
                    wire.set(i + 2, null);
                }
            }
        }
    }

    public void seekToMergeMinusY(List<Object> wire) {
        for (int i=0; i<wire.size();i++) {
            if (wire.get(i) instanceof QuantumGate){
                if (wire.get(i) instanceof HadamardGate && wire.get(i + 1) instanceof PauliYGate && wire.get(i + 2) instanceof HadamardGate) {
                    wire.set(i, new PauliYGate().getMatrix().multiply(-1));
                    wire.set(i + 1, null);
                    wire.set(i + 2, null);
                }
            }
        }
    }

    public void seekToMergeX(List<Object> wire) {
        for (int i=0; i<wire.size();i++) {
            if (wire.get(i) instanceof QuantumGate){
                if (wire.get(i) instanceof HadamardGate && wire.get(i + 1) instanceof PauliZGate && wire.get(i + 2) instanceof HadamardGate) {
                    wire.set(i, new PauliXGate());
                    wire.set(i + 1, null);
                    wire.set(i + 2, null);
                }
            }
        }
    }

    public List<List<Object>> shiftColumnsRight(List<List<Object>> circuit, int fromColumnIndex, int shiftAmount) {
        if (circuit.isEmpty()){throw new IllegalArgumentException("Empty circuit!");}
        if (fromColumnIndex < 0 || shiftAmount < 0) {throw new IllegalArgumentException("index out of bounds");}
        List<List<Object>> result = new ArrayList<>();
        for (int i = 0; i < circuit.size(); i++) {
            List<Object> row = new ArrayList<>();
            for (int j = 0; j < circuit.get(i).size(); j++) {
                row.add(null);
            }
            result.add(row);
        }
        for (int i=0;i<result.size();i++) {
            for (int j=0;j<fromColumnIndex;j++) {
                result.get(i).set(j, circuit.get(i).get(j));
            }
        }
        for (int i=0;i<result.size();i++) {
            if (result.get(i).size() < result.get(i).size() + shiftAmount) {
                extend(result, 0, shiftAmount);
            }
            for (int j=fromColumnIndex+shiftAmount;j < result.get(i).size() + shiftAmount;j++) {
                result.get(i).set(j, circuit.get(i).get(j));
            }
        }
        return result;
    }

    public String getStateLabel(QuantumState state) {
        if (state instanceof PHIplus) return "|Φ+⟩";
        if (state instanceof PHIminus) return "|Φ-⟩";
        if (state instanceof PSIplus) return "|Ψ+⟩";
        if (state instanceof PSIminus) return "|Ψ-⟩";
        if (state instanceof GHZState) return "|GHZ⟩";
        if (state instanceof WState) return "|W⟩";
        if (state.getNumQubits() == 1) return "|" + state + "⟩";
        return "|ψ⟩";
    }
}
