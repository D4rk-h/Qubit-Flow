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

package control.command;

import java.util.Stack;

public class CommandHistory {
    private final Stack<QuantumCommand> undoStack;
    private final Stack<QuantumCommand> redoStack;

    public CommandHistory() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void executeCommand(QuantumCommand command) {
        command.execute();
        if (command.canUndo()) {
            undoStack.push(command);
            redoStack.clear();
        }
    }

    public boolean undo() {
        if (canUndo()) {
            QuantumCommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            return true;
        }
        return false;
    }

    public boolean redo() {
        if (canRedo()) {
            QuantumCommand command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            return true;
        }
        return false;
    }

    public boolean canUndo() {return !undoStack.isEmpty();}

    public boolean canRedo() {return !redoStack.isEmpty();}

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }

    public int getUndoStackSize() {return undoStack.size();}

    public int getRedoStackSize() {return redoStack.size();}
}