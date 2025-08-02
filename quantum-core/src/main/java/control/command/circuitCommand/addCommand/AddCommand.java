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

package control.command.circuitCommand.addCommand;

import control.command.QuantumCommand;
import model.quantumModel.measurementDisplay.Display;
import model.commandsModel.Location;
import model.quantumModel.QuantumGate;
import model.quantumModel.quantumState.QuantumState;


public class AddCommand implements QuantumCommand {
    private final Object elementToAdd;
    public final Location location;
    private AddCommandPort addCommandDelegate;

    public AddCommand(Object elementToAdd, Location location){
        this.elementToAdd = elementToAdd;
        this.location = location;
    }

    @Override
    public void execute() {
        if (elementToAdd instanceof QuantumGate) {
            AddGateCommand add = new AddGateCommand(elementToAdd, location.circuit(), location.wire(), location.depth());
            add.addToCircuit();
        } else if (elementToAdd instanceof QuantumState) {
            AddQubitCommand add = new AddQubitCommand(elementToAdd, location.circuit(), location.wire(), location.depth());
            add.addToCircuit();
        } else if (elementToAdd instanceof Display) {
            AddDisplayCommand add = new AddDisplayCommand(elementToAdd, location.circuit(), location.wire(), location.depth());
            add.addToCircuit();
        } else {
            throw new IllegalArgumentException("Cannot create AddCommand objects with elementToAdd being instance of " + elementToAdd.getClass());
        }
    }

    @Override
    public void undo() {
        if (canUndo()) {
            if (elementToAdd instanceof QuantumGate) {
                location.circuit().removeGate(location.wire(), location.depth());
            } else if (elementToAdd instanceof QuantumState) {
                location.circuit().removeWire(location.wire());
            } else if (elementToAdd instanceof Display) {
                location.circuit().removeDisplay((Display) elementToAdd);
            }
        }
    }

    @Override
    public boolean canUndo() {
        return addCommandDelegate != null;
    }

    @Override
    public void redo() {
        if (addCommandDelegate != null) {
            addCommandDelegate.addToCircuit();
        }
    }
}
