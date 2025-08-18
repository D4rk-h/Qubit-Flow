package control.command.parser;

import control.command.ports.ImportParser;
import model.quantumModel.quantumCircuit.QuantumCircuit;

public class QasmParser implements ImportParser {
    private final String qasmContent;

    public QasmParser(String qasmContent) {
        this.qasmContent = qasmContent;
    }

    @Override
    public QuantumCircuit parse() {
        int numQubits = extractQubitCount(qasmContent);

        return null;
    }

    private int extractQubitCount(String qasmContent) {
        String[] lines = qasmContent.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("qreg")) {
                try {
                    int start = line.indexOf('[') + 1;
                    int end = line.indexOf(']');
                    if (start > 0 && end > start) {
                        return Integer.parseInt(line.substring(start, end));
                    }
                } catch (Exception e) {
                }
            }
        }
        return 1;
    }

    public String getQasmContent() {return qasmContent;}

}
