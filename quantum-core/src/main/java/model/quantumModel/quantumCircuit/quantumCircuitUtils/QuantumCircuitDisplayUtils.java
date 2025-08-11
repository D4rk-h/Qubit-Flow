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
import model.quantumModel.quantumState.quantumStateUtils.BasicQuantumState;

public class QuantumCircuitDisplayUtils {

    public boolean isDisplayValid(Display display, int circuitSize) {
        return display.fromWire() >= 0 && display.toWire() < circuitSize && display.fromWire() <= display.toWire();
    }

    public String getStateLabel(BasicQuantumState state) {
        if (state.toQuantumState().getNumQubits() == 1) return state.getSymbol();
        return "|ψ⟩";
    }

    public boolean hasDisplayConflict(Display display1, Display display2) {
        return !(display1.toWire() < display2.fromWire() || display2.toWire() < display1.fromWire());
    }

    public int getDisplayQubitSpan(Display display) {
        return display.toWire() - display.fromWire() + 1;
    }
}