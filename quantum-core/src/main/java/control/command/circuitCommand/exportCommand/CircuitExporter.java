package control.command.circuitCommand.exportCommand;

import control.ExportStrategy;
import control.command.UndoableCommand;
import model.quantumModel.quantumCircuit.QuantumCircuit;

import java.util.Map;

public class CircuitExporter {
    private final Map<ExportFormat, ExportStrategy> strategies;

    public CircuitExporter() {
        this.strategies = Map.of(
                ExportFormat.JSON, new JSONExportStrategy(),
                ExportFormat.QASM, new QasmExportStrategy()
        );
    }

    public void export(QuantumCircuit circuit, ExportFormat format) {
        ExportStrategy strategy = strategies.get(format);
        if (strategy == null) {
            throw new UnsupportedOperationException("Export format not supported: " + format);
        }

        String filename = generateFilename(format);
        strategy.export(circuit, filename);
    }

    public UndoableCommand createExportCommand(QuantumCircuit circuit, ExportFormat format) {
        ExportStrategy strategy = strategies.get(format);
        if (strategy == null) {
            throw new UnsupportedOperationException("Export format not supported: " + format);
        }

        String filename = generateFilename(format);
        return strategy.createExportCommand(circuit, filename);
    }

    private String generateFilename(ExportFormat format) {
        return "./exported_circuit." + format.getExtension();
    }
}