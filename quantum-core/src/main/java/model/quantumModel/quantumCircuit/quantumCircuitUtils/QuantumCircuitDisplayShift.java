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

package model.quantumModel.quantumCircuit.quantumCircuitUtils;

import model.quantumModel.measurementDisplay.Display;
import model.quantumModel.QuantumState;
import model.quantumModel.quantumState.BellStates.PHIState.PHIminus;
import model.quantumModel.quantumState.BellStates.PHIState.PHIplus;
import model.quantumModel.quantumState.BellStates.PSIState.PSIminus;
import model.quantumModel.quantumState.BellStates.PSIState.PSIplus;
import model.quantumModel.quantumState.GreenbergHorneZeilinger.GHZState;
import model.quantumModel.quantumState.WState.WState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuantumCircuitDisplayShift {

    public List<List<Object>> shiftColumnsRight(List<List<Object>> circuit, int fromColumnIndex, int shiftAmount) {
        if (circuit == null || circuit.isEmpty()) {throw new IllegalArgumentException("Circuit cannot be null or empty");}
        if (fromColumnIndex < 0 || shiftAmount <= 0) {throw new IllegalArgumentException("fromColumnIndex and shiftAmount must be positive");}
        int rows = circuit.size();
        int originalCols = circuit.get(0).size();
        int newCols = originalCols + shiftAmount;
        List<List<Object>> result = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<Object> row = new ArrayList<>(Collections.nCopies(newCols, null));
            result.add(row);
        }
        for (int row = 0; row < rows; row++) {
            List<Object> originalRow = circuit.get(row);
            List<Object> newRow = result.get(row);
            for (int col = 0; col < fromColumnIndex && col < originalRow.size(); col++) {
                newRow.set(col, originalRow.get(col));
            }
            for (int col = fromColumnIndex; col < originalRow.size(); col++) {
                int newPosition = col + shiftAmount;
                if (newPosition < newCols) {
                    newRow.set(newPosition, originalRow.get(col));
                }
            }
        }
        return result;
    }

    public int calculateRequiredShiftAmount(Display display, List<List<Object>> circuit) {
        int maxShiftNeeded = 0;
        for (int wire = display.fromWire(); wire <= display.toWire(); wire++) {
             int shiftForThisWire = 0;
            for (int depth = display.fromDepth(); depth <= display.toDepth(); depth++) {
                if (depth < circuit.get(wire).size() && circuit.get(wire).get(depth) != null) {
                    int searchDepth = depth;
                    while (searchDepth < circuit.get(wire).size() && circuit.get(wire).get(searchDepth) != null) {
                        searchDepth++;
                    }
                    shiftForThisWire = Math.max(shiftForThisWire, searchDepth - display.fromDepth() + (display.toDepth() - display.fromDepth()));
                }
            }
            maxShiftNeeded = Math.max(maxShiftNeeded, shiftForThisWire);
        }
        return maxShiftNeeded;
    }

    public void placeDisplay(Display display, List<List<Object>> circuit) {
        ensureCircuitSize(display.toDepth() + 1, circuit);
        for (int wire = display.fromWire(); wire <= display.toWire(); wire++) {
            for (int depth = display.fromDepth(); depth <= display.toDepth(); depth++) {
                circuit.get(wire).set(depth, display.display());
            }
        }
    }

    private void ensureCircuitSize(int requiredDepth, List<List<Object>> circuit) {
        for (List<Object> wire : circuit) {
            while (wire.size() < requiredDepth) {
                wire.add(null);
            }
        }
    }

    public boolean needsShift(Display display, List<List<Object>> circuit) {
        for (int i = display.fromWire(); i <= display.toWire(); i++) {
            for (int j = display.fromDepth(); j <= display.toDepth(); j++) {
                if (circuit.get(i).get(j) != null) {
                    return true;
                }
            }
        }
        return false;
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
