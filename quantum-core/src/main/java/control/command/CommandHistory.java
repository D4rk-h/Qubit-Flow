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