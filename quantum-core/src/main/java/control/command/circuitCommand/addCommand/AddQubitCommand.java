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

import model.commandsModel.Location;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.QuantumState;

public class AddQubitCommand extends AddCommand implements AddCommandPort {
    private final QuantumState state;

    public AddQubitCommand(Object state, QuantumCircuit circuit, int wire, int depth) {
        super(state, new Location(circuit, wire, depth));
        this.state = (QuantumState) state;
    }

    @Override
    public void addToCircuit() {
        location.circuit().add(state, location.wire());
    }
}
