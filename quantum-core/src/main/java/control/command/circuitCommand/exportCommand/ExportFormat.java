package control.command.circuitCommand.exportCommand;

public enum ExportFormat {
    JSON("json"),
    QASM("qasm"),
    QISKIT("py");

    private final String extension;

    ExportFormat(String extension) {this.extension = extension;}

    public String getExtension() {return extension;}
}