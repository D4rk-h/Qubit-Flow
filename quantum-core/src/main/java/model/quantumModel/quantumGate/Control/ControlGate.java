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

package model.quantumModel.quantumGate.Control;

import model.quantumModel.quantumState.QuantumState;

public class ControlGate {
    private QuantumState targetState;
    private boolean activation;
    private Object targetGate;
    private final String symbol = "●";

    public ControlGate(QuantumState targetState, Object targetGate) {
        this.targetState = targetState;
        this.targetGate = targetGate;
    }

    public boolean activateControl(int numOfQubits) {
        return targetState.equals(QuantumState.one(numOfQubits));
    }

    public QuantumState getTargetState() {
        return targetState;
    }

    public void setTargetState(QuantumState targetState) {
        this.targetState = targetState;
    }

    public boolean isActivation() {
        return activation;
    }

    public void setActivation(boolean activation) {
        this.activation = activation;
    }

    public Object getTargetGate() {
        return targetGate;
    }

    public void setTargetGate(Object targetGate) {
        this.targetGate = targetGate;
    }

    public String getSymbol() {
        return symbol;
    }
}
