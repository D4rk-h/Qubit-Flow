# Qubit Flow Rest API

A high-performance Java quantum circuit simulation engine with comprehensive REST API integration.

## Overview

Qubit Flow provides a complete backend solution for quantum circuit design, simulation, and analysis. Built with clean architecture principles, it supports multi-qubit quantum operations with efficient state vector simulation.

## Features

### Core Capabilities
- **Quantum Circuit Simulation** - 1-10 (10 is default but it can handle up to 16 with no performance problems) qubit systems with state vector simulation
- **Comprehensive Gate Library** - Single/multi-qubit gates with controlled variants
- **Circuit Management** - Layer-optimized construction with undo/redo support
- **Measurement Operations** - Probabilistic measurements with result tracking
- **Export** - JSON, QASM, and Qiskit format support

### Architecture
- **Clean Architecture** with domain-driven design
- **Command Pattern** for operation management and history
- **Strategy Pattern** for extensible display providers
- **REST API** with comprehensive endpoint coverage

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.8+

### Running the API
```bash
# Start the server
mvn spring-boot:run

# Verify endpoint
curl http://localhost:8080/api/quantum/circuit/info
```

### Docker Deployment
```bash
# Build and run
docker-compose up --build

# With monitoring stack
docker-compose --profile monitoring up
```

## API Usage

### Basic Circuit Operations
```bash
# Create 3-qubit circuit
POST /api/quantum/circuit/create?qubits=3

# Add quantum gates
POST /api/quantum/gates/hadamard?qubit=0
POST /api/quantum/gates/cnot?control=0&target=1
POST /api/quantum/gates/rx?qubit=0?theta=2.14

# Execute simulation
POST /api/quantum/simulate/execute

# Get probabilities
GET /api/quantum/state/probabilities
```

### Testing
Run the complete test suite:
```bash
chmod +x test_qubit_flow_api.sh
./test_qubit_flow_api.sh
```

## Gate Library

| Gate                   | Qubits | Description               |
|------------------------|--------|---------------------------|
| Hadamard               | 1      | Superposition gate        |
| Pauli-X/Y/Z            | 1      | Bit/phase flip gates      |
| RX/RY/RZ               | 1      | Rotation gates            |
| √X                     | 1      | Partial X gate            |
| P                      | 1      | Phase rotation gate       |
| T, S & dagger variants | 1      | Phase rotation gates      |
| U                      | 1      | General Unitary           |
| CNOT                   | 2      | Controlled NOT            |
| SWAP                   | 2      | Qubit exchange            |
| Toffoli                | 3      | Controlled-controlled NOT |
| Measure                | -      | Measurement Gate          |


## Programming Interface

### Direct Usage
```java
Controller controller = new Controller(3);
controller.addHadamard(0);
controller.addCNOT(0, 1);
controller.simulate();
```

### State Management
```java
QuantumState state = QuantumState.uniformSuperposition(2);
MeasurementResult result = state.measureQubit(0); // Only one qubit of state
MeasurementResult result = state.measure(); // Whole state
```

## Configuration

Configure via `application.properties`:
```properties
server.port=8080
logging.level.control=DEBUG
spring.jackson.property-naming-strategy=SNAKE_CASE
```

## Development

### Project Structure
```
quantum-core/
├── api/           # REST API layer
├── control/       # Application services
├── model/         # Domain layer
│   ├── mathModel/     # Complex numbers, matrices
│   ├── quantumModel/  # Quantum computing core
│   └── commandModel/  # Command pattern
```

### Extending the System
- **New Gates**: Extend `QuantumGate` class
- **Algorithms**: Implement in `quantumAlgorithm` package
- **Display Providers**: Implement `DisplayMeasurementPort`

## API Endpoints

| Category | Endpoint | Description |
|----------|----------|-------------|
| Circuit | `/api/quantum/circuit/*` | Circuit management |
| Gates | `/api/quantum/gates/*` | Gate operations |
| State | `/api/quantum/state/*` | State queries |
| Simulation | `/api/quantum/simulate/*` | Circuit execution |
| Measurement | `/api/quantum/measure/*` | Quantum measurements |
| History | `/api/quantum/history/*` | Undo/redo operations |
| Qubits | `/api/quantum/qubits/*` | Qubit management |

## License

Licensed under the Apache License 2.0. See [LICENSE](LICENSE) for details.

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/new-algorithm`)
3. Commit changes (`git commit -am 'Add quantum algorithm'`)
4. Push branch (`git push origin feature/new-algorithm`)
5. Create Pull Request

---

*Built for quantum computing education and research*
