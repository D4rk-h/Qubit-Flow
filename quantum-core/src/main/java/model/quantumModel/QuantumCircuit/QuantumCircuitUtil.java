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

import model.commandsModel.Display;
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

    public void seekAndExtend(List<List<Object>> circuit, Display display) {
        // Todo develop a method that pushes all 1 column to right when adding something on a position were is already something
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
