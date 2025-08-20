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

package model.quantumModel.quantumCircuit.circuitModel;

import model.quantumModel.quantumGate.GateOperation;
import model.quantumModel.quantumState.QuantumState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CircuitLayer {
    private Set<Integer> usedQubits;
    private List<GateOperation> operations;

    public CircuitLayer() {
        this.usedQubits = new HashSet<>();
        this.operations = new ArrayList<>();
    }

    public boolean hasConflictWith(int... qubits) {
        for (int qubit : qubits) if (usedQubits.contains(qubit)) return true;
        return false;
    }

    public void executeOn(QuantumState state) {for (GateOperation operation : operations) operation.applyTo(state);}

    public void addOperation(GateOperation operation) {
        if (hasConflictWith(operation.getTargetQubits())) throw new IllegalArgumentException("Qubit conflict found");
        operations.add(operation);
        for (int qubit: operation.getTargetQubits()) usedQubits.add(qubit);
    }

    public int getOperationCount() {return operations.size();}
    public List<GateOperation> getOperations() {return operations;}
    public void setOperations(List<GateOperation> operations) {this.operations = operations;}
    public Set<Integer> getUsedQubits() {return usedQubits;}
    public void setUsedQubits(Set<Integer> usedQubits) {this.usedQubits = usedQubits;}

    @Override
    public CircuitLayer clone() {
        try {
            CircuitLayer cloned = (CircuitLayer) super.clone();
            cloned.usedQubits = new HashSet<>(this.usedQubits);
            cloned.operations = new ArrayList<>();
            for (GateOperation operation : this.operations) cloned.operations.add(operation.clone());
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < operations.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(operations.get(i).toString());
        }
        sb.append("]");
        return sb.toString();
    }
}
