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

package model.quantumModel.measurementDisplay;

import model.quantumModel.measurementDisplay.displayUtils.DisplayCell;

import java.util.UUID;

public record Display(
        Object display,
        int fromWire,
        int toWire,
        UUID id
) {
    public Display(Object displayImplementation, int fromWire, int toWire) {
        this(displayImplementation, fromWire, toWire, UUID.randomUUID());
    }

    public Display(Object displayImplementation, int wire) {
        this(displayImplementation, wire, wire, UUID.randomUUID());
    }

    public DisplayCell createCell() {
        return new DisplayCell(this);
    }

    public boolean isMultiQubit() {
        return fromWire != toWire;
    }

    public int getQubitCount() {
        return toWire - fromWire + 1;
    }

    public boolean coversWire(int wire) {
        return wire >= fromWire && wire <= toWire;
    }
}