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

package model.quantumModel.QuantumGates.ControlledGate;

import model.quantumModel.QuantumState;

public class ControlledGate {
    private QuantumState[] control;
    private int controlQubits;
    private Object[] target;

    public ControlledGate(QuantumState[] control, int controlQubits, Object[] target) {
        this.control = control;
        this.controlQubits = controlQubits;
        this.target = target;
    }

    public void addTarget(Object newTarget){
        setTarget(new Object[]{target, newTarget});
    }

    public QuantumState[] getControl() {
        return control;
    }

    public QuantumState getControlIndex(int index) {
        return control[index];
    }

    public void setControl(QuantumState[] control) {
        this.control = control;
    }

    public Object[] getTarget() {
        return target;
    }

    public Object getTargetIndex(int index) {
        return target[index];
    }

    public void setTarget(Object[] target) {
        this.target = target;
    }

    public int getControlQubits() {
        return controlQubits;
    }

    public void setControlQubits(int controlQubits) {
        this.controlQubits = controlQubits;
    }
}