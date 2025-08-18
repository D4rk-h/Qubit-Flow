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

package control.command.history;

import control.command.add.AddQubitCommand;
import control.command.ports.UndoableCommand;
import control.command.remover.RemoveQubitCommand;

import java.util.Stack;
import java.util.function.BiConsumer;

public class CommandHistory {
    private final Stack<UndoableCommand> undoStack;
    private final Stack<UndoableCommand> redoStack;
    private BiConsumer<Object, Object> stateRestorer;

    public CommandHistory() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void setStateRestorer(BiConsumer<Object, Object> restorer) {this.stateRestorer = restorer;}

    public void executeCommand(UndoableCommand command) {
        if (command == null) throw new IllegalArgumentException("Command cannot be null");
        command.execute();
        if (command.canUndo()) {
            undoStack.push(command);
            redoStack.clear();
        }
    }

    public boolean undo() {
        if (!canUndo()) return false;
        UndoableCommand command = undoStack.pop();
        executeUndoWithStateRestoration(command);
        redoStack.push(command);
        return true;
    }

    public boolean redo() {
        if (!canRedo()) return false;
        UndoableCommand command = redoStack.pop();
        executeRedoWithStateRestoration(command);
        undoStack.push(command);
        return true;
    }

    public boolean canUndo() {return !undoStack.isEmpty();}

    public boolean canRedo() {return !redoStack.isEmpty();}

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }

    public int getUndoStackSize() {return undoStack.size();}

    public int getRedoStackSize() {return redoStack.size();}

    public String getLastCommandType() {
        return undoStack.isEmpty() ? "None" : undoStack.peek().getClass().getSimpleName();
    }

    private void executeUndoWithStateRestoration(UndoableCommand command) {
        command.undo();
        if (stateRestorer != null) restoreStateForCommand(command, true);
    }

    private void executeRedoWithStateRestoration(UndoableCommand command) {
        command.execute();
        if (stateRestorer != null) restoreStateForCommand(command, false);
    }

    private void restoreStateForCommand(UndoableCommand command, boolean isUndo) {
        switch (command) {
            case AddQubitCommand addCmd -> {
                if (isUndo) stateRestorer.accept(addCmd.getOriginalCircuit(), addCmd.getOriginalState());
                else stateRestorer.accept(addCmd.getNewCircuit(), addCmd.getNewState());
            }
            case RemoveQubitCommand removeCmd -> {
                if (isUndo) stateRestorer.accept(removeCmd.getOriginalCircuit(), removeCmd.getOriginalState());
                else stateRestorer.accept(removeCmd.getNewCircuit(), removeCmd.getNewState());
            }
            default -> {
            }
        }
    }
}