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

package control.command.importer.qasm;

import control.command.ports.UndoableCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;

public class ImportQASMCommand implements UndoableCommand {
    private final QuantumCircuit targetCircuit;
    private final String filename;
    private QuantumCircuit previousState;
    private boolean wasExecuted;

    public ImportQASMCommand(QuantumCircuit targetCircuit, String filename) {
        this.targetCircuit = targetCircuit;
        this.filename = filename;
        this.wasExecuted = false;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }

    @Override
    public boolean canUndo() {
        return wasExecuted && previousState != null;
    }

    @Override
    public void redo() {
        execute();
    }
}
