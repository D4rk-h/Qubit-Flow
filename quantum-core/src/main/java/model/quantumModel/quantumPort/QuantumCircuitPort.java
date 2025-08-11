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

package model.quantumModel.quantumPort;

import model.quantumModel.measurementDisplay.Display;
import model.quantumModel.measurementDisplay.blochSphere.BlochSphere;
import model.quantumModel.QuantumGate;
import model.quantumModel.quantumGate.ControlledGate.ControlGate;
import model.quantumModel.quantumState.QuantumState;

import java.util.List;

public interface QuantumCircuitPort {
    void add(QuantumGate gate, int i, int j);
    void add(BlochSphere sphere, int i, int j);
    void add(QuantumState state, int i);
    void add(Display display);
    QuantumGate removeGate(int i, int j);
    List<Object> removeWire(int i);
    Display removeDisplay(Display display);

    void addControlled(ControlGate controlledGate, int i, int j);
}
