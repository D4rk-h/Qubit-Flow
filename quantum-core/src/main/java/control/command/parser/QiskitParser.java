package control.command.parser;

import control.command.ports.ImportParser;
import model.quantumModel.quantumCircuit.QuantumCircuit;

public class QiskitParser implements ImportParser {
    private final String qiskitContent;
    public QiskitParser(String qiskitContent) {
        this.qiskitContent = qiskitContent;
    }

    @Override
    public QuantumCircuit parse() {
        int nQubit = extractQubitCount(qiskitContent);

        return null;
    }

    private int extractQubitCount(String qiskitContent) {
        String[] lines = qiskitContent.split("\n");
        for (String line : lines) {
            if (line.trim().contains("QuantumRegister(")) {
                try {
                    int start = line.indexOf('(') + 1;
                    int end = line.indexOf(',');
                    if (end == -1) end = line.indexOf(')');
                    if (start > 0 && end > start) {
                        String numStr = line.substring(start, end).trim();
                        return Integer.parseInt(numStr);
                    }
                } catch (Exception e) {
                }
            }
        }
        return 1;
    }

    public String getQiskitContent() {return qiskitContent;}

}
