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

package control.command.createCustom;

import control.command.QuantumCommand;
import model.mathModel.Matrix;
import model.quantumModel.quantumGate.QuantumGate;
import java.util.Stack;

public class CreateCustomGate implements QuantumCommand {
    private Matrix matrix;
    private int numOfQubits;
    private String customName;
    private Stack<QuantumGate> customGates;

    @Override
    public void execute() {
        customGates.push(new QuantumGate(matrix, numOfQubits, customName));
    }

    @Override
    public void undo() {
        customGates.pop();
    }

    @Override
    public boolean canUndo() {
        return !customGates.isEmpty();
    }

    @Override
    public void redo() {
        execute();
    }
}
