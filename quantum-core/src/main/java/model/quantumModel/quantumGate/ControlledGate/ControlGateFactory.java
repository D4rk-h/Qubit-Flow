package model.quantumModel.quantumGate.ControlledGate;

public class ControlGateFactory {

    public ControlGate createCNOT() {return new ControlGate(0, 1);}

    public ControlGate createCNOT(int controlIndex, int targetIndex) {return new ControlGate(controlIndex, targetIndex);}

    public ControlGate createToffoli() {return new ControlGate(new int[]{0, 1}, 2);}

    public ControlGate createToffoli(int control1, int control2, int target) {return new ControlGate(new int[]{control1, control2}, target);}

    public ControlGate createFredkin() {return new ControlGate(0, new int[]{1, 2});}

    public ControlGate createFredkin(int control, int target1, int target2) {return new ControlGate(control, new int[]{target1, target2});}

    public ControlGate createMultiControl(int[] controlIndices, int targetIndex) {return new ControlGate(controlIndices, targetIndex);}

    public ControlGate createMultiTarget(int controlIndex, int[] targetIndices) {return new ControlGate(controlIndex, targetIndices);}

    public ControlGate createDiagramExample() {return new ControlGate(new int[]{0, 1}, new int[]{2, 3});}

    public ControlGate createChain(int startIndex, int length) {
        if (length < 2) {throw new IllegalArgumentException("Chain must have at least 2 qubits");}
        int[] controls = new int[length - 1];
        int[] targets = new int[length - 1];
        for (int i = 0; i < length - 1; i++) {
            controls[i] = startIndex + i;
            targets[i] = startIndex + i + 1;
        }
        return new ControlGate(controls, targets);
    }

    public boolean isValidConfiguration(int[] controlIndices, int[] targetIndices, int maxQubits) {
        try {
            ControlGate test = new ControlGate(controlIndices, targetIndices);
            return test.getMaxIndex() < maxQubits;
        } catch (Exception e) {
            return false;
        }
    }

    public ControlGate createPattern(String pattern, int offset) {
        return switch (pattern.toUpperCase()) {
            case "CNOT" -> new ControlGate(offset, offset + 1);
            case "TOFFOLI" -> new ControlGate(new int[]{offset, offset + 1}, offset + 2);
            case "FREDKIN" -> new ControlGate(offset, new int[]{offset + 1, offset + 2});
            default -> throw new IllegalArgumentException("Unknown pattern: " + pattern);
        };
    }
}