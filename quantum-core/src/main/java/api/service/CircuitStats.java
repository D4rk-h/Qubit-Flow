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

package api.service;

import java.util.List;

public record CircuitStats(
        int qubits,
        int depth,
        int totalGates,
        List<String> gateTypes
) {
    @Override
    public String toString() {
        return String.format("CircuitStats{qubits=%d, depth=%d, totalGates=%d, gateTypes=%s}",
                qubits, depth, totalGates, gateTypes);
    }
}