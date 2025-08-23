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

package api.utils;

import java.util.ArrayList;
import java.util.List;

public record CircuitSummary(int qubits, int depth, int gateCount, List<String> gateTypes) {
    public CircuitSummary(int qubits, int depth, int gateCount, List<String> gateTypes) {
        this.qubits = qubits;
        this.depth = depth;
        this.gateCount = gateCount;
        this.gateTypes = new ArrayList<>(gateTypes);
    }

    @Override
    public List<String> gateTypes() {return new ArrayList<>(gateTypes);}

    @Override
    public String toString() {
        return "CircuitSummary{" +
                "qubits=" + qubits +
                ", depth=" + depth +
                ", gateCount=" + gateCount +
                ", gateTypes=" + gateTypes +
                '}';
    }
}