package control.command.parser;

import control.command.ports.ImportParser;
import model.quantumModel.quantumCircuit.QuantumCircuit;
import model.quantumModel.quantumGate.QuantumGates;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QiskitParser implements ImportParser {
    private final String qiskitContent;
    public QiskitParser(String qiskitContent) {
        this.qiskitContent = qiskitContent;
    }

    @Override
    public QuantumCircuit parse() {
        String qiskitContent = getQiskitContent();
        int nQubit = extractQubitCount(qiskitContent);
        String circuitVar = extractCircuitVarName(qiskitContent);
        if (nQubit == 0 || circuitVar == null) return new QuantumCircuit(0);
        QuantumCircuit circuit = new QuantumCircuit(nQubit);
        final Pattern indexPattern = Pattern.compile("\\d+");
        final Pattern instructionPattern = Pattern.compile(
                "^\\s*" + Pattern.quote(circuitVar) + "\\.(\\w+)\\(([^)]*)\\)"
        );
        String[] lines = qiskitContent.split("\\R");
        for (String line : lines) {
            Matcher matcher = instructionPattern.matcher(line.trim());
            if (matcher.find()) {
                String operation = matcher.group(1);
                String args = matcher.group(2);
                List<Integer> indices = new ArrayList<>();
                Matcher indexMatcher = indexPattern.matcher(args);
                while (indexMatcher.find()) indices.add(Integer.parseInt(indexMatcher.group()));
                if (indices.isEmpty()) {continue;}
                switch (operation) {
                    case "h":       circuit.addHadamard(indices.get(0)); break;
                    case "x":       circuit.addNot(indices.get(0)); break;
                    case "y":       circuit.addY(indices.get(0)); break;
                    case "z":       circuit.addZ(indices.get(0)); break;
                    case "s":       circuit.addPhase(indices.get(0)); break;
                    case "t":       circuit.addT(indices.get(0)); break;
                    case "cx":      circuit.addCNOT(indices.get(0), indices.get(1)); break;
                    case "cy":      circuit.addControlled(QuantumGates.y(), indices.get(0), indices.get(1)); break;
                    case "cz":      circuit.addControlled(QuantumGates.z(), indices.get(0), indices.get(1)); break;
                    case "ch":      circuit.addControlled(QuantumGates.hadamard(), indices.get(0), indices.get(1)); break;
                    case "swap":    circuit.addSwap(indices.get(0), indices.get(1)); break;
                    case "ccx":     circuit.addToffoli(indices.get(0), indices.get(1), indices.get(2)); break;
                    case "cswap":   circuit.addControlled(QuantumGates.swap(), indices.get(0), indices.get(1), indices.get(2)); break;
                    case "measure":
                        circuit.addMeasurement(indices.get(0), indices.get(1));
                        break;
                }
            }
        }
        return circuit;
    }

    private int extractQubitCount(String qiskitContent) {
        Pattern qregPattern = Pattern.compile("QuantumRegister\\s*\\(\\s*(\\d+)");
        Matcher matcher = qregPattern.matcher(qiskitContent);
        if (matcher.find()) return Integer.parseInt(matcher.group(1));
        return 0;
    }

    private String extractCircuitVarName(String qiskitContent) {
        Pattern circuitVarPattern = Pattern.compile("(\\w+)\\s*=\\s*QuantumCircuit");
        Matcher matcher = circuitVarPattern.matcher(qiskitContent);
        if (matcher.find()) return matcher.group(1);
        return null;
    }
    public String getQiskitContent() {return qiskitContent;}

}
