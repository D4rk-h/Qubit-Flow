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

package control.util;

import java.util.ArrayList;
import java.util.List;

public record CircuitStats(
        int qubits,
        int depth,
        int totalGates,
        int totalOperations,
        List<Integer> layerDistribution
    )
{
    public CircuitStats(int qubits, int depth, int totalGates, int totalOperations, List<Integer> layerDistribution) {
        this.qubits = qubits;
        this.depth = depth;
        this.totalGates = totalGates;
        this.totalOperations = totalOperations;
        this.layerDistribution = new ArrayList<>(layerDistribution);
    }

    @Override
    public List<Integer> layerDistribution() {return new ArrayList<>(layerDistribution);}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Circuit Statistics:\n");
        sb.append("- Qubits: ").append(qubits).append("\n");
        sb.append("- Depth: ").append(depth).append("\n");
        sb.append("- Total Gates: ").append(totalGates).append("\n");
        sb.append("- Total Operations: ").append(totalOperations).append("\n");
        sb.append("- Average Operations per Layer: ")
                .append(depth > 0 ? (double) totalOperations / depth : 0).append("\n");

        return sb.toString();
    }
}
