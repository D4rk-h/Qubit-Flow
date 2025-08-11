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

package model.quantumModel.quantumGate.ControlledGate;

import java.util.Arrays;

public record ControlGate (
        int[] controlQubitIndex,
        int[] targetObjectIndex
) {

    public ControlGate {
        if (controlQubitIndex == null || controlQubitIndex.length == 0) {throw new IllegalArgumentException("Control qubit indices cannot be null or empty");}
        if (targetObjectIndex == null || targetObjectIndex.length == 0) {throw new IllegalArgumentException("Target object indices cannot be null or empty");}
        if (Arrays.stream(controlQubitIndex).distinct().count() != controlQubitIndex.length) {throw new IllegalArgumentException("Control qubit indices must be unique");}
        for (int control : controlQubitIndex) {
            for (int target : targetObjectIndex) {
                if (control == target) {throw new IllegalArgumentException("Control and target indices cannot overlap");}
            }
        }
        controlQubitIndex = Arrays.copyOf(controlQubitIndex, controlQubitIndex.length);
        targetObjectIndex = Arrays.copyOf(targetObjectIndex, targetObjectIndex.length);
    }

    public ControlGate(int controlIndex, int targetIndex) {this(new int[]{controlIndex}, new int[]{targetIndex});}

    public ControlGate(int controlIndex, int[] targetIndices) {this(new int[]{controlIndex}, targetIndices);}

    public ControlGate(int[] controlIndices, int targetIndex) {this(controlIndices, new int[]{targetIndex});}

    public int getNumControls() {return controlQubitIndex.length;}

    public int getNumTargets() {return targetObjectIndex.length;}

    public boolean isSingleControl() {return controlQubitIndex.length == 1;}

    public boolean isMultiControl() {return controlQubitIndex.length > 1;}

    public int getControlIndex() {
        if (controlQubitIndex.length == 0) {throw new IllegalStateException("No control qubits defined");}
        return controlQubitIndex[0];
    }

    public int getTargetIndex() {
        if (targetObjectIndex.length == 0) {throw new IllegalStateException("No target objects defined");}
        return targetObjectIndex[0];
    }

    public boolean isControlQubit(int index) {
        return Arrays.stream(controlQubitIndex).anyMatch(i -> i == index);
    }

    public boolean isTargetObject(int index) {
        return Arrays.stream(targetObjectIndex).anyMatch(i -> i == index);
    }

    private int[] getAllIndices() {
        int[] all = new int[controlQubitIndex.length + targetObjectIndex.length];
        System.arraycopy(controlQubitIndex, 0, all, 0, controlQubitIndex.length);
        System.arraycopy(targetObjectIndex, 0, all, controlQubitIndex.length, targetObjectIndex.length);
        return all;
    }

    public int getSpan() {return Arrays.stream(getAllIndices()).max().orElse(0) - Arrays.stream(getAllIndices()).min().orElse(0) + 1;}

    public int getMinIndex() {return Arrays.stream(getAllIndices()).min().orElse(0);}

    public int getMaxIndex() {return Arrays.stream(getAllIndices()).max().orElse(0);}

    public String toDisplayString() {
        if (isSingleControl()) {
            return String.format("C%d→T%d", getControlIndex(), getTargetIndex());
        } else {
            return String.format("C%s→T%s",
                    Arrays.toString(controlQubitIndex),
                    Arrays.toString(targetObjectIndex));
        }
    }
}