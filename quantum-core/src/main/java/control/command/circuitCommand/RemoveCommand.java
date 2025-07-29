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

package control.command.circuitCommand;

import control.command.QuantumCommand;
import model.commandsModel.Location;
import model.quantumModel.QuantumGate;
import model.quantumModel.QuantumState;
import model.quantumModel.measurementDisplay.Display;

public class RemoveCommand implements QuantumCommand {
    private final Object elementToRemove;
    private final Location location;
    private Object removedElement;

    public RemoveCommand(Object elementToRemove, Location location) {
        this.elementToRemove = elementToRemove;
        this.location = location;
    }

    @Override
    public void execute() {
        if (elementToRemove instanceof QuantumGate) {
            removedElement = location.circuit().removeGate(location.wire(), location.depth());
        } else if (elementToRemove instanceof QuantumState) {
            removedElement = location.circuit().removeWire(location.wire());
        } else if (elementToRemove instanceof Display) {
            removedElement = location.circuit().removeDisplay((Display) elementToRemove);
        }
    }

    @Override
    public void undo() {
        if (removedElement != null) {
            if (removedElement instanceof QuantumGate) {
                location.circuit().add((QuantumGate) removedElement, location.wire(), location.depth());
            } else if (removedElement instanceof QuantumState) {
                location.circuit().add((QuantumState) removedElement, location.wire());
            } else if (removedElement instanceof Display) {
                location.circuit().add((Display) removedElement);
            }
        }
    }

    @Override
    public boolean canUndo() {
        return removedElement != null;
    }

    @Override
    public void redo() {
        execute();
    }
}
